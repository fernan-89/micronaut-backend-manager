package com.local.app.service;

import com.local.app.dto.request.ComputeResourceRequest;
import com.local.app.dto.request.ComputeResourceTelemetryRequest;
import com.local.app.dto.response.ComputeResourceResponse;
import com.local.app.model.AuditLogModel;
import com.local.app.model.ComputeResourceModel;
import com.local.app.model.MaintenanceWindow;
import com.local.app.model.NetworkConfig;
import com.local.app.model.ResourceMetrics;
import com.local.app.model.enums.InfrastructureProvider;
import com.local.app.model.enums.ResourceStatus;
import com.local.app.model.enums.ResourceType;
import com.local.app.repository.AuditLogRepository;
import com.local.app.repository.ComputeResourceRepository;
import com.local.app.service.exception.BusinessException;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Core business service orchestrating multi-cloud and on-premises compute resources.
 * Manages inventory registration pipelines, ingests telemetry, and governs SRE maintenance lifecycles.
 */
@Singleton
public class ComputeResourceService {

    private final ComputeResourceRepository repository;
    private final AuditLogRepository logRepository;

    /** Inbound command record DTO dedicated to scheduling future resource maintenance windows. */
    @Serdeable
    public record MaintenanceScheduleCmd(
            @NotNull Instant scheduledStart,
            @NotNull Instant scheduledEnd,
            @NotBlank String reason,
            Boolean silenceAlerts
    ) {}

    /** Inbound command record DTO dedicated to starting an immediate or scheduled maintenance window. */
    @Serdeable
    public record MaintenanceStartCmd(
            @NotBlank String reason,
            Boolean silenceAlerts
    ) {}

    /** Dependency injection constructor. */
    public ComputeResourceService(ComputeResourceRepository repository, AuditLogRepository logRepository) {
        this.repository = repository;
        this.logRepository = logRepository;
    }

    /** Registers and provisions a new compute resource inside the unified infrastructure matrix. */
    public ComputeResourceResponse registerResource(ComputeResourceRequest request, String executor) {
        String resourceHash = generateResourceHash();
        Instant now = Instant.now();

        NetworkConfig networkConfig = new NetworkConfig(
                request.privateIp(), request.publicIp(), request.subnetCidr(),
                request.gateway(), request.macAddress(), request.internalDnsName(), request.externalFqdn()
        );

        ResourceMetrics initialMetrics = new ResourceMetrics(0.0, 0L, 0L, 0.0, 0.0, now);

        ComputeResourceModel model = new ComputeResourceModel(
                null,
                resourceHash,
                request.name(),
                ResourceType.fromString(request.resourceType()),
                InfrastructureProvider.fromString(request.provider()),
                ResourceStatus.PROVISIONING,
                request.vCpus(),
                request.ramMemoryMb(),
                request.storageCapacityGb(),
                request.operatingSystem(),
                networkConfig,
                initialMetrics,
                null, // Newborn instance has no maintenance record assigned
                request.spec(),
                now,
                now
        );

        logRepository.save(new AuditLogModel(
                generateLogHash(), resourceHash, "Compute infrastructure resource cataloged", Collections.emptyMap(), LocalDateTime.now(), executor
        ));

        return mapToResponse(repository.save(model));
    }

    /** Locates a single resource entry by its active asset tracking token hash. */
    public ComputeResourceResponse findByHash(String resourceHash) {
        return repository.findByResourceHash(resourceHash)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BusinessException("Compute resource asset not found matching token: " + resourceHash));
    }

    /** Ingests hardware telemetry streams. Mutes telemetry state shifts if node is under hard maintenance. */
    public ComputeResourceResponse updateTelemetry(String resourceHash, ComputeResourceTelemetryRequest telemetry) {
        return repository.findByResourceHash(resourceHash).map(resource -> {
            Instant now = Instant.now();
            ResourceStatus incomingStatus = ResourceStatus.fromString(telemetry.status());
            ResourceStatus oldStatus = resource.getStatus();

            ResourceMetrics updatedMetrics = new ResourceMetrics(
                    telemetry.cpuUsagePercentage(),
                    telemetry.ramUsageMb(),
                    telemetry.diskUsageGb(),
                    telemetry.networkInMbitSec(),
                    telemetry.networkOutMbitSec(),
                    now
            );

            resource.setMetrics(updatedMetrics);

            // SRE Safety Gate: If under MAINTENANCE, ignore incoming agent pings trying to force RUNNING state
            if (oldStatus == ResourceStatus.MAINTENANCE && incomingStatus == ResourceStatus.RUNNING) {
                // Keep maintenance locked down, update telemetry metrics only
                resource.setUpdatedAt(now);
            } else {
                resource.setStatus(incomingStatus);
                resource.setUpdatedAt(now);

                if (oldStatus != incomingStatus) {
                    logRepository.save(new AuditLogModel(
                            generateLogHash(), resourceHash, "Compute node status shift event triggered",
                            Map.of("previousState", new AuditLogModel.ChangeDetail(oldStatus.name(), incomingStatus.name())),
                            LocalDateTime.now(), "AUTOMATED-MONITORING-AGENT"
                    ));
                }
            }

            return mapToResponse(repository.update(resource));
        }).orElseThrow(() -> new BusinessException("Telemetry ingestion dropped. Compute target hash not found: " + resourceHash));
    }

    /** Schedules a future operational maintenance window block for a target infrastructure asset. */
    public ComputeResourceResponse scheduleMaintenance(String resourceHash, MaintenanceScheduleCmd cmd, String executor) {
        return repository.findByResourceHash(resourceHash).map(resource -> {
            if (cmd.scheduledStart().isAfter(cmd.scheduledEnd())) {
                throw new BusinessException("Invalid timeline sequence: Scheduled start cannot execute after the scheduled expiration date.");
            }

            MaintenanceWindow window = new MaintenanceWindow(
                    cmd.scheduledStart(), cmd.scheduledEnd(), cmd.reason(), executor, cmd.silenceAlerts(), null, null
            );

            resource.setMaintenanceWindow(window);
            resource.setUpdatedAt(Instant.now());

            logRepository.save(new AuditLogModel(
                    generateLogHash(), resourceHash, "Future infrastructure maintenance timeline reserved",
                    Map.of("scheduledStart", new AuditLogModel.ChangeDetail(null, cmd.scheduledStart().toString())),
                    LocalDateTime.now(), executor
            ));

            return mapToResponse(repository.update(resource));
        }).orElseThrow(() -> new BusinessException("Scheduling rejected. Compute resource asset not found: " + resourceHash));
    }

    /** Forces an infrastructure node into hard MAINTENANCE state, executing alarm suppression pathways. */
    public ComputeResourceResponse startMaintenance(String resourceHash, MaintenanceStartCmd cmd, String executor) {
        return repository.findByResourceHash(resourceHash).map(resource -> {
            Instant now = Instant.now();
            ResourceStatus oldStatus = resource.getStatus();

            MaintenanceWindow existing = resource.getMaintenanceWindow();
            MaintenanceWindow activeWindow = new MaintenanceWindow(
                    existing != null ? existing.scheduledStart() : now,
                    existing != null ? existing.scheduledEnd() : now.plusSeconds(7200), // Default 2h fallback
                    cmd.reason(), executor, cmd.silenceAlerts(), now, null
            );

            resource.setMaintenanceWindow(activeWindow);
            resource.setStatus(ResourceStatus.MAINTENANCE);
            resource.setUpdatedAt(now);

            logRepository.save(new AuditLogModel(
                    generateLogHash(), resourceHash, "Asset isolation triggered: Node shifted into hard MAINTENANCE mode",
                    Map.of("statusChange", new AuditLogModel.ChangeDetail(oldStatus.name(), ResourceStatus.MAINTENANCE.name())),
                    LocalDateTime.now(), executor
            ));

            return mapToResponse(repository.update(resource));
        }).orElseThrow(() -> new BusinessException("Execution rejected. Target compute resource asset not found: " + resourceHash));
    }

    /** Recovers a compute asset from MAINTENANCE mode, terminating suppression and returning to verification loops. */
    public ComputeResourceResponse endMaintenance(String resourceHash, String executor) {
        return repository.findByResourceHash(resourceHash).map(resource -> {
            if (resource.getStatus() != ResourceStatus.MAINTENANCE) {
                throw new BusinessException("Recovery sequence aborted: Compute asset is not currently flagged under a MAINTENANCE cycle.");
            }

            Instant now = Instant.now();
            MaintenanceWindow existing = resource.getMaintenanceWindow();

            if (existing != null) {
                MaintenanceWindow completedWindow = new MaintenanceWindow(
                        existing.scheduledStart(), existing.scheduledEnd(), existing.reason(),
                        existing.approvedBy(), existing.silenceAlerts(), existing.actualStart(), now
                );
                resource.setMaintenanceWindow(completedWindow);
            }

            // Shifts to PROVISIONING to wait for the first valid heartbeat push from edge agents
            resource.setStatus(ResourceStatus.PROVISIONING);
            resource.setUpdatedAt(now);

            logRepository.save(new AuditLogModel(
                    generateLogHash(), resourceHash, "Asset maintenance cycle concluded. Shifted to verification loop.",
                    Map.of("statusChange", new AuditLogModel.ChangeDetail(ResourceStatus.MAINTENANCE.name(), ResourceStatus.PROVISIONING.name())),
                    LocalDateTime.now(), executor
            ));

            return mapToResponse(repository.update(resource));
        }).orElseThrow(() -> new BusinessException("Recovery sequence failed. Compute resource asset not found: " + resourceHash));
    }

    private ComputeResourceResponse mapToResponse(ComputeResourceModel model) {
        return new ComputeResourceResponse(
                model.getResourceHash(), model.getName(), model.getResourceType(),
                model.getProvider(), model.getStatus(), model.getvCpus(), model.getRamMemoryMb(),
                model.getStorageCapacityGb(), model.getOperatingSystem(), model.getNetworkConfig(),
                model.getMetrics(), model.getMaintenanceWindow(), model.getSpec(), model.getCreatedAt(), model.getUpdatedAt()
        );
    }

    private String generateResourceHash() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return "COM-" + datePart + "-" + randomPart.substring(0, 4);
    }

    private String generateLogHash() {
        return "LOG-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
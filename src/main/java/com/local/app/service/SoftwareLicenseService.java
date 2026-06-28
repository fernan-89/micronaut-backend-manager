package com.local.app.service;

import com.local.app.dto.request.SoftwareLicenseAllocationRequest;
import com.local.app.dto.request.SoftwareLicenseRequest;
import com.local.app.dto.response.SoftwareLicenseResponse;
import com.local.app.exception.LicenseDepletedException;
import com.local.app.model.AuditLogModel;
import com.local.app.model.LicenseAllocation;
import com.local.app.model.SoftwareLicenseModel;
import com.local.app.model.enums.LicenseStatus;
import com.local.app.model.enums.LicenseType;
import com.local.app.repository.AuditLogRepository;
import com.local.app.repository.SoftwareLicenseRepository;
import com.local.app.service.exception.BusinessException;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Core business service orchestrating corporate software license agreement lifecycles.
 * Validates availability quotas, expiration states, and logs events into the global audit timeline.
 */
@Singleton
public class SoftwareLicenseService {

    private final SoftwareLicenseRepository repository;
    private final AuditLogRepository logRepository;

    /**
     * Dependency injection constructor.
     */
    public SoftwareLicenseService(SoftwareLicenseRepository repository, AuditLogRepository logRepository) {
        this.repository = repository;
        this.logRepository = logRepository;
    }

    /**
     * Registers a new software license agreement contract into the inventory system.
     *
     * @param request  Inbound contract validated specifications.
     * @param executor Enterprise administrative agent executing the operation.
     * @return Outbound response data transfer projection.
     */
    public SoftwareLicenseResponse createLicense(SoftwareLicenseRequest request, String executor) {
        String licenseHash = generateLicenseHash();
        Instant now = Instant.now();

        SoftwareLicenseModel model = new SoftwareLicenseModel(
                null,
                licenseHash,
                request.softwareName(),
                request.publisher(),
                request.licenseKey(),
                LicenseType.fromString(request.licenseType()),
                LicenseStatus.ACTIVE,
                request.totalSeats(),
                0,
                request.acquisitionDate(),
                request.expirationDate(),
                now,
                now,
                new ArrayList<>()
        );

        // Auto-check compliance flag if acquisition date arrives pre-expired
        if (request.expirationDate() != null && request.expirationDate().isBefore(now)) {
            model.setStatus(LicenseStatus.EXPIRED);
        }

        logRepository.save(new AuditLogModel(
                generateLogHash(), licenseHash, "Software license agreement registered", Collections.emptyMap(), LocalDateTime.now(), executor
        ));

        return mapToResponse(repository.save(model));
    }

    /**
     * Locates a distinct software license profile entry by its active business key string token.
     */
    public SoftwareLicenseResponse findByHash(String licenseHash) {
        return repository.findByLicenseHash(licenseHash)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BusinessException("Software license contract not found matching hash: " + licenseHash));
    }

    /**
     * Allocates an atomic software license seat deployment targeted against a hardware asset or an employee user.
     *
     * @param licenseHash The operational license business key string token.
     * @param request     Target routing identification parameters.
     * @param executor    Supervisor executing the inventory allocation change.
     * @return Updated software license snapshot representation profile.
     */
    public SoftwareLicenseResponse allocateSeat(String licenseHash, SoftwareLicenseAllocationRequest request, String executor) {
        return repository.findByLicenseHash(licenseHash).map(license -> {

            // Core Compliance Guard 1: Block allocation on non-active contracts
            if (license.getStatus() == LicenseStatus.EXPIRED || license.getStatus() == LicenseStatus.SUSPENDED) {
                throw new BusinessException("Compliance restriction error. Cannot allocate seats on a license with status: " + license.getStatus());
            }

            // Core Compliance Guard 2: Enforce seat capacity ceiling bounds
            if (license.getAllocatedSeats() >= license.getTotalSeats()) {
                license.setStatus(LicenseStatus.DEPLETED);
                repository.update(license);
                throw new LicenseDepletedException("Compliance restriction error. No available seats remaining for license: " + licenseHash);
            }

            // Build structural allocation record
            LicenseAllocation allocation = new LicenseAllocation(
                    request.assetHash(),
                    request.userId(),
                    Instant.now(),
                    executor
            );

            license.addAllocation(allocation);

            // Re-evaluate if capacity has hit the absolute limit after this allocation
            if (license.getAllocatedSeats().equals(license.getTotalSeats())) {
                license.setStatus(LicenseStatus.DEPLETED);
            }

            logRepository.save(new AuditLogModel(
                    generateLogHash(), licenseHash, "Software license seat allocated successfully",
                    Map.of("targetAsset", new AuditLogModel.ChangeDetail(null, request.assetHash()), "targetUser", new AuditLogModel.ChangeDetail(null, request.userId())),
                    LocalDateTime.now(), executor
            ));

            return mapToResponse(repository.update(license));
        }).orElseThrow(() -> new BusinessException("Cannot allocate seat. Software license contract not found: " + licenseHash));
    }

    private SoftwareLicenseResponse mapToResponse(SoftwareLicenseModel model) {
        return new SoftwareLicenseResponse(
                model.getLicenseHash(), model.getSoftwareName(), model.getPublisher(),
                model.getLicenseKey(), model.getLicenseType(), model.getStatus(),
                model.getTotalSeats(), model.getAllocatedSeats(), model.getAcquisitionDate(),
                model.getExpirationDate(), model.getCreatedAt(), model.getUpdatedAt(), model.getAllocations()
        );
    }

    private String generateLicenseHash() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return "LIC-" + datePart + "-" + randomPart.substring(0, 4);
    }

    private String generateLogHash() {
        return "LOG-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
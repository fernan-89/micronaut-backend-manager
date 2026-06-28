package com.local.app.model;

import com.local.app.model.enums.InfrastructureProvider;
import com.local.app.model.enums.ResourceStatus;
import com.local.app.model.enums.ResourceType;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Enterprise core Mapped Mongo entity representing a unified, multi-cloud,
 * hybrid, or on-premises compute environment instance node with advanced SRE lifecycle support.
 */
@Serdeable
@MappedEntity("compute_resources")
public class ComputeResourceModel {

    @Id
    private String id;
    private String resourceHash;
    private String name;
    private ResourceType resourceType;
    private InfrastructureProvider provider;
    private ResourceStatus status;

    // Core Provisioned Compute Capacity
    private Integer vCpus;
    private Integer ramMemoryMb;
    private Long storageCapacityGb;
    private String operatingSystem;

    // Complex Structural Embeds
    private NetworkConfig networkConfig;
    private ResourceMetrics metrics;

    /** Embedded SRE governance block detailing current active or future scheduled maintenance configurations. */
    private MaintenanceWindow maintenanceWindow;

    /**
     * Dynamic open-ended metadata block. Captures custom hypervisor configurations,
     * cloud tags, cloud provider identification IDs, or home-lab hardware configurations.
     */
    private Map<String, Object> spec;

    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Default constructor for mapping frameworks and reflection-free serde.
     */
    public ComputeResourceModel() {
    }

    /**
     * Fully detailed constructor initialization with maintenance window support.
     */
    public ComputeResourceModel(String id, String resourceHash, String name, ResourceType resourceType,
                                InfrastructureProvider provider, ResourceStatus status, Integer vCpus,
                                Integer ramMemoryMb, Long storageCapacityGb, String operatingSystem,
                                NetworkConfig networkConfig, ResourceMetrics metrics, MaintenanceWindow maintenanceWindow,
                                Map<String, Object> spec, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.resourceHash = resourceHash;
        this.name = name;
        this.resourceType = resourceType;
        this.provider = provider;
        this.status = status;
        this.vCpus = vCpus;
        this.ramMemoryMb = ramMemoryMb;
        this.storageCapacityGb = storageCapacityGb;
        this.operatingSystem = operatingSystem;
        this.networkConfig = networkConfig;
        this.metrics = metrics;
        this.maintenanceWindow = maintenanceWindow;
        this.spec = spec != null ? spec : new HashMap<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getResourceHash() { return resourceHash; }
    public void setResourceHash(String resourceHash) { this.resourceHash = resourceHash; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ResourceType getResourceType() { return resourceType; }
    public void setResourceType(ResourceType resourceType) { this.resourceType = resourceType; }

    public InfrastructureProvider getProvider() { return provider; }
    public void setProvider(InfrastructureProvider provider) { this.provider = provider; }

    public ResourceStatus getStatus() { return status; }
    public void setStatus(ResourceStatus status) { this.status = status; }

    public Integer getvCpus() { return vCpus; }
    public void setvCpus(Integer vCpus) { this.vCpus = vCpus; }

    public Integer getRamMemoryMb() { return ramMemoryMb; }
    public void setRamMemoryMb(Integer ramMemoryMb) { this.ramMemoryMb = ramMemoryMb; }

    public Long getStorageCapacityGb() { return storageCapacityGb; }
    public void setStorageCapacityGb(Long storageCapacityGb) { this.storageCapacityGb = storageCapacityGb; }

    public String getOperatingSystem() { return operatingSystem; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }

    public NetworkConfig getNetworkConfig() { return networkConfig; }
    public void setNetworkConfig(NetworkConfig networkConfig) { this.networkConfig = networkConfig; }

    public ResourceMetrics getMetrics() { return metrics; }
    public void setMetrics(ResourceMetrics metrics) { this.metrics = metrics; }

    public MaintenanceWindow getMaintenanceWindow() { return maintenanceWindow; }
    public void setMaintenanceWindow(MaintenanceWindow maintenanceWindow) { this.maintenanceWindow = maintenanceWindow; }

    public Map<String, Object> getSpec() { return spec; }
    public void setSpec(Map<String, Object> spec) { this.spec = spec; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
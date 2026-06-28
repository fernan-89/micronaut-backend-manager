package com.local.app.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;

/**
 * Inbound API Request payload containing comprehensive parameters for registering,
 * provisioning, and cataloging a cloud or on-premises compute resource.
 */
@Serdeable
@Introspected
public record ComputeResourceRequest(
        @NotBlank(message = "Resource instance descriptive name cannot be blank") String name,
        @NotBlank(message = "Resource virtualization architecture type layer cannot be blank") String resourceType,
        @NotBlank(message = "Infrastructure hosting provider deployment location cannot be blank") String provider,

        @NotNull(message = "Allocated virtual CPU core quantity must be provided")
        @Positive(message = "vCPUs volume allocation must be greater than zero") Integer vCpus,

        @NotNull(message = "Allocated RAM memory size must be provided")
        @Positive(message = "RAM memory allocation size in Megabytes must be greater than zero") Integer ramMemoryMb,

        @NotNull(message = "Persistent storage block volume capacity must be provided")
        @Positive(message = "Storage capacity allocation in Gigabytes must be greater than zero") Long storageCapacityGb,

        @NotBlank(message = "Target node operating system or base image template name cannot be blank") String operatingSystem,

        // Detailed Network Connectivity Addresses Block
        String privateIp,
        String publicIp,
        String subnetCidr,
        String gateway,
        String macAddress,
        String internalDnsName,
        String externalFqdn,

        /** Dynamic polymorphism specification block mapping open metadata configurations (AWS tags, Docker keys, etc.) */
        Map<String, Object> spec
) {}
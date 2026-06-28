package com.local.app.dto.response;

import com.local.app.model.MaintenanceWindow; // <- Garanta que este import existe
import com.local.app.model.NetworkConfig;
import com.local.app.model.ResourceMetrics;
import com.local.app.model.enums.InfrastructureProvider;
import com.local.app.model.enums.ResourceStatus;
import com.local.app.model.enums.ResourceType;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.Map;

/**
 * Outbound unified JSON data snapshot representing a complete hybrid, cloud,
 * or on-premises compute environment profile with active network and SRE maintenance configurations.
 */
@Serdeable
public record ComputeResourceResponse(
        String resourceHash,
        String name,
        ResourceType resourceType,
        InfrastructureProvider provider,
        ResourceStatus status,
        Integer vCpus,
        Integer ramMemoryMb,
        Long storageCapacityGb,
        String operatingSystem,
        NetworkConfig networkConfig,
        ResourceMetrics metrics,
        MaintenanceWindow maintenanceWindow, // <- Adicionado aqui para bater com os 15 parâmetros do Service
        Map<String, Object> spec,
        Instant createdAt,
        Instant updatedAt
) {}
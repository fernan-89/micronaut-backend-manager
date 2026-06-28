package com.local.app.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * High-performance inbound telemetry payload pushed by remote monitoring agents or cloud metrics scrapers.
 * Updates dynamic strain levels, capacity usage, and overrides heartbeat states.
 */
@Serdeable
@Introspected
public record ComputeResourceTelemetryRequest(
        @NotBlank(message = "Operational status signature node cannot be blank") String status,

        @NotNull(message = "Instantaneous CPU usage ratio core must be provided")
        @Min(value = 0, message = "CPU percentage consumption floor bounds must be 0%")
        @Max(value = 100, message = "CPU percentage consumption ceiling bounds cannot exceed 100%") Double cpuUsagePercentage,

        @NotNull(message = "Active RAM memory consumption metrics must be provided")
        @Min(value = 0, message = "RAM memory utilization size in MB cannot be negative") Long ramUsageMb,

        @NotNull(message = "Active disk space consumption metrics must be provided")
        @Min(value = 0, message = "Storage utilization size in GB cannot be negative") Long diskUsageGb,

        Double networkInMbitSec,  // Inbound network throughput bandwidth speed rate
        Double networkOutMbitSec  // Outbound network throughput bandwidth speed rate
) {}
package com.local.app.model;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;

/**
 * Embedded subdocument tracking real-time telemetry, capacity thresholds,
 * and hardware processing strain signatures for a compute resource.
 */
@Serdeable
public record ResourceMetrics(
        Double cpuUsagePercentage,    // Instantaneous CPU consumption (e.g., 42.5%)
        Long ramUsageMb,             // Active RAM memory consumed in Megabytes
        Long diskUsageGb,            // Persistent storage volume utilization in Gigabytes
        Double networkInMbitSec,     // Inbound network throughput bandwidth traffic load
        Double networkOutMbitSec,    // Outbound network throughput bandwidth traffic load
        Instant lastTelemetryAt      // Precise UTC timestamp of the last agent ping metric push
) {
    /**
     * Compact constructor to inject safety fallbacks and sanitize telemetry numbers.
     */
    public ResourceMetrics {
        cpuUsagePercentage = (cpuUsagePercentage == null) ? 0.0 : Math.max(0.0, Math.min(100.0, cpuUsagePercentage));
        ramUsageMb = (ramUsageMb == null) ? 0L : Math.max(0L, ramUsageMb);
        diskUsageGb = (diskUsageGb == null) ? 0L : Math.max(0L, diskUsageGb);
        networkInMbitSec = (networkInMbitSec == null) ? 0.0 : Math.max(0.0, networkInMbitSec);
        networkOutMbitSec = (networkOutMbitSec == null) ? 0.0 : Math.max(0.0, networkOutMbitSec);
        lastTelemetryAt = (lastTelemetryAt == null) ? Instant.now() : lastTelemetryAt;
    }
}
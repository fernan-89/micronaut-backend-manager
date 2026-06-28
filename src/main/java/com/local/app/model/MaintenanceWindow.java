package com.local.app.model;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;

/**
 * Embedded subdocument record tracking infrastructure governance, approval signatures,
 * time boundaries, and alarm suppression flags during structural maintenance pipelines.
 */
@Serdeable
public record MaintenanceWindow(
        Instant scheduledStart,    // Expected UTC timestamp to initiate maintenance operational mode
        Instant scheduledEnd,      // Expected UTC timestamp to restore standard operational mode
        String reason,             // Technical justification (e.g., "Bare-metal kernel upgrade", "AWS EBS expansion")
        String approvedBy,         // Security or infrastructure engineer sign-off token
        Boolean silenceAlerts,     // Flag directing monitoring systems to mute automated incident alerts
        Instant actualStart,       // Real UTC timestamp when the maintenance procedure actually began
        Instant actualEnd          // Real UTC timestamp when the maintenance execution was finalized
) {
    /**
     * Compact constructor enforcing structural validation constraints and sanitizing strings.
     */
    public MaintenanceWindow {
        reason = (reason == null) ? "Unspecified operational maintenance intervention" : reason.trim();
        approvedBy = (approvedBy == null) ? "SYSTEM-AUTOMATION" : approvedBy.trim().toUpperCase();
        silenceAlerts = (silenceAlerts == null) ? Boolean.TRUE : silenceAlerts;
    }
}
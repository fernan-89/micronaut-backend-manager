package com.local.app.model.enums;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Defines the operational compliance lifecycle states for a software license record.
 * Annotated with Serdeable for reflection-free JSON serialization bounds.
 */
@Serdeable
public enum LicenseStatus {
    /** License is active, valid, and available for hardware or user allocations. */
    ACTIVE,

    /** The license calendar expiration date has been passed. Structural block on new seats allocation. */
    EXPIRED,

    /** Administrative lock state (e.g., billing disputes, software audit verification pauses). */
    SUSPENDED,

    /** All purchased seats are fully consumed and allocated across infrastructure assets. */
    DEPLETED;

    /**
     * Safely converts inbound API string data values into the target LicenseStatus enum token.
     * Supports case-insensitive matches and auto-trims whitespace strings.
     *
     * @param value The raw input string payload data.
     * @return The matched LicenseStatus token instance.
     * @throws com.local.app.exception.InvalidMaintenanceStatusException If the parameter string does not align with any valid state node.
     */
    public static LicenseStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new com.local.app.exception.InvalidMaintenanceStatusException("Parsing failed: License status string cannot be null or empty.");
        }

        try {
            return LicenseStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.local.app.exception.InvalidMaintenanceStatusException("Parsing failed: '" + value + "' is not a valid recognized LicenseStatus.");
        }
    }
}
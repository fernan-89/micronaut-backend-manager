package com.local.app.model.enums; // <-- Corrigido para o namespace real do projeto

import io.micronaut.serde.annotation.Serdeable;

/**
 * Defines the strict, chronological lifecycle states for a computer maintenance ticket.
 * Annotated with Serdeable to allow seamless compilation and native JSON serialization.
 */
@Serdeable
public enum MaintenanceStatus {
    /**
     * Ticket successfully registered and awaiting initial technical triage.
     */
    OPENED,

    /**
     * Computer is currently on the technical bench undergoing hardware diagnostics.
     */
    IN_ANALYSIS,

    /**
     * Diagnosis completed, but progress is halted waiting for internal or external parts replacement.
     */
    AWAITING_PARTS,

    /**
     * Ticket paused because the equipment was dispatched to a specialized external vendor.
     */
    EXTERNAL_VENDOR,

    /**
     * Ticket paused because the equipment was dispatched to the manufacturer's official warranty service.
     */
    MANUFACTURER_WARRANTY,

    /**
     * Maintenance or external execution finished; awaiting pickup from vendor or notification to user.
     */
    AWAITING_PICKUP,

    /**
     * Equipment returned to bench for hardware stress tests, quality assurance, and OS stabilization.
     */
    TESTING,

    /**
     * Maintenance flow successfully validated, resolved, and equipment returned to the user.
     */
    COMPLETED,

    /**
     * Ticket administrative cancellation due to irreparable damage, structural write-off, or duplication.
     */
    CANCELED;

    /**
     * Utility method to safely convert incoming API strings into the target MaintenanceStatus enum.
     * Supports case-insensitive matching and trims blank spaces.
     *
     * @param value The raw string payload representation.
     * @return The matched MaintenanceStatus enum token instance.
     * @throws InvalidMaintenanceStatusException If the incoming payload value does not map to any valid maintenance state.
     */
    public static MaintenanceStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new com.local.app.exception.InvalidMaintenanceStatusException("Parsing failed: Maintenance status string cannot be null or empty.");
        }

        try {
            return MaintenanceStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.local.app.exception.InvalidMaintenanceStatusException("Parsing failed: '" + value + "' is not a valid recognized MaintenanceStatus.");
        }
    }
}
package com.local.app.model.enums;

import io.micronaut.serde.annotation.Serdeable;
import com.local.app.exception.InvalidMaintenanceStatusException; // Reutilizando a base de validação ou ajustando para a estrutura global

/**
 * Defines the architectural licensing distribution models for corporate software assets.
 * Annotated with Serdeable for native compilation and fast JSON serialization.
 */
@Serdeable
public enum LicenseType {
    /** Bound to a specific physical hardware machine or single active user profile seat. */
    PER_SEAT,

    /** Enterprise-wide contract allowing unrestricted deployment within the entire organization bounds. */
    ENTERPRISE,

    /** Modern Software-as-a-Service monthly or annual recurring cloud subscription vector. */
    SAAS_SUBSCRIPTION,

    /** Multiple Activation Key format allowing a predefined block lock of sequential activations. */
    VOLUME_MAK;

    /**
     * Safely converts inbound API string data values into the target LicenseType enum token.
     * Supports case-insensitive matches and auto-trims whitespace strings.
     *
     * @param value The raw input string payload data.
     * @return The matched LicenseType token instance.
     * @throws InvalidMaintenanceStatusException If the parameter string does not align with any valid type node.
     */
    public static LicenseType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new com.local.app.exception.InvalidMaintenanceStatusException("Parsing failed: License type string cannot be null or empty.");
        }

        try {
            return LicenseType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.local.app.exception.InvalidMaintenanceStatusException("Parsing failed: '" + value + "' is not a valid recognized LicenseType.");
        }
    }
}
package com.local.app.model.enums;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Defines the structural hosting architecture zones for the compute infrastructure.
 * Maps high-availability public clouds, bare-metal enterprise datacenters, and edge home-lab environments.
 */
@Serdeable
public enum InfrastructureProvider {
    /** Amazon Web Services elastic compute global public cloud infrastructure. */
    AWS_CLOUD,

    /** Microsoft Azure public enterprise cloud computing matrix. */
    AZURE_CLOUD,

    /** Google Cloud Platform data-driven hyper-scaler infrastructure network. */
    GCP_CLOUD,

    /** Private on-premises Tier-3/Tier-4 physical datacenter environments (Datacenter X, Datacenter Y). */
    PRIVATE_DATACENTER,

    /** Localized edge home-lab deployment ecosystem running behind custom proxy blocks or residential networks. */
    HOME_LAB;

    /**
     * Safely converts inbound API string tokens into the target InfrastructureProvider enum instance.
     * Supports absolute case-insensitive matches and auto-trims whitespace strings.
     *
     * @param value The raw input string payload data from the wire.
     * @return The matched InfrastructureProvider token instance.
     * @throws com.local.app.service.exception.BusinessException If the parameter string does not align with any valid hosting provider node.
     */
    public static InfrastructureProvider fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new com.local.app.service.exception.BusinessException("Infrastructure provider parsing failure: Input token cannot be null or blank.");
        }

        try {
            return InfrastructureProvider.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.local.app.service.exception.BusinessException("Infrastructure provider parsing failure: '" + value + "' is not a recognized provider location target.");
        }
    }
}
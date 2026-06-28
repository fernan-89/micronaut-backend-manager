package com.local.app.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;

/**
 * Inbound API Request payload holding structural validation parameters for purchasing
 * and cataloging a new corporate software license contract.
 */
@Serdeable
@Introspected
public record SoftwareLicenseRequest(
        @NotBlank(message = "Software name cannot be blank") String softwareName,
        @NotBlank(message = "Publisher name cannot be blank") String publisher,
        @NotBlank(message = "License activation key serial code cannot be blank") String licenseKey,
        @NotBlank(message = "License distribution type model cannot be blank") String licenseType,
        @NotNull(message = "Total seat block size capacity must be supplied")
        @Positive(message = "Total bought seats volume capacity must be greater than zero") Integer totalSeats,
        @NotNull(message = "Acquisition timeline instance timestamp cannot be null") Instant acquisitionDate,
        Instant expirationDate // Nullable parameter if dealing with a lifetime/perpetual enterprise license
) {}
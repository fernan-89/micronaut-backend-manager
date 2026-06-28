package com.local.app.dto.response;

import com.local.app.model.LicenseAllocation;
import com.local.app.model.enums.LicenseStatus;
import com.local.app.model.enums.LicenseType;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.List;

/**
 * Outbound unified JSON data snapshot representing a complete corporate software license profile.
 */
@Serdeable
public record SoftwareLicenseResponse(
        String licenseHash,
        String softwareName,
        String publisher,
        String licenseKey,
        LicenseType licenseType,
        LicenseStatus status,
        Integer totalSeats,
        Integer allocatedSeats,
        Instant acquisitionDate,
        Instant expirationDate,
        Instant createdAt,
        Instant updatedAt,
        List<LicenseAllocation> allocations
) {}
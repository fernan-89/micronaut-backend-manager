package com.local.app.model;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;

/**
 * Immutable embedded subdocument tracking the atomic assignment of a software license seat.
 * Must map explicitly against a hardware asset machine target or a corporate user entity identity.
 *
 * @param assetHash   The target workstation machine hardware asset tracking token. Nullable if user-bound.
 * @param userId      The employee identifier utilizing the platform seat access. Nullable if machine-bound.
 * @param allocatedAt The precise timestamp when this seat allocation was recorded.
 * @param allocatedBy The identity string of the supervisor or automated agent executing the assignment.
 */
@Serdeable
public record LicenseAllocation(
        String assetHash,
        String userId,
        Instant allocatedAt,
        String allocatedBy
) {
    /**
     * Compact constructor to enforce contextual business constraints and safety defaults.
     */
    public LicenseAllocation {
        // Enforce that an allocation must point to at least one entity destination type
        if ((assetHash == null || assetHash.trim().isEmpty()) && (userId == null || userId.trim().isEmpty())) {
            throw new IllegalArgumentException("License allocation target mismatch. Either assetHash or userId must be provided.");
        }

        if (allocatedBy == null || allocatedBy.trim().isEmpty()) {
            throw new IllegalArgumentException("Allocation operator origin identity (allocatedBy) cannot be null or empty.");
        }

        // Auto-assign real-time clock timestamp if not supplied during injection
        allocatedAt = (allocatedAt == null) ? Instant.now() : allocatedAt;
    }
}
package com.local.app.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

/**
 * Inbound API Request payload representing the allocation of a single license seat.
 * Contains optional targets to route the software access link to a physical machine or employee.
 */
@Serdeable
@Introspected
public record SoftwareLicenseAllocationRequest(
        String assetHash, // Target hardware identifier (Optional if userId is present)
        String userId     // Target employee identifier (Optional if assetHash is present)
) {}
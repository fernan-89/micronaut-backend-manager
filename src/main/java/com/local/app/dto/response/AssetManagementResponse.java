package com.local.app.dto.response;

import com.local.app.model.AssetSpecificationModel; // <-- Atualizado aqui
import com.local.app.model.MaintenanceComment;
import com.local.app.model.enums.AssetStatus;
import com.local.app.model.enums.EquipmentType;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object representing the unified outbound payload for asset profiles.
 * Formatted for seamless integration with client-side user interfaces and frontend dashboards.
 */
@Serdeable
@Introspected
public record AssetManagementResponse(
        String assetHash,
        String assetTag,
        String serialNumber,
        EquipmentType equipmentType,
        AssetStatus status,
        @Nullable String userId,
        BigDecimal purchaseValue,
        @Nullable BigDecimal saleValue,
        Instant acquisitionDate,
        @Nullable Instant decommissionDate,
        @Nullable Instant saleDate,
        AssetSpecificationModel specification, // <-- Tipo atualizado aqui
        List<MaintenanceComment> comments
) {}
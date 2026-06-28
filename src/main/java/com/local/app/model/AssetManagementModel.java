package com.local.app.model;

import com.local.app.model.enums.AssetStatus;
import com.local.app.model.enums.EquipmentType;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model representing a corporate hardware asset in the database.
 * This model is strictly mapped to the 'assets-management' collection in MongoDB.
 *
 * Note: Audit trails are deliberately excluded from this entity to maintain
 * a decoupled and performant centralized audit architecture in the 'logs-management' collection.
 */
@Serdeable
@Introspected
@MappedEntity("assets-management")
public record AssetManagementModel(
        @Id
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
) {
    /**
     * Compact canonical constructor annotated with @Creator to ensure Micronaut Data
     * correctly reconstructs the record from MongoDB documents and ensures list mutability safety.
     */
    @Creator
    public AssetManagementModel {
        comments = (comments == null) ? new ArrayList<>() : new ArrayList<>(comments);
        purchaseValue = (purchaseValue == null) ? BigDecimal.ZERO : purchaseValue;
        acquisitionDate = (acquisitionDate == null) ? Instant.now() : acquisitionDate;
    }
}
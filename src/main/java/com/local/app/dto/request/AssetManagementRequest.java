package com.local.app.dto.request;

import com.local.app.model.AssetSpecificationModel; // <-- Atualizado aqui
import com.local.app.model.enums.AssetStatus;
import com.local.app.model.enums.EquipmentType;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Data Transfer Object representing the incoming payload to create or update a hardware asset.
 * Contains rigorous Jakarta validation rules to enforce data compliance at the API edge.
 */
@Serdeable
@Introspected
public record AssetManagementRequest(
        @NotBlank(message = "Asset tag cannot be blank")
        String assetTag,

        @NotBlank(message = "Serial number cannot be blank")
        String serialNumber,

        @NotNull(message = "Equipment type is required")
        EquipmentType equipmentType,

        @Nullable
        AssetStatus status,

        @Nullable
        String userId,

        @NotNull(message = "Purchase value is required")
        @PositiveOrZero(message = "Purchase value must be greater than or equal to zero")
        BigDecimal purchaseValue,

        @Nullable
        @PositiveOrZero(message = "Sale value must be greater than or equal to zero")
        BigDecimal saleValue,

        @Nullable
        Instant acquisitionDate,

        @NotNull(message = "Asset specification details are required")
        @Valid
        AssetSpecificationModel specification // <-- Tipo atualizado aqui
) {}
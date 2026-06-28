package com.local.app.service;

import com.local.app.dto.request.AssetManagementRequest;
import com.local.app.dto.response.AssetManagementResponse;
import com.local.app.exception.AssetAlreadyExistsException;
import com.local.app.exception.AssetNotFoundException;
import com.local.app.model.AssetManagementModel;
import com.local.app.model.AuditLogModel;
import com.local.app.model.enums.AssetStatus;
import com.local.app.repository.AssetManagementRepository;
import com.local.app.repository.AuditLogRepository;
import com.local.app.service.exception.BusinessException;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class that handles complex business logic for corporate hardware asset management.
 * Integrated with a centralized audit logging system to guarantee high-fidelity traceability
 * of all state transitions and physical characteristic modifications.
 */
@Singleton
public class AssetManagementService {

    private final AssetManagementRepository repository;
    private final AuditLogRepository logRepository;

    /**
     * Constructs the service with necessary repositories for data persistence and audit logging.
     *
     * @param repository    The primary repository for asset entity operations.
     * @param logRepository The dedicated repository for centralized audit trail storage.
     */
    public AssetManagementService(AssetManagementRepository repository, AuditLogRepository logRepository) {
        this.repository = repository;
        this.logRepository = logRepository;
    }

    /**
     * Persists a new hardware asset record and initializes the immutable audit trail.
     *
     * @param request Data containing the new asset specifications.
     * @param executor Identifier of the person/system creating the asset.
     * @return The created asset DTO.
     */
    public AssetManagementResponse createAsset(AssetManagementRequest request, String executor) {
        validateUniqueConstraints(request, null);

        String assetHash = generateAssetHash();
        AssetStatus initialStatus = request.status() != null ? request.status() : AssetStatus.AVAILABLE;
        Instant now = Instant.now();

        AssetManagementModel model = new AssetManagementModel(
                assetHash, request.assetTag(), request.serialNumber(), request.equipmentType(),
                initialStatus, request.userId(), request.purchaseValue(), request.saleValue(),
                request.acquisitionDate() != null ? request.acquisitionDate() : now,
                null, null, request.specification(), new ArrayList<>()
        );

        // Records the initial 'create' event in the central audit collection
        logRepository.save(new AuditLogModel(
                generateLogHash(), assetHash, "Asset registered in inventory", Collections.emptyMap(), LocalDateTime.now(), executor
        ));

        return mapToResponse(repository.save(model));
    }

    /**
     * Retrieves an asset by its unique corporate business hash.
     */
    public AssetManagementResponse findByHash(String assetHash) {
        return repository.findByAssetHash(assetHash)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with hash: " + assetHash));
    }

    /**
     * Retrieves an asset by its exact corporate physical tag.
     *
     * @param assetTag The structural tag attached to the physical unit.
     * @return The found asset converted to a response DTO.
     */
    public AssetManagementResponse findByAssetTag(String assetTag) {
        return repository.findByAssetTag(assetTag)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with tag: " + assetTag));
    }

    /**
     * Retrieves an asset by its hardware manufacturer serial number.
     *
     * @param serialNumber The unique identification stamped on the machine component.
     * @return The found asset converted to a response DTO.
     */
    public AssetManagementResponse findBySerialNumber(String serialNumber) {
        return repository.findBySerialNumber(serialNumber)
                .map(this::mapToResponse)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with serial number: " + serialNumber));
    }

    /**
     * Retrieves all hardware assets mapped inside the persistence database collection.
     *
     * @return A list containing all asset records transformed into response payloads.
     */
    public List<AssetManagementResponse> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing hardware asset and captures granular "De/Para" field modifications for the audit trail.
     *
     * @param assetHash Identifier of the asset to be updated.
     * @param request   Data payload with new values.
     * @param executor  Identifier of the person performing the update.
     */
    public AssetManagementResponse updateAsset(String assetHash, AssetManagementRequest request, String executor) {
        return repository.findByAssetHash(assetHash).map(existing -> {
            validateUniqueConstraints(request, existing.assetHash());

            // Calculates field-level deltas for the audit log
            Map<String, AuditLogModel.ChangeDetail> changes = calculateChanges(existing, request);

            if (!changes.isEmpty()) {
                logRepository.save(new AuditLogModel(
                        generateLogHash(), assetHash, "Update asset specifications", changes, LocalDateTime.now(), executor
                ));
            }

            AssetStatus updatedStatus = request.status() != null ? request.status() : existing.status();

            // Handling specific lifecycle timestamps based on status transitions
            Instant decommissionDate = existing.decommissionDate();
            Instant saleDate = existing.saleDate();

            if (updatedStatus == AssetStatus.DECOMMISSIONED && decommissionDate == null) {
                decommissionDate = Instant.now();
            } else if (updatedStatus == AssetStatus.SOLD && saleDate == null) {
                saleDate = Instant.now();
            }

            AssetManagementModel updated = new AssetManagementModel(
                    existing.assetHash(),
                    request.assetTag(),
                    request.serialNumber(),
                    request.equipmentType(),
                    updatedStatus,
                    request.userId(),
                    request.purchaseValue(),
                    request.saleValue() != null ? request.saleValue() : existing.saleValue(),
                    existing.acquisitionDate(),
                    decommissionDate,
                    saleDate,
                    request.specification(),
                    existing.comments()
            );

            return mapToResponse(repository.update(updated));
        }).orElseThrow(() -> new AssetNotFoundException("Asset not found with hash: " + assetHash));
    }

    /**
     * Permanently deletes the asset record from the system.
     */
    public void deleteAsset(String assetHash) {
        if (repository.findByAssetHash(assetHash).isEmpty()) {
            throw new AssetNotFoundException("Asset not found with hash: " + assetHash);
        }
        repository.deleteByAssetHash(assetHash);
    }

    /**
     * Helper method to compare current entity state with incoming request to detect specific modifications.
     */
    private Map<String, AuditLogModel.ChangeDetail> calculateChanges(AssetManagementModel existing, AssetManagementRequest request) {
        Map<String, AuditLogModel.ChangeDetail> changes = new HashMap<>();

        if (!request.assetTag().equals(existing.assetTag()))
            changes.put("assetTag", new AuditLogModel.ChangeDetail(existing.assetTag(), request.assetTag()));

        if (!request.serialNumber().equals(existing.serialNumber()))
            changes.put("serialNumber", new AuditLogModel.ChangeDetail(existing.serialNumber(), request.serialNumber()));

        if (request.status() != null && request.status() != existing.status())
            changes.put("status", new AuditLogModel.ChangeDetail(existing.status().name(), request.status().name()));

        if (request.equipmentType() != null && request.equipmentType() != existing.equipmentType())
            changes.put("equipmentType", new AuditLogModel.ChangeDetail(existing.equipmentType().name(), request.equipmentType().name()));

        if (!Objects.equals(request.userId(), existing.userId()))
            changes.put("userId", new AuditLogModel.ChangeDetail(String.valueOf(existing.userId()), String.valueOf(request.userId())));

        return changes;
    }

    /**
     * Validates that unique constraints (assetTag and serialNumber) are not violated during creation or update.
     */
    private void validateUniqueConstraints(AssetManagementRequest request, String currentHash) {
        repository.findByAssetTag(request.assetTag()).ifPresent(asset -> {
            if (currentHash == null || !asset.assetHash().equals(currentHash)) {
                throw new AssetAlreadyExistsException("An asset with the tag " + request.assetTag() + " already exists.");
            }
        });

        repository.findBySerialNumber(request.serialNumber()).ifPresent(asset -> {
            if (currentHash == null || !asset.assetHash().equals(currentHash)) {
                throw new AssetAlreadyExistsException("An asset with the serial number " + request.serialNumber() + " already exists.");
            }
        });
    }

    private AssetManagementResponse mapToResponse(AssetManagementModel model) {
        return new AssetManagementResponse(
                model.assetHash(), model.assetTag(), model.serialNumber(), model.equipmentType(),
                model.status(), model.userId(), model.purchaseValue(), model.saleValue(),
                model.acquisitionDate(), model.decommissionDate(), model.saleDate(),
                model.specification(), model.comments()
        );
    }

    private String generateAssetHash() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return "AST-" + datePart + "-" + randomPart.substring(0, 4) + "-" + randomPart.substring(4, 8);
    }

    private String generateLogHash() {
        return "LOG-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
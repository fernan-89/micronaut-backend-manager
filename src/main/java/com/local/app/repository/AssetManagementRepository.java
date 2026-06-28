package com.local.app.repository;

import com.local.app.model.AssetManagementModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
import java.util.Optional;

/**
 * Data repository interface acting as the abstraction layer over MongoDB.
 * Leverages Micronaut Data to handle automated query compilation for the 'assets-management' collection.
 */
@MongoRepository
public interface AssetManagementRepository extends CrudRepository<AssetManagementModel, String> {

    /**
     * Queries the database to locate an asset matching the precise business token hash.
     *
     * @param assetHash The unique business token assigned during creation.
     * @return An Optional container which may or may not hold the found {@link AssetManagementModel}.
     */
    Optional<AssetManagementModel> findByAssetHash(@NonNull String assetHash);

    /**
     * Queries the database to locate an asset matching the assigned physical corporate tag.
     * Used primary to avoid unique identification duplicates.
     *
     * @param assetTag The structural tag attached to the physical unit.
     * @return An Optional container holding the asset if found.
     */
    Optional<AssetManagementModel> findByAssetTag(@NonNull String assetTag);

    /**
     * Queries the database to locate an asset matching the manufacturer serial number.
     * Used to prevent physical resource shadowing.
     *
     * @param serialNumber The unique identifier stamped on the machine component.
     * @return An Optional container holding the asset if found.
     */
    Optional<AssetManagementModel> findBySerialNumber(@NonNull String serialNumber);

    /**
     * Issues a hard deletion contract to eliminate the record linked to the business hash.
     *
     * @param assetHash The identifier of the document to be purged from persistence.
     */
    void deleteByAssetHash(@NonNull String assetHash);
}
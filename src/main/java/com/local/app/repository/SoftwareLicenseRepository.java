package com.local.app.repository;

import com.local.app.model.SoftwareLicenseModel;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Data Access Repository responsible for executing operations on the 'software_licenses' MongoDB collection.
 */
@MongoRepository
public interface SoftwareLicenseRepository extends CrudRepository<SoftwareLicenseModel, String> {

    /**
     * Finds a specific software license contract using its generated business hash identifier.
     *
     * @param licenseHash The unique business token string.
     * @return An optional containing the license document profile if found.
     */
    Optional<SoftwareLicenseModel> findByLicenseHash(String licenseHash);

    /**
     * Checks if a specific software license hash already exists in the persistent storage.
     *
     * @param licenseHash The unique business token string.
     * @return True if the document exists, false otherwise.
     */
    boolean existsByLicenseHash(String licenseHash);
}
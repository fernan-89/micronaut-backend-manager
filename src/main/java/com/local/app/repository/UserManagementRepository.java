package com.local.app.repository;

import com.local.app.model.UserManagementModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for UserManagementModel.
 * Provides custom query methods for the 'users-management' collection in MongoDB.
 * Uses CrudRepository to inherit standard persistence operations like save and update.
 */
@MongoRepository
public interface UserManagementRepository extends CrudRepository<UserManagementModel, String> {

    /**
     * Finds a user document by their unique user hash identifier.
     *
     * @param userHash The unique string hash assigned to the user.
     * @return An {@link Optional} containing the user model if found, or empty.
     */
    Optional<UserManagementModel> findByUserHash(@NonNull String userHash);

    /**
     * Finds a user document by their corporate email address.
     *
     * @param companyEmail The corporate email address.
     * @return An {@link Optional} containing the user model if found, or empty.
     */
    Optional<UserManagementModel> findByCompanyEmail(@NonNull String companyEmail);

    /**
     * Finds a user document by their personal email address.
     *
     * @param personalEmail The personal email address.
     * @return An {@link Optional} containing the user model if found, or empty.
     */
    Optional<UserManagementModel> findByPersonalEmail(@NonNull String personalEmail);

    /**
     * Finds a user document by their system login identifier.
     *
     * @param login The unique system login identifier.
     * @return An {@link Optional} containing the user model if found, or empty.
     */
    Optional<UserManagementModel> findByLogin(@NonNull String login);

    /**
     * Finds a user document by their corporate phone number.
     *
     * @param companyPhone The corporate phone number.
     * @return An {@link Optional} containing the user model if found, or empty.
     */
    Optional<UserManagementModel> findByCompanyPhone(@NonNull String companyPhone);

    /**
     * Finds a user document by their personal phone number.
     *
     * @param personalPhone The personal phone number.
     * @return An {@link Optional} containing the user model if found, or empty.
     */
    Optional<UserManagementModel> findByPersonalPhone(@NonNull String personalPhone);

    /**
     * Finds a user document by their internal registration code.
     *
     * @param registration The internal registration code.
     * @return An {@link Optional} containing the user model if found, or empty.
     */
    Optional<UserManagementModel> findByRegistration(@NonNull String registration);

    /**
     * Deletes a user document from the MongoDB collection based on their unique user hash.
     *
     * @param userHash The unique string hash of the user to be deleted.
     */
    void deleteByUserHash(@NonNull String userHash);
}
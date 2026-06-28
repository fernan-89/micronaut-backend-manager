package com.local.app.repository;

import com.local.app.model.MaintenanceTicketModel;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Primary Data Access Repository for executing operations on the 'maintenance_tickets' MongoDB collection.
 */
@MongoRepository
public interface MaintenanceTicketRepository extends CrudRepository<MaintenanceTicketModel, String> {

    /**
     * Finds a unique maintenance ticket by its generated business hash identifier.
     *
     * @param ticketHash The business hash token.
     * @return An optional tracking the state profile.
     */
    Optional<MaintenanceTicketModel> findByTicketHash(String ticketHash);

    /**
     * Query all tickets opened or assigned against a specific corporate hardware asset hash.
     *
     * @param assetHash Unique physical machine reference tracking string.
     * @return List of matching maintenance historical graphs.
     */
    List<MaintenanceTicketModel> findByAssetHash(String assetHash);
}
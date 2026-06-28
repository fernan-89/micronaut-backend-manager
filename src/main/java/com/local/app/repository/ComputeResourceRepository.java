package com.local.app.repository;

import com.local.app.model.ComputeResourceModel;
import com.local.app.model.enums.InfrastructureProvider;
import com.local.app.model.enums.ResourceStatus;
import com.local.app.model.enums.ResourceType; // <- Garanta que este import está aqui
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Repository responsible for executing operations on the 'compute_resources' MongoDB collection.
 */
@MongoRepository
public interface ComputeResourceRepository extends CrudRepository<ComputeResourceModel, String> {

    /**
     * Finds a specific compute resource by its unique infrastructure business hash token.
     */
    Optional<ComputeResourceModel> findByResourceHash(String resourceHash);

    /**
     * Finds all compute resources flagged with a specific operational status.
     */
    List<ComputeResourceModel> findByStatus(ResourceStatus status);

    /**
     * Finds all resources filtering by hypervisor type layer and infrastructure deployment locator.
     * Fixed: Added the missing ResourceType parameter to align with the query method name.
     *
     * @param resourceType The architecture type layer (e.g., CONTAINER, VIRTUAL_MACHINE).
     * @param provider     The target hosting matrix (e.g., AWS_CLOUD, HOME_LAB).
     * @return A list of filtered compute resources.
     */
    List<ComputeResourceModel> findByResourceTypeAndProvider(ResourceType resourceType, InfrastructureProvider provider);
}
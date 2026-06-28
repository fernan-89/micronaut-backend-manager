package com.local.app.repository;

import com.local.app.model.AuditLogModel;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

/**
 * Repository interface for AuditLogModel persistence in MongoDB.
 * Provides standard CRUD operations to manage audit trails in the 'logs-management' collection.
 */
@MongoRepository
public interface AuditLogRepository extends CrudRepository<AuditLogModel, String> {

    /**
     * Finds all log entries associated with a specific entity hash.
     * Useful for retrieving the audit history of a specific user or item.
     *
     * @param entityHash The hash of the entity to filter logs by.
     * @return A list of audit logs related to the entity.
     */
    java.util.List<AuditLogModel> findByEntityHash(String entityHash);
}
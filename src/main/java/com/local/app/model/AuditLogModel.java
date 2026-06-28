package com.local.app.model;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing an audit log entry in the database.
 * This model is mapped to the 'logs-management' collection to provide
 * centralized traceability across the system.
 */
@Serdeable
@Introspected
@MappedEntity("logs-management")
public record AuditLogModel(
        /**
         * Unique identifier for the audit log entry.
         */
        @Id
        String logHash,

        /**
         * The reference hash of the entity being audited (e.g., userHash).
         */
        String entityHash,

        /**
         * The description of the action performed.
         */
        String action,

        /**
         * A map containing the fields changed, with 'from' and 'to' values.
         */
        Map<String, ChangeDetail> changes,

        /**
         * The timestamp of when the action occurred.
         */
        LocalDateTime timestamp,

        /**
         * The identifier of the user or system component that performed the action.
         */
        String executedBy
) {
    @Creator
    public AuditLogModel {}

    /**
     * Represents the transition of a field value.
     */
    @Serdeable
    public record ChangeDetail(
            String from,
            String to
    ) {}
}
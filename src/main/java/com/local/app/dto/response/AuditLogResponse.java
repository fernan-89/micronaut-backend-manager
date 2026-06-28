package com.local.app.dto.response;

import com.local.app.model.AuditLogModel.ChangeDetail;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object representing an audit log entry in the API response.
 */
@Serdeable
public record AuditLogResponse(
        String logHash,
        String entityHash,
        String action,
        Map<String, ChangeDetail> changes,
        LocalDateTime timestamp,
        String executedBy
) {}
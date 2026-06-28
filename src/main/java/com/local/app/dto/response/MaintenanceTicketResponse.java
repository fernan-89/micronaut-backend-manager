package com.local.app.dto.response;

import com.local.app.model.MaintenanceComment;
import com.local.app.model.enums.MaintenanceStatus;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.List;

/**
 * Outbound complete structural JSON snapshot representing a corporate maintenance ticket instance.
 */
@Serdeable
public record MaintenanceTicketResponse(
        String ticketHash,
        String assetHash,
        String requesterId,
        String assignedTechnicianId,
        String issueDescription,
        MaintenanceStatus status,
        Instant createdAt,
        Instant updatedAt,
        List<MaintenanceComment> comments
) {}
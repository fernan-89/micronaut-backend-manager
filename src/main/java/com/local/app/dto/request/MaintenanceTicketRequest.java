package com.local.app.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * API Request payload bound to input validations for launching a new hardware maintenance ticket.
 */
@Serdeable
@Introspected
public record MaintenanceTicketRequest(
        @NotBlank(message = "Target asset hash reference cannot be blank") String assetHash,
        @NotBlank(message = "Requester identifier cannot be blank") String requesterId,
        String assignedTechnicianId,
        @NotBlank(message = "Detailed issue description cannot be blank") String issueDescription
) {}
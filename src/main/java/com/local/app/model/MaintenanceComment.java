package com.local.app.model; // <-- Corrigido para o pacote real do seu projeto

import com.local.app.model.enums.MaintenanceStatus; // <-- Alinhado para a sua estrutura
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;

/**
 * Represents an immutable user or technician comment within a maintenance ticket.
 * Captures historical timeline discourse bound to the system state at that specific moment.
 *
 * @param author     The name or business identifier of the person who wrote the comment.
 * @param message    The actual discursive content/text of the update.
 * @param statusWhen The ticket status at the precise moment this comment was appended.
 * @param timestamp  The absolute instant when the comment was recorded.
 */
@Serdeable
public record MaintenanceComment(
        String author,
        String message,
        MaintenanceStatus statusWhen,
        Instant timestamp
) {
    /**
     * Compact constructor to enforce strict data integrity and provide defaults.
     */
    public MaintenanceComment {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment author cannot be null or empty.");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment message cannot be null or empty.");
        }
        if (statusWhen == null) {
            throw new IllegalArgumentException("Comment must be bound to a valid MaintenanceStatus.");
        }
        // Defaults to current instant if not provided for real-time logging
        timestamp = (timestamp == null) ? Instant.now() : timestamp;
    }
}
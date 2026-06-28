package com.local.app.dto.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 * Inbound payload representing an atomic discursive addition inside an active maintenance pipeline.
 */
@Serdeable
@Introspected
public record AddCommentRequest(
        @NotBlank(message = "Comment author cannot be blank") String author,
        @NotBlank(message = "Comment message content text cannot be blank") String message
) {}
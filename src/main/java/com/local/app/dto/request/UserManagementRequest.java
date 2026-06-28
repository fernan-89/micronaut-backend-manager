package com.local.app.dto.request;

import com.local.app.model.enums.UserManagementTypes;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Data Transfer Object representing the request payload for creating or updating a user.
 * All fields are validated using Jakarta Validation constraints to ensure
 * data integrity before reaching the business logic layer.
 */
@Serdeable
public record UserManagementRequest(
        /**
         * The full name of the user. This field must not be blank or null.
         */
        @NotBlank @Nullable
        String name,

        /**
         * The unique login identifier defined for the user. This field must not be blank or null.
         */
        @NotBlank @Nullable
        String login,

        /**
         * The corporate phone number assigned to the user for business communications.
         * This field must not be blank or null.
         */
        @NotBlank @Nullable
        String companyPhone,

        /**
         * The personal phone number of the user for direct contact.
         * This field must not be blank or null.
         */
        @NotBlank @Nullable
        String personalPhone,

        /**
         * The primary corporate email address used by the user.
         * Must be a well-formed email address and must not be blank or null.
         */
        @Email @NotBlank @Nullable
        String companyEmail,

        /**
         * The alternative or personal email address of the user.
         * Must be a well-formed email address and must not be blank or null.
         */
        @Email @NotBlank @Nullable
        String personalEmail,

        /**
         * The date of birth of the user, expected in ISO-8601 format (YYYY-MM-DD).
         * This field is mandatory and must not be null.
         */
        @NotNull @Nullable
        LocalDate birthDate,

        /**
         * The specific department or sector where the user is intended to be allocated.
         * This field must not be blank or null.
         */
        @NotBlank @Nullable
        String department,

        /**
         * The name of the company or subsidiary to which the user belongs.
         * This field must not be blank or null.
         */
        @NotBlank @Nullable
        String company,

        /**
         * The operational type or category of the user, defining their primary work environment.
         * This field is mandatory and must not be null.
         */
        @NotNull @Nullable
        UserManagementTypes type,

        /**
         * Indicates the current status of the user account.
         * Nullable to allow partial updates without affecting the account status.
         */
        @Nullable
        Boolean active
) {}
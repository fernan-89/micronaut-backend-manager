package com.local.app.dto.response;

import com.local.app.model.enums.UserManagementTypes;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing the response payload for user management operations.
 * This record encapsulates the user details returned by the API.
 */
@Serdeable
public record UserManagementResponse(
        /**
         * The unique string hash identifier assigned to the user upon creation.
         */
        String userHash,

        /**
         * The full name of the user.
         */
        String name,

        /**
         * The unique login identifier defined for the user.
         */
        String login,

        /**
         * The corporate phone number assigned to the user for business communications.
         */
        String companyPhone,

        /**
         * The personal phone number of the user for direct contact.
         */
        String personalPhone,

        /**
         * The primary corporate email address used by the user.
         */
        String companyEmail,

        /**
         * The alternative or personal email address of the user.
         */
        String personalEmail,

        /**
         * The date of birth of the user, represented in ISO-8601 format (YYYY-MM-DD).
         */
        LocalDate birthDate,

        /**
         * The internal registration or employee identification code.
         */
        String registration,

        /**
         * The specific department or sector where the user is currently allocated.
         */
        String department,

        /**
         * The company to which the user belongs.
         */
        String company,

        /**
         * A boolean flag indicating whether the user account is currently active.
         */
        boolean active,

        /**
         * The operational type or category of the user, defining their primary work environment.
         */
        UserManagementTypes type,

        /**
         * The timestamp indicating when the user record was created.
         */
        LocalDateTime createdAt,

        /**
         * The timestamp indicating the last modification performed on the user record.
         */
        LocalDateTime updatedAt
) {}
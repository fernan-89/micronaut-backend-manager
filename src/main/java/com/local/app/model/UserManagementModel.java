package com.local.app.model;

import com.local.app.model.enums.UserManagementTypes;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain model representing a user entity in the database.
 * This model is strictly mapped to the 'users-management' collection in MongoDB.
 */
@Serdeable
@Introspected
@MappedEntity("users-management")
public record UserManagementModel(
        /**
         * The unique hash identifier for the user. Used as the primary key in MongoDB.
         */
        @Id
        String userHash,

        /**
         * The full name of the user.
         */
        String name,

        /**
         * The system-generated login identifier based on user initials and surname.
         */
        String login,

        /**
         * The corporate phone number used for business communication.
         */
        String companyPhone,

        /**
         * The personal phone number for direct contact.
         */
        String personalPhone,

        /**
         * The corporate email address. Indexed for uniqueness to prevent duplicates.
         */
        String companyEmail,

        /**
         * The personal email address for alternative communication.
         */
        String personalEmail,

        /**
         * The date of birth of the user in YYYY-MM-DD format.
         */
        LocalDate birthDate,

        /**
         * The internal registration code. Indexed for uniqueness and identification.
         */
        String registration,

        /**
         * The department where the user is currently allocated.
         */
        String department,

        /**
         * The company to which the user belongs.
         */
        String company,

        /**
         * Indicates if the user account is currently active in the system.
         */
        boolean active,

        /**
         * The operational type or category of the user, defining access levels.
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
) {
        /**
         * Compact canonical constructor annotated with @Creator to ensure Micronaut Data
         * correctly reconstructs the record from MongoDB documents.
         */
        @Creator
        public UserManagementModel {}
}
package com.local.app.model.enums;

/**
 * Enumeration representing the different operational categories of users within the system.
 * This is used to classify users based on their primary work environment or role,
 * which can be utilized to dictate specific access levels, policies, or business rules.
 */
public enum UserManagementTypes {

    /**
     * Represents a user who primarily operates outside the main corporate facilities,
     * such as field workers, traveling personnel, or work-from-home employees.
     */
    REMOTE("Remote"),

    /**
     * Represents a user who primarily operates within the standard corporate office environment.
     */
    LOCAL("Local"),

    /**
     * Represents a user who primarily operates within the industrial, warehouse, or production facility.
     */
    FACTORY("Factory"),

    /**
     * Represents an automated system, application, or integration account rather than a human user.
     * Used for machine-to-machine communication, automated scripts, and background processes.
     */
    SERVICE("Service"),

    /**
     * Represents a human user with limited-time access, such as a contractor, intern, consultant,
     * or third-party vendor. These accounts typically require stricter access controls or expiration dates.
     */
    TEMPORARY("Temporary");

    private final String description;

    /**
     * Constructs a new UserManagementTypes enum instance with the specified description.
     *
     * @param description The human-readable string describing the operational environment or role of the user.
     */
    UserManagementTypes(String description) {
        this.description = description;
    }

    /**
     * Retrieves the human-readable description of the user type.
     * This is particularly useful for logging, reporting, or displaying the formatted type
     * on a client-side user interface.
     *
     * @return A string containing the descriptive name of the user type.
     */
    public String getDescription() {
        return description;
    }
}
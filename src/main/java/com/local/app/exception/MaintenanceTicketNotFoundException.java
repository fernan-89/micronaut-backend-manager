package com.local.app.exception;

/**
 * Domain exception thrown when a requested maintenance ticket cannot be located
 * within the persistent infrastructure using its unique business key hash.
 */
public class MaintenanceTicketNotFoundException extends RuntimeException {

    /**
     * Constructs the exception with a specific detailed message.
     *
     * @param message The technical or functional reason for the error.
     */
    public MaintenanceTicketNotFoundException(String message) {
        super(message);
    }
}
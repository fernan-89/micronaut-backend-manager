package com.local.app.exception;

/**
 * Exception thrown when an attempt is made to register or update a user
 * with a corporate email address that is already registered in the system.
 */
public class CompanyEmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new CompanyEmailAlreadyExistsException with a standard message.
     * @param message The detailed error message indicating the conflicting email.
     */
    public CompanyEmailAlreadyExistsException(String message) {
        super(message);
    }
}
package com.local.app.exception;

/**
 * Exception thrown when an attempt is made to register or update a user
 * with a corporate phone number that is already assigned to another user.
 */
public class CompanyPhoneAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new CompanyPhoneAlreadyExistsException with a standard message.
     * @param message The detailed error message indicating the conflicting corporate phone.
     */
    public CompanyPhoneAlreadyExistsException(String message) {
        super(message);
    }
}
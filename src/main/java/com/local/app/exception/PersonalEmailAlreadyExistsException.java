package com.local.app.exception;

/**
 * Exception thrown when an attempt is made to register or update a user
 * with a personal email address that is already registered to another user.
 */
public class PersonalEmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new PersonalEmailAlreadyExistsException with a standard message.
     * @param message The detailed error message indicating the conflicting personal email.
     */
    public PersonalEmailAlreadyExistsException(String message) {
        super(message);
    }
}
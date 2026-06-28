package com.local.app.exception;

/**
 * Custom exception thrown when attempting to create a user that already exists in the system.
 * This is used to signal a conflict with existing unique constraints such as email or registration.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     * @param message The detail message explaining the reason for the conflict
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
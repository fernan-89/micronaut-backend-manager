package com.local.app.exception;

/**
 * Custom exception thrown when a user cannot be found in the system.
 * This is used to signal that a requested user entity does not exist for a given identifier.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     * @param message The detail message explaining the reason why the user was not found
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
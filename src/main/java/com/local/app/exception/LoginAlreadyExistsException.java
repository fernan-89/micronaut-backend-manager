package com.local.app.exception;

/**
 * Exception thrown when an attempt is made to register or update a user
 * with a system login that is already in use by another active account.
 */
public class LoginAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new LoginAlreadyExistsException with a standard message.
     * @param message The detailed error message indicating the conflicting login.
     */
    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
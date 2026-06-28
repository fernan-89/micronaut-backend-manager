package com.local.app.exception;

/**
 * Exception thrown when an attempt is made to register or update a user
 * with a personal phone number that is already recorded in the system.
 */
public class PersonalPhoneAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new PersonalPhoneAlreadyExistsException with a standard message.
     * @param message The detailed error message indicating the conflicting personal phone.
     */
    public PersonalPhoneAlreadyExistsException(String message) {
        super(message);
    }
}
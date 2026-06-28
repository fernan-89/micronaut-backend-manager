package com.local.app.service.exception; // <-- Define o pacote esperado pelo Service

/**
 * Custom runtime exception thrown when a core business rule, validation threshold,
 * or state machine transition contract is violated within the service layer.
 */
public class BusinessException extends RuntimeException {

    /**
     * Constructs a new BusinessException with the specified detail message.
     *
     * @param message The detailed error message indicating the business rule violation.
     */
    public BusinessException(String message) {
        super(message);
    }
}
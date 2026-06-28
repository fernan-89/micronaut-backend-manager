package com.local.app.exception;

import com.local.app.service.exception.BusinessException;

/**
 * Business rule exception thrown when a maintenance ticket attempts to execute
 * an illegal structural state change (e.g., modifying a canceled or completed ticket).
 */
public class MaintenanceStatusTransitionException extends BusinessException {

    /**
     * Constructs the exception with a specific business constraint message.
     *
     * @param message The explanation of the violated lifecycle constraint.
     */
    public MaintenanceStatusTransitionException(String message) {
        super(message);
    }
}
package com.local.app.exception;

import com.local.app.service.exception.BusinessException;

/**
 * Domain business exception thrown when an allocation assignment pipeline fails
 * because the target software license has completely consumed its purchased seats quota.
 */
public class LicenseDepletedException extends BusinessException {

    /**
     * Constructs the exception with a clear contextual compliance failure message.
     *
     * @param message Detailed reason describing the seat depletion constraint state.
     */
    public LicenseDepletedException(String message) {
        super(message);
    }
}
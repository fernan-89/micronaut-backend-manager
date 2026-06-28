package com.local.app.exception;

import com.local.app.service.exception.BusinessException;

/**
 * Validation exception thrown when an inbound API string payload fails to map
 * against any known recognized internal MaintenanceStatus enum tokens.
 */
public class InvalidMaintenanceStatusException extends BusinessException {

    /**
     * Constructs the exception highlighting the unrecognized value.
     *
     * @param message Detailed parsing failure message bounds.
     */
    public InvalidMaintenanceStatusException(String message) {
        super(message);
    }
}
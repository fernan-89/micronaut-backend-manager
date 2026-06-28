package com.local.app.exception; // <-- Corrigido para o pacote real do projeto

/**
 * Custom exception thrown when attempting to create or update an asset
 * with unique identifiers that already exist in the system.
 * This is used to signal a conflict with unique constraints such as assetTag or serialNumber.
 */
public class AssetAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new AssetAlreadyExistsException with the specified detail message.
     * @param message The detail message explaining the reason for the conflict.
     */
    public AssetAlreadyExistsException(String message) {
        super(message);
    }
}
package com.local.app.exception; // <-- Corrigido para o pacote real do projeto

/**
 * Custom exception thrown when a hardware asset cannot be found in the system.
 * This is used to signal that a requested asset entity does not exist for a given identifier (hash).
 */
public class AssetNotFoundException extends RuntimeException {

    /**
     * Constructs a new AssetNotFoundException with the specified detail message.
     * @param message The detail message explaining the reason why the asset was not found.
     */
    public AssetNotFoundException(String message) {
        super(message);
    }
}
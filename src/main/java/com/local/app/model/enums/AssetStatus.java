package com.local.app.model.enums;

/**
 * Enumeration representing the granular administrative lifecycle states of a corporate hardware asset.
 */
public enum AssetStatus {
    /**
     * The asset is sitting in inventory and is completely ready to be assigned to an employee.
     */
    AVAILABLE,

    /**
     * The asset has been actively provisioned and is currently in custody of an employee.
     */
    ALLOCATED,

    /**
     * The device is undergoing physical repairs, hardware upgrades, or configuration resets.
     */
    UNDER_MAINTENANCE,

    /**
     * The hardware has reached its logical end-of-life, is broken, or was retired from active service.
     */
    DECOMMISSIONED,

    /**
     * The asset was legally sold or liquidated to a third party or employee.
     */
    SOLD
}
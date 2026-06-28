package com.local.app.model.enums;

/**
 * Enumeration detailing the structural taxonomy classifications of corporate hardware items.
 */
public enum EquipmentType {
    /**
     * Portable employee workstations (e.g., MacBooks, ThinkPads, Dell Latitudes).
     */
    LAPTOP,

    /**
     * Fixed desk workstations or mini PC hosts.
     */
    DESKTOP,

    /**
     * Corporate mobile cellular nodes or tablets.
     */
    MOBILE,

    /**
     * Network infrastructure units (e.g., switches, physical routers, firewalls, Wi-Fi APs).
     */
    NETWORKING,

    /**
     * On-premise infrastructure hosts or rack-mounted computational blocks.
     */
    SERVER
}
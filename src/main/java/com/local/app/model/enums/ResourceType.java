package com.local.app.model.enums;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Defines the structural compute virtualization level and execution architecture layer.
 */
@Serdeable
public enum ResourceType {
    /** Bare-metal physical enterprise hardware server rack unit hosting hypervisors or cluster engines. */
    BARE_METAL_HOST,

    /** Traditional hypervisor-managed abstraction layer instance (e.g., AWS EC2, Proxmox VM, VMware ESXi). */
    VIRTUAL_MACHINE,

    /** Lightweight OS-level virtualized isolate processing node (e.g., Docker Container, Podman Core, K8s Pod). */
    CONTAINER;

    /**
     * Safely converts inbound API string tokens into the target ResourceType enum instance.
     * Supports absolute case-insensitive matches and auto-trims whitespace strings.
     *
     * @param value The raw input string payload data from the wire.
     * @return The matched ResourceType token instance.
     * @throws com.local.app.service.exception.BusinessException If the parameter string does not align with any valid architecture node.
     */
    public static ResourceType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new com.local.app.service.exception.BusinessException("Resource type parsing failure: Input token cannot be null or blank.");
        }

        try {
            return ResourceType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.local.app.service.exception.BusinessException("Resource type parsing failure: '" + value + "' is not a recognized compute execution layer.");
        }
    }
}
package com.local.app.model.enums;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Defines the operational heartbeat, provisioning lifecycle, and SRE governance maintenance states
 * of a cloud, hybrid, or on-premises compute resource.
 */
@Serdeable
public enum ResourceStatus {
    /** Resource is successfully deployed, powered on, and actively communicating heartbeat signatures. */
    RUNNING,

    /** Resource is administratively or physically powered off, paused, or suspended. */
    STOPPED,

    /** Resource is in the orchestration pipeline (e.g., AWS EC2 spinning up, Docker build executing). */
    PROVISIONING,

    /** Resource has been permanently deleted, de-provisioned, or purged from cloud/hypervisor bounds. */
    TERMINATED,

    /** Critical state! Hypervisor or automation system lost network ping or telemetry route with the node. */
    UNREACHABLE,

    /** * Resource is undergoing an authorized, scheduled maintenance window (e.g., hardware upgrades, kernel patching).
     * Automated monitoring alerts and notification pathways (PagerDuty/Slack) must be muted/silenced for this node.
     */
    MAINTENANCE,

    /**
     * Graceful decommissioning state. The cluster node or host is bleeding out connections, finishing running tasks,
     * and refusing new incoming traffic routing requests before entering hard termination.
     */
    DRAINING,

    /**
     * Permanently retired and decommissioned asset. The record is preserved within the cluster database
     * for strict financial amortization auditing and historical inventory tracking, but the instance is offline forever.
     */
    DECOMMISSIONED;

    /**
     * Safely converts inbound API string tokens into the target ResourceStatus enum instance.
     * Supports absolute case-insensitive matches and auto-trims whitespace strings.
     *
     * @param value The raw input string payload data from the wire.
     * @return The matched ResourceStatus token instance.
     * @throws com.local.app.service.exception.BusinessException If the parameter string does not align with any valid state node.
     */
    public static ResourceStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new com.local.app.service.exception.BusinessException("Resource status parsing failure: Input token cannot be null or blank.");
        }

        try {
            return ResourceStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.local.app.service.exception.BusinessException("Resource status parsing failure: '" + value + "' is not a recognized operational state.");
        }
    }
}
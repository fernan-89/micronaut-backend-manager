package com.local.app.model;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Embedded subdocument tracking detailed network topology, routing vectors,
 * hardware mac identification, and DNS configurations across public or private segments.
 */
@Serdeable
public record NetworkConfig(
        String privateIp,          // E.g., "10.0.4.15" or "192.168.1.50"
        String publicIp,           // E.g., "54.211.90.4" (Null or empty if internal isolated node)
        String subnetCidr,         // E.g., "10.0.4.0/24" or "192.168.1.0/24"
        String gateway,            // E.g., "10.0.4.1" or "192.168.1.1"
        String macAddress,         // Hardware physical burn address for bare-metal/VM inventory matching
        String internalDnsName,    // E.g., "api-worker-01.home.local" or "ec2.internal"
        String externalFqdn        // Fully Qualified Domain Name, e.g., "vm1.datacenterx.com"
) {
    /**
     * Compact constructor to sanitize incoming infrastructure string records.
     */
    public NetworkConfig {
        privateIp = (privateIp == null) ? "" : privateIp.trim();
        publicIp = (publicIp == null) ? "" : publicIp.trim();
        subnetCidr = (subnetCidr == null) ? "" : subnetCidr.trim();
        gateway = (gateway == null) ? "" : gateway.trim();
        macAddress = (macAddress == null) ? "" : macAddress.trim().toUpperCase();
        internalDnsName = (internalDnsName == null) ? "" : internalDnsName.trim().toLowerCase();
        externalFqdn = (externalFqdn == null) ? "" : externalFqdn.trim().toLowerCase();
    }
}
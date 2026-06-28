package com.local.app.model;

import com.local.app.model.enums.LicenseStatus;
import com.local.app.model.enums.LicenseType;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapped Mongo entity representing a corporate software license contract.
 * Tracks compliance, available seat counts, expiration timelines, and structural physical/user allocations.
 */
@Serdeable
@MappedEntity("software_licenses")
public class SoftwareLicenseModel {

    @Id
    private String id;
    private String licenseHash;
    private String softwareName;
    private String publisher;
    private String licenseKey;
    private LicenseType licenseType;
    private LicenseStatus status;
    private Integer totalSeats;
    private Integer allocatedSeats;
    private Instant acquisitionDate;
    private Instant expirationDate;
    private Instant createdAt;
    private Instant updatedAt;
    private List<LicenseAllocation> allocations;

    /**
     * Default constructor for serialization and mapping frameworks.
     */
    public SoftwareLicenseModel() {
    }

    /**
     * Comprehensive constructor initialization.
     */
    public SoftwareLicenseModel(String id, String licenseHash, String softwareName, String publisher,
                                String licenseKey, LicenseType licenseType, LicenseStatus status,
                                Integer totalSeats, Integer allocatedSeats, Instant acquisitionDate,
                                Instant expirationDate, Instant createdAt, Instant updatedAt,
                                List<LicenseAllocation> allocations) {
        this.id = id;
        this.licenseHash = licenseHash;
        this.softwareName = softwareName;
        this.publisher = publisher;
        this.licenseKey = licenseKey;
        this.licenseType = licenseType;
        this.status = status;
        this.totalSeats = totalSeats;
        this.allocatedSeats = allocatedSeats != null ? allocatedSeats : 0;
        this.acquisitionDate = acquisitionDate;
        this.expirationDate = expirationDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.allocations = allocations != null ? allocations : new ArrayList<>();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLicenseHash() { return licenseHash; }
    public void setLicenseHash(String licenseHash) { this.licenseHash = licenseHash; }

    public String getSoftwareName() { return softwareName; }
    public void setSoftwareName(String softwareName) { this.softwareName = softwareName; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }

    public LicenseType getLicenseType() { return licenseType; }
    public void setLicenseType(LicenseType licenseType) { this.licenseType = licenseType; }

    public LicenseStatus getStatus() { return status; }
    public void setStatus(LicenseStatus status) { this.status = status; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Integer getAllocatedSeats() { return allocatedSeats; }
    public void setAllocatedSeats(Integer allocatedSeats) { this.allocatedSeats = allocatedSeats; }

    public Instant getAcquisitionDate() { return acquisitionDate; }
    public void setAcquisitionDate(Instant acquisitionDate) { this.acquisitionDate = acquisitionDate; }

    public Instant getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Instant expirationDate) { this.expirationDate = expirationDate; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public List<LicenseAllocation> getAllocations() { return allocations; }
    public void setAllocations(List<LicenseAllocation> allocations) { this.allocations = allocations; }

    /**
     * Structural business method to safely add a new allocation record to the license.
     * Automatically increments the active seat counter and updates the modification timestamp.
     *
     * @param allocation The validated immutable allocation instance.
     */
    public void addAllocation(LicenseAllocation allocation) {
        if (this.allocations == null) {
            this.allocations = new ArrayList<>();
        }
        this.allocations.add(allocation);
        this.allocatedSeats = this.allocations.size();
        this.updatedAt = Instant.now();
    }
}
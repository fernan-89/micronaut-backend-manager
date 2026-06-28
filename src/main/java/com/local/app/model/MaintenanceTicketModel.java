package com.local.app.model;

import com.local.app.model.enums.MaintenanceStatus;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapped Mongo entity representing a centralized hardware maintenance ticket.
 * Manages the progressive states from triage to completion and records encapsulated historical comments.
 */
@Serdeable
@MappedEntity("maintenance_tickets")
public class MaintenanceTicketModel {

    @Id
    private String id;
    private String ticketHash;
    private String assetHash;
    private String requesterId;
    private String assignedTechnicianId;
    private String issueDescription;
    private MaintenanceStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private List<MaintenanceComment> comments;

    /**
     * Default constructor for serialization and reflection frameworks.
     */
    public MaintenanceTicketModel() {
    }

    /**
     * Comprehensive constructor initialization.
     */
    public MaintenanceTicketModel(String id, String ticketHash, String assetHash, String requesterId,
                                  String assignedTechnicianId, String issueDescription, MaintenanceStatus status,
                                  Instant createdAt, Instant updatedAt, List<MaintenanceComment> comments) {
        this.id = id;
        this.ticketHash = ticketHash;
        this.assetHash = assetHash;
        this.requesterId = requesterId;
        this.assignedTechnicianId = assignedTechnicianId;
        this.issueDescription = issueDescription;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments != null ? comments : new ArrayList<>();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTicketHash() { return ticketHash; }
    public void setTicketHash(String ticketHash) { this.ticketHash = ticketHash; }

    public String getAssetHash() { return assetHash; }
    public void setAssetHash(String assetHash) { this.assetHash = assetHash; }

    public String getRequesterId() { return requesterId; }
    public void setRequesterId(String requesterId) { this.requesterId = requesterId; }

    public String getAssignedTechnicianId() { return assignedTechnicianId; }
    public void setAssignedTechnicianId(String assignedTechnicianId) { this.assignedTechnicianId = assignedTechnicianId; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public List<MaintenanceComment> getComments() { return comments; }
    public void setComments(List<MaintenanceComment> comments) { this.comments = comments; }

    /**
     * Structural business method to safely add a new immutable comment to the ticket timeline.
     * Automatically sets the system update timestamp.
     *
     * @param comment The validated comment record instance.
     */
    public void addComment(MaintenanceComment comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
        this.updatedAt = Instant.now();
    }
}
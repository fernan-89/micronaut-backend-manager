package com.local.app.service;

import com.local.app.dto.request.AddCommentRequest;
import com.local.app.dto.request.MaintenanceTicketRequest;
import com.local.app.dto.response.MaintenanceTicketResponse;
import com.local.app.exception.AssetNotFoundException;
import com.local.app.model.MaintenanceComment;
import com.local.app.model.MaintenanceTicketModel;
import com.local.app.model.AuditLogModel;
import com.local.app.model.enums.MaintenanceStatus;
import com.local.app.repository.AssetManagementRepository;
import com.local.app.repository.MaintenanceTicketRepository;
import com.local.app.repository.AuditLogRepository;
import com.local.app.service.exception.BusinessException;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Core business service handling orchestration logic for hardware maintenance lifecycles.
 * Integrates directly with asset checking bounds and structural audit logs pipelines.
 */
@Singleton
public class MaintenanceTicketService {

    private final MaintenanceTicketRepository repository;
    private final AssetManagementRepository assetRepository;
    private final AuditLogRepository logRepository;

    /**
     * Dependency injection constructor.
     */
    public MaintenanceTicketService(MaintenanceTicketRepository repository,
                                    AssetManagementRepository assetRepository,
                                    AuditLogRepository logRepository) {
        this.repository = repository;
        this.assetRepository = assetRepository;
        this.logRepository = logRepository;
    }

    /**
     * Launches a new corporate hardware maintenance ticket record.
     * Validates that the requested target asset hash exists in inventory database first.
     *
     * @param request  Mandatory ticket input attributes.
     * @param executor Operator trigger entity string.
     * @return Generated ticket summary snapshot response.
     */
    public MaintenanceTicketResponse createTicket(MaintenanceTicketRequest request, String executor) {
        // Enforce asset integrity checkpoint
        assetRepository.findByAssetHash(request.assetHash())
                .orElseThrow(() -> new AssetNotFoundException("Cannot open ticket. Asset not found with hash: " + request.assetHash()));

        String ticketHash = generateTicketHash();
        Instant now = Instant.now();

        MaintenanceTicketModel model = new MaintenanceTicketModel(
                null,
                ticketHash,
                request.assetHash(),
                request.requesterId(),
                request.assignedTechnicianId(),
                request.issueDescription(),
                MaintenanceStatus.OPENED,
                now,
                now,
                new ArrayList<>()
        );

        // Save trace audit trail record
        logRepository.save(new AuditLogModel(
                generateLogHash(), ticketHash, "Maintenance ticket opened", Collections.emptyMap(), LocalDateTime.now(), executor
        ));

        return mapToResponse(repository.save(model));
    }

    /**
     * Retrieves a distinct maintenance lifecycle entity record matching business key string token.
     */
    public MaintenanceTicketResponse findByHash(String ticketHash) {
        return repository.findByTicketHash(ticketHash)
                .map(this::mapToResponse)
                .orElseThrow(() -> new BusinessException("Maintenance ticket not found matching hash: " + ticketHash));
    }

    /**
     * Injects an atomic timeline commentary tracking thread inside an existing active maintenance ticket document.
     *
     * @param ticketHash The unique identifier key of the ticket.
     * @param request    Comment text payload details.
     * @param executor   Operator entity user.
     * @return Updated ticket snapshot response.
     */
    public MaintenanceTicketResponse addComment(String ticketHash, AddCommentRequest request, String executor) {
        return repository.findByTicketHash(ticketHash).map(ticket -> {

            // Auto-advance status to IN_ANALYSIS if it was just opened upon receiving the first diagnostic technical comment
            if (ticket.getStatus() == MaintenanceStatus.OPENED) {
                ticket.setStatus(MaintenanceStatus.IN_ANALYSIS);
            }

            MaintenanceComment newComment = new MaintenanceComment(
                    request.author(),
                    request.message(),
                    ticket.getStatus(),
                    Instant.now()
            );

            ticket.addComment(newComment);
            ticket.setUpdatedAt(Instant.now());

            logRepository.save(new AuditLogModel(
                    generateLogHash(), ticketHash, "New engineering workflow comment added",
                    Map.of("messageExcerpt", new AuditLogModel.ChangeDetail(null, request.message().substring(0, Math.min(request.message().length(), 30)))),
                    LocalDateTime.now(), executor
            ));

            return mapToResponse(repository.update(ticket));
        }).orElseThrow(() -> new BusinessException("Cannot append comment. Ticket not found: " + ticketHash));
    }

    /**
     * Updates the global operational state transition vector of a ticket.
     *
     * @param ticketHash Target ticket business key identifier.
     * @param newStatus  Target enum token node.
     * @param executor   Operator action executor.
     * @return Transited ticket profile representation.
     */
    public MaintenanceTicketResponse updateStatus(String ticketHash, MaintenanceStatus newStatus, String executor) {
        return repository.findByTicketHash(ticketHash).map(ticket -> {
            if (ticket.getStatus() == MaintenanceStatus.COMPLETED || ticket.getStatus() == MaintenanceStatus.CANCELED) {
                throw new BusinessException("Terminal state constraint validation error. Closed/Canceled tickets cannot shift status.");
            }

            String oldStatusStr = ticket.getStatus().name();
            ticket.setStatus(newStatus);
            ticket.setUpdatedAt(Instant.now());

            logRepository.save(new AuditLogModel(
                    generateLogHash(), ticketHash, "Ticket structural status transition altered",
                    Map.of("status", new AuditLogModel.ChangeDetail(oldStatusStr, newStatus.name())),
                    LocalDateTime.now(), executor
            ));

            return mapToResponse(repository.update(ticket));
        }).orElseThrow(() -> new BusinessException("Cannot alter status bounds. Ticket not found: " + ticketHash));
    }

    private MaintenanceTicketResponse mapToResponse(MaintenanceTicketModel model) {
        return new MaintenanceTicketResponse(
                model.getTicketHash(), model.getAssetHash(), model.getRequesterId(),
                model.getAssignedTechnicianId(), model.getIssueDescription(), model.getStatus(),
                model.getCreatedAt(), model.getUpdatedAt(), model.getComments()
        );
    }

    private String generateTicketHash() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return "TK-" + datePart + "-" + randomPart.substring(0, 4);
    }

    private String generateLogHash() {
        return "LOG-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
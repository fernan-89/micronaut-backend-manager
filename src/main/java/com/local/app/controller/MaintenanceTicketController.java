package com.local.app.controller;

import com.local.app.dto.request.AddCommentRequest;
import com.local.app.dto.request.MaintenanceTicketRequest;
import com.local.app.dto.response.MaintenanceTicketResponse;
import com.local.app.model.enums.MaintenanceStatus;
import com.local.app.service.MaintenanceTicketService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.net.URI;

/**
 * REST Endpoint exposure gateway responsible for processing hardware maintenance orchestration pipelines.
 */
@Controller("/maintenance")
@Tag(name = "Maintenance Workflow", description = "Operations related to corporate hardware repair tickets, diagnostics, and lifecycle status logs.")
@Validated
public class MaintenanceTicketController {

    private final MaintenanceTicketService service;

    /**
     * Controller injection mapping handler.
     */
    public MaintenanceTicketController(MaintenanceTicketService service) {
        this.service = service;
    }

    /**
     * Endpoint to launch/register a new hardware engineering repair ticket.
     */
    @Post
    @Operation(summary = "Open a maintenance ticket", description = "Initializes a central tracking record bound against an existing inventory hardware asset hash.")
    @ApiResponse(responseCode = "201", description = "Ticket successfully opened and cataloged",
            content = @Content(schema = @Schema(implementation = MaintenanceTicketResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid payload structural attributes")
    @ApiResponse(responseCode = "404", description = "Target infrastructure asset hash not found")
    public HttpResponse<MaintenanceTicketResponse> openTicket(
            @Body @Valid MaintenanceTicketRequest request,
            @Parameter(description = "Identifier of the agent dispatching the pipeline action")
            @Header("X-Executor") @NotBlank String executor) {

        MaintenanceTicketResponse response = service.createTicket(request, executor);
        return HttpResponse.created(response, URI.create("/maintenance/" + response.ticketHash()));
    }

    /**
     * Endpoint to fetch a distinct maintenance document profile entry graph graph.
     */
    @Get("/{ticketHash}")
    @Operation(summary = "Retrieve a ticket profile", description = "Fetches complete ongoing details, timeline commentary logs, and status scopes of a ticket.")
    @ApiResponse(responseCode = "200", description = "Maintenance tracking ticket found and returned")
    @ApiResponse(responseCode = "404", description = "Ticket not found matching hash parameter")
    public HttpResponse<MaintenanceTicketResponse> getTicketByHash(
            @Parameter(description = "The specific business token string hash reference")
            @PathVariable String ticketHash) {

        return HttpResponse.ok(service.findByHash(ticketHash));
    }

    /**
     * Endpoint to append an immutable discursive progress entry inside the ticket timeline.
     */
    @Post("/{ticketHash}/comments")
    @Operation(summary = "Add a progress comment", description = "Appends a historical immutable textual diagnostic or status notation log directly inside the document timeline graph.")
    @ApiResponse(responseCode = "200", description = "Comment successfully appended into the timeline list document graph")
    @ApiResponse(responseCode = "404", description = "Ticket target hash parameter reference not found")
    public HttpResponse<MaintenanceTicketResponse> appendComment(
            @PathVariable String ticketHash,
            @Body @Valid AddCommentRequest request,
            @Parameter(description = "Identifier of the human technician or webhook updating the queue trace")
            @Header("X-Executor") @NotBlank String executor) {

        return HttpResponse.ok(service.addComment(ticketHash, request, executor));
    }

    /**
     * Endpoint to alter the workflow node state location vector parameter.
     */
    @Patch("/{ticketHash}/status")
    @Operation(summary = "Transition ticket lifecycle state", description = "Alters the internal operational track enum status location nodes using strict guardrails checking mechanisms.")
    @ApiResponse(responseCode = "200", description = "Status updated and saved successfully")
    @ApiResponse(responseCode = "400", description = "State validation guard check bounds violation or terminal node block lock")
    public HttpResponse<MaintenanceTicketResponse> transitionStatus(
            @PathVariable String ticketHash,
            @Parameter(description = "The raw target MaintenanceStatus string node token enum")
            @QueryValue String targetStatus,
            @Parameter(description = "Identifier of the supervisor executing systemic lifecycle override")
            @Header("X-Executor") @NotBlank String executor) {

        MaintenanceStatus status = MaintenanceStatus.fromString(targetStatus);
        return HttpResponse.ok(service.updateStatus(ticketHash, status, executor));
    }
}
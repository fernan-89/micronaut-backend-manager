package com.local.app.controller;

import com.local.app.dto.request.ComputeResourceRequest;
import com.local.app.dto.request.ComputeResourceTelemetryRequest;
import com.local.app.dto.response.ComputeResourceResponse;
import com.local.app.service.ComputeResourceService;
import com.local.app.service.ComputeResourceService.MaintenanceScheduleCmd;
import com.local.app.service.ComputeResourceService.MaintenanceStartCmd;
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
 * REST Endpoint exposure gateway responsible for managing cloud, hybrid,
 * and on-premises compute infrastructure instances and governing SRE maintenance windows.
 */
@Controller("/compute")
@Tag(name = "Compute Resource Management", description = "Operations inventorying hardware and executing advanced lifecycle maintenance protocols.")
@Validated
public class ComputeResourceController {

    private final ComputeResourceService service;

    /**
     * Controller injection mapping constructor.
     */
    public ComputeResourceController(ComputeResourceService service) {
        this.service = service;
    }

    /**
     * Endpoint to catalog and inventory a new compute node.
     */
    @Post
    @Operation(summary = "Register a new hybrid compute resource node", description = "Logs and provisions a target cloud instance, on-prem VM, or physical hardware asset profile.")
    @ApiResponse(responseCode = "201", description = "Compute resource instance cataloged successfully",
            content = @Content(schema = @Schema(implementation = ComputeResourceResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid payload constraints or unrecognized type configuration tokens")
    public HttpResponse<ComputeResourceResponse> registerComputeNode(
            @Body @Valid ComputeResourceRequest request,
            @Parameter(description = "Identifier of the administrative engineer cataloging the asset node")
            @Header("X-Executor") @NotBlank String executor) {

        ComputeResourceResponse response = service.registerResource(request, executor);
        return HttpResponse.created(response, URI.create("/compute/" + response.resourceHash()));
    }

    /**
     * Endpoint to fetch a single compute profile by hash tracking token.
     */
    @Get("/{resourceHash}")
    @Operation(summary = "Retrieve a distinct compute profile configuration", description = "Fetches complete specifications, active network layout mapping, and the latest hardware usage telemetry snapshot.")
    @ApiResponse(responseCode = "200", description = "Compute node profile located and returned")
    @ApiResponse(responseCode = "404", description = "Target resource tracking hash token not found in cluster storage")
    public HttpResponse<ComputeResourceResponse> getComputeNodeByHash(
            @Parameter(description = "The specific infra business key tracking token hash")
            @PathVariable String resourceHash) {

        // FIXED: Corrigido o encadeamento duplicado 'service.service' para apenas 'service'
        return HttpResponse.ok(service.findByHash(resourceHash));
    }

    /**
     * High-frequency endpoint to ingest real-time hardware telemetry streams.
     */
    @Patch("/{resourceHash}/telemetry")
    @Operation(summary = "Ingest remote agent streaming telemetry metrics", description = "Updates active processing strain load percentages, network throughput, and overrides the node operational health status.")
    @ApiResponse(responseCode = "200", description = "Telemetry packet successfully processed and counters overridden")
    @ApiResponse(responseCode = "400", description = "Invalid telemetry counters or parsing failure exception triggered")
    @ApiResponse(responseCode = "404", description = "Target resource tracking hash not found to map metrics link")
    public HttpResponse<ComputeResourceResponse> ingestMetrics(
            @PathVariable String resourceHash,
            @Body @Valid ComputeResourceTelemetryRequest request) {

        return HttpResponse.ok(service.updateTelemetry(resourceHash, request));
    }

    /**
     * Endpoint to reserve a future scheduled maintenance window timeline block.
     */
    @Post("/{resourceHash}/maintenance/schedule")
    @Operation(summary = "Schedule a future maintenance window", description = "Reserves a timeline block for physical or virtual hardware interventions, mapping out expected duration and alarm silences.")
    @ApiResponse(responseCode = "200", description = "Maintenance window successfully reserved in inventory timeline")
    @ApiResponse(responseCode = "400", description = "Invalid timeline boundaries or date execution logic sequence constraints")
    @ApiResponse(responseCode = "404", description = "Target compute resource hash not found")
    public HttpResponse<ComputeResourceResponse> scheduleMaintenanceBlock(
            @PathVariable String resourceHash,
            @Body @Valid MaintenanceScheduleCmd command,
            @Header("X-Executor") @NotBlank String executor) {

        return HttpResponse.ok(service.scheduleMaintenance(resourceHash, command, executor));
    }

    /**
     * Endpoint to manually force a node into an active maintenance cycle.
     */
    @Post("/{resourceHash}/maintenance/start")
    @Operation(summary = "Activate maintenance isolation protocol", description = "Shifts node into MAINTENANCE state, applying hard suppressions against monitoring agent ping interference.")
    @ApiResponse(responseCode = "200", description = "Asset locked and isolated under hard maintenance mode")
    @ApiResponse(responseCode = "404", description = "Target compute resource hash not found")
    public HttpResponse<ComputeResourceResponse> initiateActiveMaintenance(
            @PathVariable String resourceHash,
            @Body @Valid MaintenanceStartCmd command,
            @Header("X-Executor") @NotBlank String executor) {

        return HttpResponse.ok(service.startMaintenance(resourceHash, command, executor));
    }

    /**
     * Endpoint to release an asset from its active maintenance cycle.
     */
    @Post("/{resourceHash}/maintenance/end")
    @Operation(summary = "Terminate maintenance mode and start verification loop", description = "Closes actual timestamps and returns node to PROVISIONING status to await clean agent telemetry synchronization.")
    @ApiResponse(responseCode = "200", description = "Asset released from maintenance and routed back to verification queues")
    @ApiResponse(responseCode = "400", description = "Target asset is not currently flagged under an open maintenance loop")
    @ApiResponse(responseCode = "404", description = "Target compute resource hash not found")
    public HttpResponse<ComputeResourceResponse> concludeActiveMaintenance(
            @PathVariable String resourceHash,
            @Header("X-Executor") @NotBlank String executor) {

        return HttpResponse.ok(service.endMaintenance(resourceHash, executor));
    }
}
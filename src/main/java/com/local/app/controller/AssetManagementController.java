package com.local.app.controller;

import com.local.app.dto.request.AssetManagementRequest;
import com.local.app.dto.response.AssetManagementResponse;
import com.local.app.service.AssetManagementService;
import io.micronaut.core.annotation.Nullable;
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
import java.util.Collections;
import java.util.List;

/**
 * REST Controller responsible for exposing the Asset Management API endpoints.
 * Handles HTTP requests, enforces payload validation, and delegates business logic
 * to the underlying {@link AssetManagementService}.
 */
@Controller("/assets")
@Tag(name = "Asset Management", description = "Operations related to corporate hardware asset lifecycle and inventory.")
@Validated
public class AssetManagementController {

    private final AssetManagementService service;

    /**
     * Injects the required service dependency.
     *
     * @param service The asset management business logic handler.
     */
    public AssetManagementController(AssetManagementService service) {
        this.service = service;
    }

    /**
     * Endpoint to register a new hardware asset in the corporate inventory.
     *
     * @param request  The validated JSON payload containing asset specifications.
     * @param executor The identifier of the user or system performing the action (passed via header).
     * @return HTTP 201 Created with the location URI and the created asset payload.
     */
    @Post
    @Operation(summary = "Register a new asset", description = "Creates a new hardware asset and initializes its centralized audit trail.")
    @ApiResponse(responseCode = "201", description = "Asset successfully created",
            content = @Content(schema = @Schema(implementation = AssetManagementResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "409", description = "Asset tag or serial number already exists")
    public HttpResponse<AssetManagementResponse> createAsset(
            @Body @Valid AssetManagementRequest request,
            @Parameter(description = "Identifier of the entity executing the creation")
            @Header("X-Executor") @NotBlank String executor) {

        AssetManagementResponse response = service.createAsset(request, executor);
        return HttpResponse.created(response, URI.create("/assets/" + response.assetHash()));
    }

    /**
     * Endpoint to search or list corporate assets using optional filtering parameters.
     *
     * @param assetTag     Optional physical identification tag filter.
     * @param serialNumber Optional manufacturer serial number filter.
     * @return HTTP 200 OK with the filtered list of assets matching the query constraints.
     */
    @Get
    @Operation(summary = "Search or list assets", description = "Retrieves a list of assets filtered optionally by physical asset tag or serial number.")
    @ApiResponse(responseCode = "200", description = "Query executed successfully")
    public HttpResponse<List<AssetManagementResponse>> listOrSearchAssets(
            @Parameter(description = "Filter by exact corporate asset tag") @QueryValue @Nullable String assetTag,
            @Parameter(description = "Filter by exact hardware serial number") @QueryValue @Nullable String serialNumber) {

        // Delega para o service baseado nos filtros informados
        if (assetTag != null && !assetTag.isBlank()) {
            return HttpResponse.ok(List.of(service.findByAssetTag(assetTag)));
        }
        if (serialNumber != null && !serialNumber.isBlank()) {
            return HttpResponse.ok(List.of(service.findBySerialNumber(serialNumber)));
        }

        return HttpResponse.ok(service.findAll());
    }

    /**
     * Endpoint to retrieve the current profile and state of a specific asset.
     *
     * @param assetHash The unique corporate business key of the asset.
     * @return HTTP 200 OK with the asset details, or HTTP 404 if not found.
     */
    @Get("/{assetHash}")
    @Operation(summary = "Retrieve an asset", description = "Fetches the current specifications and lifecycle state of an asset by its hash.")
    @ApiResponse(responseCode = "200", description = "Asset found and returned",
            content = @Content(schema = @Schema(implementation = AssetManagementResponse.class)))
    @ApiResponse(responseCode = "404", description = "Asset not found")
    public HttpResponse<AssetManagementResponse> getAsset(
            @Parameter(description = "The unique business hash of the asset")
            @PathVariable String assetHash) {

        return HttpResponse.ok(service.findByHash(assetHash));
    }

    /**
     * Endpoint to update an existing asset's specifications or lifecycle status.
     *
     * @param assetHash The unique corporate business key of the asset.
     * @param request   The validated JSON payload containing the new values.
     * @param executor  The identifier of the user or system performing the action (passed via header).
     * @return HTTP 200 OK with the updated asset payload.
     */
    @Put("/{assetHash}")
    @Operation(summary = "Update an asset", description = "Performs a full or partial update on an asset and logs granular from/to changes in the audit trail.")
    @ApiResponse(responseCode = "200", description = "Asset successfully updated",
            content = @Content(schema = @Schema(implementation = AssetManagementResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "404", description = "Asset not found")
    @ApiResponse(responseCode = "409", description = "Asset tag or serial number conflict")
    public HttpResponse<AssetManagementResponse> updateAsset(
            @Parameter(description = "The unique business hash of the asset")
            @PathVariable String assetHash,
            @Body @Valid AssetManagementRequest request,
            @Parameter(description = "Identifier of the entity executing the update")
            @Header("X-Executor") @NotBlank String executor) {

        return HttpResponse.ok(service.updateAsset(assetHash, request, executor));
    }

    /**
     * Endpoint to permanently delete a hardware asset from the system.
     *
     * @param assetHash The unique corporate business key of the asset.
     * @return HTTP 204 No Content upon successful deletion, or HTTP 404 if not found.
     */
    @Delete("/{assetHash}")
    @Operation(summary = "Delete an asset", description = "Permanently removes the hardware asset record from the inventory database.")
    @ApiResponse(responseCode = "204", description = "Asset successfully deleted")
    @ApiResponse(responseCode = "404", description = "Asset not found")
    public HttpResponse<Void> deleteAsset(
            @Parameter(description = "The unique business hash of the asset")
            @PathVariable String assetHash) {

        service.deleteAsset(assetHash);
        return HttpResponse.noContent();
    }
}
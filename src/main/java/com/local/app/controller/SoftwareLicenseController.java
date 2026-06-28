package com.local.app.controller;

import com.local.app.dto.request.SoftwareLicenseAllocationRequest;
import com.local.app.dto.request.SoftwareLicenseRequest;
import com.local.app.dto.response.SoftwareLicenseResponse;
import com.local.app.service.SoftwareLicenseService;
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
 * REST Endpoint exposure gateway responsible for managing corporate software license agreements and seats compliance.
 */
@Controller("/licenses")
@Tag(name = "Software License Management", description = "Operations managing corporate system keys, purchased software agreements, quotas, and structural asset allocations.")
@Validated
public class SoftwareLicenseController {

    private final SoftwareLicenseService service;

    /**
     * Controller injection mapping constructor.
     */
    public SoftwareLicenseController(SoftwareLicenseService service) {
        this.service = service;
    }

    /**
     * Endpoint to catalog and purchase a new software license.
     */
    @Post
    @Operation(summary = "Register a new software license contract", description = "Purchases and stores a software contract parameter block into inventory database bounds.")
    @ApiResponse(responseCode = "201", description = "Software license contract successfully cataloged",
            content = @Content(schema = @Schema(implementation = SoftwareLicenseResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid payload structural attributes or unrecognized enum tokens")
    public HttpResponse<SoftwareLicenseResponse> registerLicense(
            @Body @Valid SoftwareLicenseRequest request,
            @Parameter(description = "Identifier of the administrative executive executing the contract logging")
            @Header("X-Executor") @NotBlank String executor) {

        SoftwareLicenseResponse response = service.createLicense(request, executor);
        return HttpResponse.created(response, URI.create("/licenses/" + response.licenseHash()));
    }

    /**
     * Endpoint to fetch a single software license document profile by hash.
     */
    @Get("/{licenseHash}")
    @Operation(summary = "Retrieve a distinct license configuration", description = "Fetches comprehensive details, metadata tracking, status nodes, and ongoing allocation lists of a contract.")
    @ApiResponse(responseCode = "200", description = "Software license configuration located and returned")
    @ApiResponse(responseCode = "404", description = "Contract hash target parameter not found in cluster storage")
    public HttpResponse<SoftwareLicenseResponse> getLicenseByHash(
            @Parameter(description = "The specific corporate license string token identifier hash")
            @PathVariable String licenseHash) {

        return HttpResponse.ok(service.findByHash(licenseHash));
    }

    /**
     * Endpoint to consume/allocate a single software license seat.
     */
    @Post("/{licenseHash}/allocations")
    @Operation(summary = "Allocate a single software license seat", description = "Deploys and links a license seat entry record to a corporate user profile or hardware workstation machine asset.")
    @ApiResponse(responseCode = "200", description = "Seat successfully allocated and compliance counters updated")
    @ApiResponse(responseCode = "400", description = "Compliance capacity ceiling bounds depletion or expired status restriction exception triggered")
    @ApiResponse(responseCode = "404", description = "Target contract reference hash not found")
    public HttpResponse<SoftwareLicenseResponse> allocateLicenseSeat(
            @PathVariable String licenseHash,
            @Body @Valid SoftwareLicenseAllocationRequest request,
            @Parameter(description = "Identifier of the supervisor routing the deployment seat link")
            @Header("X-Executor") @NotBlank String executor) {

        return HttpResponse.ok(service.allocateSeat(licenseHash, request, executor));
    }
}
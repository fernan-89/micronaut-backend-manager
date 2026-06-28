package com.local.app.controller;

import com.local.app.dto.request.UserManagementRequest;
import com.local.app.dto.response.AuditLogResponse;
import com.local.app.dto.response.UserManagementResponse;
import com.local.app.service.UserManagementService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller responsible for handling User Management operations.
 * This class exposes HTTP endpoints to perform CRUD (Create, Read, Update, Delete)
 * and search operations on user resources, including audit history retrieval.
 */
@Tag(name = "User Management", description = "Operations related to user management")
@Controller("/users")
public class UserManagementController {

    private final UserManagementService service;

    /**
     * Constructs a new UserManagementController with the required service dependency.
     *
     * @param service The user management service containing the core business logic.
     */
    public UserManagementController(UserManagementService service) {
        this.service = service;
    }

    /**
     * Creates a new user in the system.
     */
    @Operation(summary = "Create a new user", description = "Persists a new user record in the system.")
    @Post
    public HttpResponse<UserManagementResponse> create(@Body @Valid UserManagementRequest request) {
        return HttpResponse.created(service.createUser(request));
    }

    /**
     * Retrieves user details by their unique hash.
     */
    @Operation(summary = "Find user by hash", description = "Retrieves user details using their unique identifier.")
    @Get("/{userHash}")
    public HttpResponse<UserManagementResponse> getByHash(String userHash) {
        return HttpResponse.ok(service.findByHash(userHash));
    }

    /**
     * Retrieves the audit history for a specific user.
     *
     * @param userHash The unique string hash of the user.
     * @return A list of {@link AuditLogResponse} containing all historical changes.
     */
    @Operation(summary = "Get user audit logs", description = "Retrieves the historical changes for a specific user from the audit collection.")
    @Get("/{userHash}/logs")
    public List<AuditLogResponse> getAuditLogs(String userHash) {
        return service.findLogsByEntityHash(userHash);
    }

    /**
     * Searches for a user based on query parameters.
     */
    @Operation(summary = "Search for a user", description = "Searches for a user based on multiple optional criteria.")
    @Get("/search")
    public HttpResponse<UserManagementResponse> search(
            @QueryValue(value = "companyEmail", defaultValue = "") String companyEmail,
            @QueryValue(value = "personalEmail", defaultValue = "") String personalEmail,
            @QueryValue(value = "login", defaultValue = "") String login,
            @QueryValue(value = "companyPhone", defaultValue = "") String companyPhone,
            @QueryValue(value = "personalPhone", defaultValue = "") String personalPhone) {

        return HttpResponse.ok(service.searchUser(companyEmail, personalEmail, login, companyPhone, personalPhone));
    }

    /**
     * Lists all users.
     */
    @Operation(summary = "List all users", description = "Retrieves all user records from the system.")
    @Get
    public List<UserManagementResponse> listAll() {
        return service.findAll();
    }

    /**
     * Updates an existing user and logs the action.
     */
    @Operation(summary = "Update an existing user", description = "Modifies user profile details and logs the action centrally.")
    @Put("/{userHash}")
    public HttpResponse<UserManagementResponse> update(
            String userHash,
            @Header("X-Executor") String executor,
            @Body @Valid UserManagementRequest request) {
        return HttpResponse.ok(service.updateUser(userHash, request, executor));
    }

    /**
     * Deletes a user.
     */
    @Operation(summary = "Delete a user", description = "Permanently removes a user record from the system.")
    @Delete("/{userHash}")
    public HttpResponse<?> delete(String userHash) {
        service.deleteUser(userHash);
        return HttpResponse.noContent();
    }
}
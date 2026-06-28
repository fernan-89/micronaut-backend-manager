package com.local.app.controller.exception;

import com.local.app.exception.UserAlreadyExistsException;
import com.local.app.exception.UserNotFoundException;
import com.local.app.exception.AssetAlreadyExistsException; // <-- Corrigido para com.local.app
import com.local.app.exception.AssetNotFoundException;      // <-- Corrigido para com.local.app
import com.local.app.service.exception.BusinessException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

/**
 * Global exception handler responsible for intercepting domain-specific exceptions
 * thrown by the application layer and converting them into appropriate HTTP error responses.
 * This ensures that the client receives standardized error payloads instead of raw server stack traces.
 */
public class GlobalExceptionHandler {

    // ==========================================
    // USER MANAGEMENT HANDLERS
    // ==========================================

    /**
     * Exception handler dedicated to intercepting {@link UserNotFoundException}.
     * It maps the exception to an HTTP 404 (Not Found) response.
     */
    @Produces
    @Singleton
    @Requires(classes = {UserNotFoundException.class})
    public static class UserNotFoundHandler implements ExceptionHandler<UserNotFoundException, HttpResponse<JsonError>> {

        /**
         * Converts a {@link UserNotFoundException} into a 404 Not Found HTTP response.
         * The exception message is wrapped in a standard {@link JsonError} object.
         *
         * @param request The incoming HTTP request that triggered the exception.
         * @param exception The specific {@link UserNotFoundException} instance thrown by the business logic.
         * @return An {@link HttpResponse} containing a 404 NOT_FOUND status and a JSON-formatted error body.
         */
        @Override
        public HttpResponse<JsonError> handle(HttpRequest request, UserNotFoundException exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.<JsonError>notFound().body(error);
        }
    }

    /**
     * Exception handler dedicated to intercepting {@link UserAlreadyExistsException}.
     * It maps the exception to an HTTP 409 (Conflict) response, typically used when
     * attempting to create a resource that violates a unique constraint.
     */
    @Produces
    @Singleton
    @Requires(classes = {UserAlreadyExistsException.class})
    public static class UserConflictHandler implements ExceptionHandler<UserAlreadyExistsException, HttpResponse<JsonError>> {

        /**
         * Converts a {@link UserAlreadyExistsException} into a 409 Conflict HTTP response.
         * The exception message is wrapped in a standard {@link JsonError} object to provide
         * clear feedback to the client regarding which field caused the conflict.
         *
         * @param request The incoming HTTP request that triggered the exception.
         * @param exception The specific {@link UserAlreadyExistsException} instance thrown by the business logic.
         * @return An {@link HttpResponse} containing a 409 CONFLICT status and a JSON-formatted error body.
         */
        @Override
        public HttpResponse<JsonError> handle(HttpRequest request, UserAlreadyExistsException exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.<JsonError>status(HttpStatus.CONFLICT).body(error);
        }
    }

    // ==========================================
    // ASSET MANAGEMENT HANDLERS
    // ==========================================

    /**
     * Exception handler dedicated to intercepting {@link AssetNotFoundException}.
     * It maps the exception to an HTTP 404 (Not Found) response.
     */
    @Produces
    @Singleton
    @Requires(classes = {AssetNotFoundException.class})
    public static class AssetNotFoundHandler implements ExceptionHandler<AssetNotFoundException, HttpResponse<JsonError>> {

        /**
         * Converts an {@link AssetNotFoundException} into a 404 Not Found HTTP response.
         * The exception message is wrapped in a standard {@link JsonError} object.
         *
         * @param request The incoming HTTP request that triggered the exception.
         * @param exception The specific {@link AssetNotFoundException} instance thrown by the asset business logic.
         * @return An {@link HttpResponse} containing a 404 NOT_FOUND status and a JSON-formatted error body.
         */
        @Override
        public HttpResponse<JsonError> handle(HttpRequest request, AssetNotFoundException exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.<JsonError>notFound().body(error);
        }
    }

    /**
     * Exception handler dedicated to intercepting {@link AssetAlreadyExistsException}.
     * It maps the exception to an HTTP 409 (Conflict) response, typically used when
     * attempting to create or update an asset that violates corporate unique constraints.
     */
    @Produces
    @Singleton
    @Requires(classes = {AssetAlreadyExistsException.class})
    public static class AssetConflictHandler implements ExceptionHandler<AssetAlreadyExistsException, HttpResponse<JsonError>> {

        /**
         * Converts an {@link AssetAlreadyExistsException} into a 409 Conflict HTTP response.
         * The exception message is wrapped in a standard {@link JsonError} object to provide
         * explicit context regarding unique data violations like serial numbers or corporate asset tags.
         *
         * @param request The incoming HTTP request that triggered the exception.
         * @param exception The specific {@link AssetAlreadyExistsException} instance thrown by the asset business logic.
         * @return An {@link HttpResponse} containing a 409 CONFLICT status and a JSON-formatted error body.
         */
        @Override
        public HttpResponse<JsonError> handle(HttpRequest request, AssetAlreadyExistsException exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.<JsonError>status(HttpStatus.CONFLICT).body(error);
        }
    }

    /**
     * Exception handler dedicated to intercepting {@link BusinessException}.
     * It maps general lifecycle, serialization, or state machine violations to an HTTP 400 (Bad Request) response.
     */
    @Produces
    @Singleton
    @Requires(classes = {BusinessException.class})
    public static class BusinessExceptionHandler implements ExceptionHandler<BusinessException, HttpResponse<JsonError>> {

        /**
         * Converts a {@link BusinessException} into a 400 Bad Request HTTP response.
         * Wraps the domain evaluation violation message inside a structured {@link JsonError} representation.
         *
         * @param request The incoming HTTP request that triggered the validation or parsing issue.
         * @param exception The specific {@link BusinessException} instance thrown by structural models or enums.
         * @return An {@link HttpResponse} containing a 400 BAD_REQUEST status and a JSON-formatted error body.
         */
        @Override
        public HttpResponse<JsonError> handle(HttpRequest request, BusinessException exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
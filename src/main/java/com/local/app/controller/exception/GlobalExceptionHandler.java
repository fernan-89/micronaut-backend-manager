package com.local.app.controller.exception;

import com.local.app.exception.UserAlreadyExistsException;
import com.local.app.exception.UserNotFoundException;
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

    /**
     * Exception handler dedicated to intercepting {@link UserNotFoundException}.
     * It maps the exception to an HTTP 404 (Not Found) response.
     */
    @Produces
    @Singleton
    @Requires(classes = {UserNotFoundException.class})
    public static class NotFoundHandler implements ExceptionHandler<UserNotFoundException, HttpResponse<JsonError>> {

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
    public static class ConflictHandler implements ExceptionHandler<UserAlreadyExistsException, HttpResponse<JsonError>> {

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
}
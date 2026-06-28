package com.local;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

/**
 * Main application entry point for the User Management system.
 * Configured with OpenAPI metadata to generate interactive API documentation.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "ThinkLab Manager API",
                version = "0.1",
                description = "REST API for managing system users and related operations.",
                contact = @Contact(name = "Development Team", email = "dev@thinklab.com"),
                license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
        )
)
public class Application {

    /**
     * Standard main method to bootstrap the Micronaut application.
     *
     * @param args Command line arguments passed during application startup.
     */
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
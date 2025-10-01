package com.restaurant.micronaut.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Restaurant Management API - Micronaut",
        version = "1.0.0",
        description = "REST API for restaurant management system built with Micronaut framework and hexagonal architecture",
        contact = @Contact(
            name = "Restaurant Management Team",
            email = "support@restaurant.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8083",
            description = "Development server (Micronaut)"
        )
    }
)
public class OpenApiConfig {
}

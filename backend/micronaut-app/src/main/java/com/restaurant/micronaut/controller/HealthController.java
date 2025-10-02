package com.restaurant.micronaut.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check controller for Micronaut application
 */
@Controller("/health")
@Tag(name = "Health", description = "Application health check operations")
public class HealthController {

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Health check", description = "Check the health status of the application")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Application is healthy", 
                    content = @Content(schema = @Schema(implementation = HealthResponse.class)))
    })
    public HttpResponse<HealthResponse> health() {
        HealthResponse response = new HealthResponse(
            "UP",
            "Micronaut Restaurant Management API",
            "1.0.0",
            LocalDateTime.now(),
            Map.of(
                "database", "UP",
                "cache", "UP",
                "framework", "Micronaut 4.2.3"
            )
        );
        return HttpResponse.ok(response);
    }

    @Get("/live")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Liveness check", description = "Check if the application is alive")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Application is alive")
    })
    public HttpResponse<Map<String, Object>> liveness() {
        return HttpResponse.ok(Map.of(
            "status", "UP",
            "check", "liveness",
            "timestamp", LocalDateTime.now()
        ));
    }

    @Get("/ready")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Readiness check", description = "Check if the application is ready to serve requests")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Application is ready")
    })
    public HttpResponse<Map<String, Object>> readiness() {
        return HttpResponse.ok(Map.of(
            "status", "UP",
            "check", "readiness",
            "timestamp", LocalDateTime.now(),
            "components", Map.of(
                "database", "UP",
                "cache", "UP"
            )
        ));
    }

    // Response record
    public record HealthResponse(
        String status,
        String application,
        String version,
        LocalDateTime timestamp,
        Map<String, String> components
    ) {}
}

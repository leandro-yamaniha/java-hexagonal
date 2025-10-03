package com.restaurant.quarkus.controller;

import com.restaurant.application.port.out.CacheService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check and monitoring endpoints
 */
@Path("/api/health")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Health", description = "Health check and monitoring operations")
public class HealthController {
    
    @Inject
    CacheService cacheService;
    
    @GET
    @Path("/info")
    @Operation(summary = "Get application information")
    @APIResponse(responseCode = "200", description = "Application information")
    public Response getApplicationInfo() {
        Map<String, Object> info = Map.of(
            "application", "Restaurant Management System",
            "version", "1.0.0",
            "framework", "Quarkus",
            "timestamp", LocalDateTime.now(),
            "status", "running"
        );
        return Response.ok(info).build();
    }
    
    @GET
    @Path("/cache/stats")
    @Operation(summary = "Get cache statistics")
    @APIResponse(responseCode = "200", description = "Cache statistics")
    public Response getCacheStats() {
        try {
            CacheService.CacheStats stats = cacheService.getStats();
            return Response.ok(stats).build();
        } catch (Exception e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(Map.of("error", "Cache unavailable: " + e.getMessage()))
                .build();
        }
    }
    
    @Liveness
    public static class LivenessCheck implements HealthCheck {
        @Override
        public HealthCheckResponse call() {
            return HealthCheckResponse.up("Restaurant Management API is alive");
        }
    }
    
    @Readiness
    public static class ReadinessCheck implements HealthCheck {
        @Override
        public HealthCheckResponse call() {
            // Add checks for database, cache, etc.
            return HealthCheckResponse.up("Restaurant Management API is ready");
        }
    }
}

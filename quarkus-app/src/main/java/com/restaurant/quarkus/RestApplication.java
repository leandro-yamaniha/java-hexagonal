package com.restaurant.quarkus;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS Application configuration for Quarkus
 */
@ApplicationPath("/")
@OpenAPIDefinition(
    info = @Info(
        title = "Restaurant Management API - Quarkus",
        version = "1.0.0",
        description = "REST API for restaurant management using Quarkus and hexagonal architecture",
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
        @Server(url = "http://localhost:8081", description = "Development server")
    }
)
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Explicitly register our controllers
        classes.add(com.restaurant.quarkus.controller.CustomerController.class);
        classes.add(com.restaurant.quarkus.controller.MenuItemController.class);
        classes.add(com.restaurant.quarkus.controller.OpenApiController.class);
        return classes;
    }
}

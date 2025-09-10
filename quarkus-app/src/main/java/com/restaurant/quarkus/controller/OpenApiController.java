package com.restaurant.quarkus.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Controller to expose OpenAPI documentation manually
 */
@Path("/test")
public class OpenApiController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTestEndpoint() {
        return "OpenAPI Controller is working!";
    }
}

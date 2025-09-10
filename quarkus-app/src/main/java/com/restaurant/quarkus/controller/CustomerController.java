package com.restaurant.quarkus.controller;

import com.restaurant.application.port.in.CustomerUseCase;
import com.restaurant.domain.entity.Customer;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for customer operations
 */
@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customers", description = "Customer management operations")
public class CustomerController {
    
    @Inject
    CustomerUseCase customerUseCase;
    
    @POST
    @Operation(summary = "Create a new customer")
    @APIResponse(responseCode = "201", description = "Customer created successfully")
    @APIResponse(responseCode = "400", description = "Invalid customer data")
    public Response createCustomer(@Valid CreateCustomerRequest request) {
        try {
            CustomerUseCase.CreateCustomerCommand command = new CustomerUseCase.CreateCustomerCommand(
                request.name(), request.email(), request.phone(), request.address()
            );
            Customer customer = customerUseCase.createCustomer(command);
            return Response.status(Response.Status.CREATED).entity(customer).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @GET
    @Operation(summary = "Get all customers")
    @APIResponse(responseCode = "200", description = "List of customers")
    public Response getAllCustomers(@QueryParam("active") @DefaultValue("true") boolean activeOnly) {
        List<Customer> customers = activeOnly ? 
            customerUseCase.getAllActiveCustomers() : 
            customerUseCase.getAllActiveCustomers(); // For now, always return active
        return Response.ok(customers).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get customer by ID")
    @APIResponse(responseCode = "200", description = "Customer found")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response getCustomerById(@Parameter(description = "Customer ID") @PathParam("id") UUID customerId) {
        Optional<Customer> customer = customerUseCase.findCustomerById(customerId);
        return customer.map(c -> Response.ok(c).build())
            .orElse(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("Customer not found")).build());
    }
    
    @GET
    @Path("/email/{email}")
    @Operation(summary = "Get customer by email")
    @APIResponse(responseCode = "200", description = "Customer found")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response getCustomerByEmail(@Parameter(description = "Customer email") @PathParam("email") String email) {
        Optional<Customer> customer = customerUseCase.findCustomerByEmail(email);
        return customer.map(c -> Response.ok(c).build())
            .orElse(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("Customer not found")).build());
    }
    
    @GET
    @Path("/search")
    @Operation(summary = "Search customers by name")
    @APIResponse(responseCode = "200", description = "List of matching customers")
    public Response searchCustomers(@QueryParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Name parameter is required")).build();
        }
        List<Customer> customers = customerUseCase.searchCustomersByName(name.trim());
        return Response.ok(customers).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update customer")
    @APIResponse(responseCode = "200", description = "Customer updated successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    @APIResponse(responseCode = "400", description = "Invalid customer data")
    public Response updateCustomer(@PathParam("id") UUID customerId, @Valid UpdateCustomerRequest request) {
        try {
            CustomerUseCase.UpdateCustomerCommand command = new CustomerUseCase.UpdateCustomerCommand(
                customerId, request.name(), request.email(), request.phone(), request.address()
            );
            Customer customer = customerUseCase.updateCustomer(command);
            return Response.ok(customer).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PUT
    @Path("/{id}/deactivate")
    @Operation(summary = "Deactivate customer")
    @APIResponse(responseCode = "204", description = "Customer deactivated successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response deactivateCustomer(@PathParam("id") UUID customerId) {
        try {
            customerUseCase.deactivateCustomer(customerId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PUT
    @Path("/{id}/activate")
    @Operation(summary = "Activate customer")
    @APIResponse(responseCode = "204", description = "Customer activated successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response activateCustomer(@PathParam("id") UUID customerId) {
        try {
            customerUseCase.activateCustomer(customerId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete customer")
    @APIResponse(responseCode = "204", description = "Customer deleted successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response deleteCustomer(@PathParam("id") UUID customerId) {
        try {
            customerUseCase.deleteCustomer(customerId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    // DTOs
    public record CreateCustomerRequest(
        String name,
        String email,
        String phone,
        String address
    ) {}
    
    public record UpdateCustomerRequest(
        String name,
        String email,
        String phone,
        String address
    ) {}
    
    public record ErrorResponse(String message) {}
    
    // OpenAPI Documentation endpoints
    @GET
    @Path("/docs/openapi")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get OpenAPI specification")
    public Response getOpenApiSpec() {
        String openApiJson = """
            {
              "openapi": "3.0.3",
              "info": {
                "title": "Restaurant Management API - Quarkus",
                "description": "REST API for restaurant management using Quarkus and hexagonal architecture",
                "version": "1.0.0"
              },
              "servers": [
                {
                  "url": "http://localhost:8081",
                  "description": "Development server"
                }
              ],
              "paths": {
                "/api/v1/customers": {
                  "get": {
                    "tags": ["Customers"],
                    "summary": "Get all customers",
                    "parameters": [
                      {
                        "name": "active",
                        "in": "query",
                        "schema": {"type": "boolean", "default": true}
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "List of customers",
                        "content": {
                          "application/json": {
                            "schema": {
                              "type": "array",
                              "items": {"$ref": "#/components/schemas/Customer"}
                            }
                          }
                        }
                      }
                    }
                  },
                  "post": {
                    "tags": ["Customers"],
                    "summary": "Create a new customer",
                    "requestBody": {
                      "required": true,
                      "content": {
                        "application/json": {
                          "schema": {"$ref": "#/components/schemas/CreateCustomerRequest"}
                        }
                      }
                    },
                    "responses": {
                      "201": {"description": "Customer created successfully"},
                      "400": {"description": "Invalid customer data"}
                    }
                  }
                }
              },
              "components": {
                "schemas": {
                  "Customer": {
                    "type": "object",
                    "properties": {
                      "id": {"type": "string", "format": "uuid"},
                      "name": {"type": "string"},
                      "email": {"type": "string"},
                      "phone": {"type": "string"},
                      "address": {"type": "string"},
                      "active": {"type": "boolean"},
                      "createdAt": {"type": "string", "format": "date-time"},
                      "updatedAt": {"type": "string", "format": "date-time"}
                    }
                  },
                  "CreateCustomerRequest": {
                    "type": "object",
                    "required": ["name", "email"],
                    "properties": {
                      "name": {"type": "string"},
                      "email": {"type": "string"},
                      "phone": {"type": "string"},
                      "address": {"type": "string"}
                    }
                  }
                }
              }
            }
            """;
        return Response.ok(openApiJson).build();
    }
    
    @GET
    @Path("/../../swagger-ui")
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Swagger UI")
    public Response getSwaggerUI() {
        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Restaurant Management API - Swagger UI</title>
                <link rel="stylesheet" type="text/css" href="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui.css" />
                <style>
                    html { box-sizing: border-box; overflow: -moz-scrollbars-vertical; overflow-y: scroll; }
                    *, *:before, *:after { box-sizing: inherit; }
                    body { margin:0; background: #fafafa; }
                </style>
            </head>
            <body>
                <div id="swagger-ui"></div>
                <script src="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui-bundle.js"></script>
                <script src="https://unpkg.com/swagger-ui-dist@4.15.5/swagger-ui-standalone-preset.js"></script>
                <script>
                window.onload = function() {
                  const ui = SwaggerUIBundle({
                    url: '/openapi',
                    dom_id: '#swagger-ui',
                    deepLinking: true,
                    presets: [
                      SwaggerUIBundle.presets.apis,
                      SwaggerUIStandalonePreset
                    ],
                    plugins: [
                      SwaggerUIBundle.plugins.DownloadUrl
                    ],
                    layout: "StandaloneLayout"
                  });
                };
                </script>
            </body>
            </html>
            """;
        return Response.ok(html).build();
    }
}

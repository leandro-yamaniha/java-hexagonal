package com.restaurant.quarkus.controller;

import com.restaurant.application.port.in.MenuUseCase;
import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.domain.valueobject.Money;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for menu operations
 */
@Path("/api/menu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Menu", description = "Menu management operations")
public class MenuController {
    
    @Inject
    MenuUseCase menuUseCase;
    
    @POST
    @Operation(summary = "Create a new menu item")
    @APIResponse(responseCode = "201", description = "Menu item created successfully")
    @APIResponse(responseCode = "400", description = "Invalid menu item data")
    public Response createMenuItem(@Valid CreateMenuItemRequest request) {
        try {
            MenuUseCase.CreateMenuItemCommand command = new MenuUseCase.CreateMenuItemCommand(
                request.name(),
                request.description(),
                new Money(request.price(), request.currency()),
                MenuCategory.valueOf(request.category()),
                request.imageUrl(),
                request.preparationTimeMinutes()
            );
            MenuItem menuItem = menuUseCase.createMenuItem(command);
            return Response.status(Response.Status.CREATED).entity(menuItem).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @GET
    @Operation(summary = "Get all menu items")
    @APIResponse(responseCode = "200", description = "List of menu items")
    public Response getAllMenuItems(@QueryParam("available") @DefaultValue("false") boolean availableOnly) {
        List<MenuItem> menuItems = availableOnly ? 
            menuUseCase.getAvailableMenuItems() : 
            menuUseCase.getAllMenuItems();
        return Response.ok(menuItems).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get menu item by ID")
    @APIResponse(responseCode = "200", description = "Menu item found")
    @APIResponse(responseCode = "404", description = "Menu item not found")
    public Response getMenuItemById(@Parameter(description = "Menu item ID") @PathParam("id") UUID menuItemId) {
        Optional<MenuItem> menuItem = menuUseCase.findMenuItemById(menuItemId);
        return menuItem.map(item -> Response.ok(item).build())
            .orElse(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("Menu item not found")).build());
    }
    
    @GET
    @Path("/category/{category}")
    @Operation(summary = "Get menu items by category")
    @APIResponse(responseCode = "200", description = "List of menu items in category")
    @APIResponse(responseCode = "400", description = "Invalid category")
    public Response getMenuItemsByCategory(
            @Parameter(description = "Menu category") @PathParam("category") String category,
            @QueryParam("available") @DefaultValue("false") boolean availableOnly) {
        try {
            MenuCategory menuCategory = MenuCategory.valueOf(category.toUpperCase());
            List<MenuItem> menuItems = availableOnly ?
                menuUseCase.getAvailableMenuItemsByCategory(menuCategory) :
                menuUseCase.getMenuItemsByCategory(menuCategory);
            return Response.ok(menuItems).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Invalid category: " + category)).build();
        }
    }
    
    @GET
    @Path("/categories")
    @Operation(summary = "Get all menu categories")
    @APIResponse(responseCode = "200", description = "List of menu categories")
    public Response getMenuCategories() {
        return Response.ok(MenuCategory.values()).build();
    }
    
    @GET
    @Path("/search")
    @Operation(summary = "Search menu items by name")
    @APIResponse(responseCode = "200", description = "List of matching menu items")
    public Response searchMenuItems(@QueryParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Name parameter is required")).build();
        }
        List<MenuItem> menuItems = menuUseCase.searchMenuItemsByName(name.trim());
        return Response.ok(menuItems).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update menu item")
    @APIResponse(responseCode = "200", description = "Menu item updated successfully")
    @APIResponse(responseCode = "404", description = "Menu item not found")
    @APIResponse(responseCode = "400", description = "Invalid menu item data")
    public Response updateMenuItem(@PathParam("id") UUID menuItemId, @Valid UpdateMenuItemRequest request) {
        try {
            MenuUseCase.UpdateMenuItemCommand command = new MenuUseCase.UpdateMenuItemCommand(
                menuItemId,
                request.name(),
                request.description(),
                new Money(request.price(), request.currency()),
                MenuCategory.valueOf(request.category()),
                request.imageUrl(),
                request.preparationTimeMinutes(),
                request.available()
            );
            MenuItem menuItem = menuUseCase.updateMenuItem(command);
            return Response.ok(menuItem).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PUT
    @Path("/{id}/availability")
    @Operation(summary = "Update menu item availability")
    @APIResponse(responseCode = "204", description = "Availability updated successfully")
    @APIResponse(responseCode = "404", description = "Menu item not found")
    public Response updateMenuItemAvailability(@PathParam("id") UUID menuItemId, 
                                             @Valid UpdateAvailabilityRequest request) {
        try {
            menuUseCase.updateMenuItemAvailability(menuItemId, request.available());
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PUT
    @Path("/{id}/price")
    @Operation(summary = "Update menu item price")
    @APIResponse(responseCode = "204", description = "Price updated successfully")
    @APIResponse(responseCode = "404", description = "Menu item not found")
    public Response updateMenuItemPrice(@PathParam("id") UUID menuItemId, 
                                      @Valid UpdatePriceRequest request) {
        try {
            Money newPrice = new Money(request.price(), request.currency());
            menuUseCase.updateMenuItemPrice(menuItemId, newPrice);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete menu item")
    @APIResponse(responseCode = "204", description = "Menu item deleted successfully")
    @APIResponse(responseCode = "404", description = "Menu item not found")
    public Response deleteMenuItem(@PathParam("id") UUID menuItemId) {
        try {
            menuUseCase.deleteMenuItem(menuItemId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    // DTOs
    public record CreateMenuItemRequest(
        String name,
        String description,
        BigDecimal price,
        String currency,
        String category,
        String imageUrl,
        int preparationTimeMinutes
    ) {}
    
    public record UpdateMenuItemRequest(
        String name,
        String description,
        BigDecimal price,
        String currency,
        String category,
        String imageUrl,
        int preparationTimeMinutes,
        boolean available
    ) {}
    
    public record UpdateAvailabilityRequest(boolean available) {}
    
    public record UpdatePriceRequest(BigDecimal price, String currency) {}
    
    public record ErrorResponse(String message) {}
}

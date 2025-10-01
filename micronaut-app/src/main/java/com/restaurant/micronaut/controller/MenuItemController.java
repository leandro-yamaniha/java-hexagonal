package com.restaurant.micronaut.controller;

import com.restaurant.application.port.in.MenuUseCase;
import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.domain.valueobject.Money;
import com.restaurant.micronaut.dto.MenuItemDTO;
import com.restaurant.micronaut.mapper.MenuItemDTOMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Micronaut REST controller for menu item operations
 */
@Controller("/api/v1/menu-items")
@Tag(name = "Menu Items", description = "Menu item management operations")
public class MenuItemController {
    
    @Inject
    private MenuUseCase menuUseCase;
    
    @Inject
    private MenuItemDTOMapper menuItemMapper;
    
    @Post
    @Operation(summary = "Create a new menu item", description = "Create a new menu item in the restaurant")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Menu item created successfully", 
                    content = @Content(schema = @Schema(implementation = MenuItemDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public HttpResponse<MenuItemDTO> createMenuItem(@Body CreateMenuItemRequest request) {
        try {
            Money price = new Money(request.price(), "USD");
            MenuCategory category = MenuCategory.valueOf(request.category());
            
            MenuUseCase.CreateMenuItemCommand command = new MenuUseCase.CreateMenuItemCommand(
                request.name(), 
                request.description(), 
                price, 
                category, 
                request.imageUrl(),
                request.preparationTimeMinutes()
            );
            MenuItem menuItem = menuUseCase.createMenuItem(command);
            MenuItemDTO dto = menuItemMapper.toDTO(menuItem);
            return HttpResponse.created(dto);
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest();
        }
    }
    
    @Get
    @Operation(summary = "Get all menu items", description = "Retrieve a list of all menu items")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of menu items", 
                    content = @Content(schema = @Schema(implementation = MenuItemDTO.class)))
    })
    public HttpResponse<List<MenuItemDTO>> getAllMenuItems(
            @Parameter(description = "Filter by availability") 
            @QueryValue(defaultValue = "false") boolean availableOnly) {
        List<MenuItem> menuItems = availableOnly ? 
            menuUseCase.getAvailableMenuItems() : 
            menuUseCase.getAllMenuItems();
        List<MenuItemDTO> dtos = menuItems.stream()
            .map(menuItemMapper::toDTO)
            .collect(Collectors.toList());
        return HttpResponse.ok(dtos);
    }
    
    @Get("/{id}")
    @Operation(summary = "Get menu item by ID", description = "Retrieve a specific menu item by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Menu item found", 
                    content = @Content(schema = @Schema(implementation = MenuItemDTO.class))),
        @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public HttpResponse<MenuItemDTO> getMenuItemById(
            @Parameter(description = "Menu item ID") 
            @PathVariable UUID id) {
        return menuUseCase.findMenuItemById(id)
            .map(menuItemMapper::toDTO)
            .map(HttpResponse::ok)
            .orElse(HttpResponse.notFound());
    }
    
    @Get("/available")
    @Operation(summary = "Get available menu items", description = "Retrieve only available menu items")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of available menu items", 
                    content = @Content(schema = @Schema(implementation = MenuItemDTO.class)))
    })
    public HttpResponse<List<MenuItemDTO>> getAvailableMenuItems() {
        List<MenuItem> menuItems = menuUseCase.getAvailableMenuItems();
        List<MenuItemDTO> dtos = menuItems.stream()
            .map(menuItemMapper::toDTO)
            .collect(Collectors.toList());
        return HttpResponse.ok(dtos);
    }
    
    @Get("/by-category")
    @Operation(summary = "Get menu items by category", description = "Retrieve menu items filtered by category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of menu items in category", 
                    content = @Content(schema = @Schema(implementation = MenuItemDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid category")
    })
    public HttpResponse<List<MenuItemDTO>> getMenuItemsByCategory(
            @Parameter(description = "Category name") 
            @QueryValue String category) {
        if (category == null || category.trim().isEmpty()) {
            return HttpResponse.badRequest();
        }
        MenuCategory menuCategory = MenuCategory.valueOf(category.trim().toUpperCase());
        List<MenuItem> menuItems = menuUseCase.getMenuItemsByCategory(menuCategory);
        List<MenuItemDTO> dtos = menuItems.stream()
            .map(menuItemMapper::toDTO)
            .collect(Collectors.toList());
        return HttpResponse.ok(dtos);
    }
    
    @Put("/{id}")
    @Operation(summary = "Update menu item", description = "Update an existing menu item")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Menu item updated successfully", 
                    content = @Content(schema = @Schema(implementation = MenuItemDTO.class))),
        @ApiResponse(responseCode = "404", description = "Menu item not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public HttpResponse<MenuItemDTO> updateMenuItem(
            @Parameter(description = "Menu item ID") 
            @PathVariable UUID id, 
            @Body UpdateMenuItemRequest request) {
        try {
            Money price = new Money(request.price(), "USD");
            MenuCategory category = MenuCategory.valueOf(request.category());
            
            MenuUseCase.UpdateMenuItemCommand command = new MenuUseCase.UpdateMenuItemCommand(
                id, 
                request.name(), 
                request.description(), 
                price, 
                category, 
                request.imageUrl(),
                request.preparationTimeMinutes(),
                request.available()
            );
            MenuItem menuItem = menuUseCase.updateMenuItem(command);
            MenuItemDTO dto = menuItemMapper.toDTO(menuItem);
            return HttpResponse.ok(dto);
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }
    
    @Put("/{id}/availability")
    @Operation(summary = "Update menu item availability", description = "Update the availability status of a menu item")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
        @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public HttpResponse<Void> updateMenuItemAvailability(
            @Parameter(description = "Menu item ID") 
            @PathVariable UUID id, 
            @Parameter(description = "Availability status") 
            @QueryValue boolean available) {
        try {
            menuUseCase.updateMenuItemAvailability(id, available);
            return HttpResponse.ok();
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }
    
    @Delete("/{id}")
    @Operation(summary = "Delete menu item", description = "Delete a menu item from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    public HttpResponse<Void> deleteMenuItem(
            @Parameter(description = "Menu item ID") 
            @PathVariable UUID id) {
        try {
            menuUseCase.deleteMenuItem(id);
            return HttpResponse.noContent();
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }
    
    // Request records
    public record CreateMenuItemRequest(String name, String description, BigDecimal price, String category, String imageUrl, int preparationTimeMinutes) {}
    public record UpdateMenuItemRequest(String name, String description, BigDecimal price, String category, String imageUrl, int preparationTimeMinutes, boolean available) {}
}

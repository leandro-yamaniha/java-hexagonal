package com.restaurant.springboot.controller;

import com.restaurant.application.port.in.MenuUseCase;
import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.domain.valueobject.Money;
import com.restaurant.springboot.dto.MenuItemDTO;
import com.restaurant.springboot.mapper.MenuItemDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Spring Boot REST controller for menu item operations
 */
@RestController
@RequestMapping("/api/v1/menu-items")
@Tag(name = "Menu Items", description = "Menu item management operations")
public class MenuItemController {
    
    @Autowired
    private MenuUseCase menuUseCase;
    
    @Autowired
    private MenuItemDTOMapper menuItemMapper;
    
    @PostMapping
    @Operation(summary = "Create a new menu item")
    @ApiResponse(responseCode = "201", description = "Menu item created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid menu item data")
    public ResponseEntity<?> createMenuItem(@Valid @RequestBody CreateMenuItemRequest request) {
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
            MenuItemDTO dto = menuItemMapper.toDTO(menuItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all menu items")
    @ApiResponse(responseCode = "200", description = "List of menu items")
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<MenuItem> menuItems = menuUseCase.getAllMenuItems();
        List<MenuItemDTO> dtos = menuItems.stream()
            .map(menuItemMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID")
    @ApiResponse(responseCode = "200", description = "Menu item found")
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    public ResponseEntity<?> getMenuItemById(
            @Parameter(description = "Menu item ID") @PathVariable UUID id) {
        Optional<MenuItem> menuItem = menuUseCase.findMenuItemById(id);
        return menuItem.map(item -> ResponseEntity.ok(menuItemMapper.toDTO(item)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-category")
    @Operation(summary = "Get menu items by category")
    @ApiResponse(responseCode = "200", description = "List of menu items in category")
    public ResponseEntity<?> getMenuItemsByCategory(@RequestParam String category) {
        try {
            MenuCategory menuCategory = MenuCategory.valueOf(category.toUpperCase());
            List<MenuItem> menuItems = menuUseCase.getMenuItemsByCategory(menuCategory);
            List<MenuItemDTO> dtos = menuItems.stream()
                .map(menuItemMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid category: " + category));
        }
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available menu items")
    @ApiResponse(responseCode = "200", description = "List of available menu items")
    public ResponseEntity<List<MenuItemDTO>> getAvailableMenuItems() {
        List<MenuItem> menuItems = menuUseCase.getAvailableMenuItems();
        List<MenuItemDTO> dtos = menuItems.stream()
            .map(menuItemMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search menu items by name")
    @ApiResponse(responseCode = "200", description = "List of matching menu items")
    public ResponseEntity<?> searchMenuItems(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Name parameter is required"));
        }
        List<MenuItem> menuItems = menuUseCase.searchMenuItemsByName(name.trim());
        List<MenuItemDTO> dtos = menuItems.stream()
            .map(menuItemMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update menu item")
    @ApiResponse(responseCode = "200", description = "Menu item updated successfully")
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    @ApiResponse(responseCode = "400", description = "Invalid menu item data")
    public ResponseEntity<?> updateMenuItem(@PathVariable UUID id, 
                                          @Valid @RequestBody UpdateMenuItemRequest request) {
        try {
            MenuUseCase.UpdateMenuItemCommand command = new MenuUseCase.UpdateMenuItemCommand(
                id,
                request.name(),
                request.description(),
                new Money(request.price(), request.currency()),
                MenuCategory.valueOf(request.category()),
                request.imageUrl(),
                request.preparationTimeMinutes(),
                request.available()
            );
            MenuItem menuItem = menuUseCase.updateMenuItem(command);
            MenuItemDTO dto = menuItemMapper.toDTO(menuItem);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/make-available")
    @Operation(summary = "Make menu item available")
    @ApiResponse(responseCode = "200", description = "Menu item made available")
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    public ResponseEntity<?> makeMenuItemAvailable(@PathVariable UUID id) {
        try {
            menuUseCase.updateMenuItemAvailability(id, true);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/make-unavailable")
    @Operation(summary = "Make menu item unavailable")
    @ApiResponse(responseCode = "200", description = "Menu item made unavailable")
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    public ResponseEntity<?> makeMenuItemUnavailable(@PathVariable UUID id) {
        try {
            menuUseCase.updateMenuItemAvailability(id, false);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu item")
    @ApiResponse(responseCode = "204", description = "Menu item deleted successfully")
    @ApiResponse(responseCode = "404", description = "Menu item not found")
    public ResponseEntity<?> deleteMenuItem(@PathVariable UUID id) {
        try {
            menuUseCase.deleteMenuItem(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
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
        Integer preparationTimeMinutes
    ) {}
    
    public record UpdateMenuItemRequest(
        String name,
        String description,
        BigDecimal price,
        String currency,
        String category,
        String imageUrl,
        Integer preparationTimeMinutes,
        Boolean available
    ) {}
    
    public record ErrorResponse(String message) {}
}

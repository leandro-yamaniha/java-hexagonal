package com.restaurant.application.port.in;

import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.domain.valueobject.Money;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for menu-related use cases
 */
public interface MenuUseCase {
    
    /**
     * Create a new menu item
     */
    MenuItem createMenuItem(CreateMenuItemCommand command);
    
    /**
     * Update an existing menu item
     */
    MenuItem updateMenuItem(UpdateMenuItemCommand command);
    
    /**
     * Find menu item by ID
     */
    Optional<MenuItem> findMenuItemById(UUID menuItemId);
    
    /**
     * Get all menu items
     */
    List<MenuItem> getAllMenuItems();
    
    /**
     * Get all available menu items
     */
    List<MenuItem> getAvailableMenuItems();
    
    /**
     * Get menu items by category
     */
    List<MenuItem> getMenuItemsByCategory(MenuCategory category);
    
    /**
     * Get available menu items by category
     */
    List<MenuItem> getAvailableMenuItemsByCategory(MenuCategory category);
    
    /**
     * Search menu items by name
     */
    List<MenuItem> searchMenuItemsByName(String name);
    
    /**
     * Update menu item availability
     */
    void updateMenuItemAvailability(UUID menuItemId, boolean available);
    
    /**
     * Update menu item price
     */
    void updateMenuItemPrice(UUID menuItemId, Money newPrice);
    
    /**
     * Delete a menu item
     */
    void deleteMenuItem(UUID menuItemId);
    
    /**
     * Command for creating a menu item
     */
    record CreateMenuItemCommand(
        String name,
        String description,
        Money price,
        MenuCategory category,
        String imageUrl,
        int preparationTimeMinutes
    ) {}
    
    /**
     * Command for updating a menu item
     */
    record UpdateMenuItemCommand(
        UUID menuItemId,
        String name,
        String description,
        Money price,
        MenuCategory category,
        String imageUrl,
        int preparationTimeMinutes,
        boolean available
    ) {}
}

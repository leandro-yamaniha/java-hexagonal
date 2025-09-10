package com.restaurant.application.port.out;

import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for menu item data persistence
 */
public interface MenuItemRepository {
    
    /**
     * Save a menu item
     */
    MenuItem save(MenuItem menuItem);
    
    /**
     * Find menu item by ID
     */
    Optional<MenuItem> findById(UUID menuItemId);
    
    /**
     * Find all menu items
     */
    List<MenuItem> findAll();
    
    /**
     * Find all available menu items
     */
    List<MenuItem> findAllAvailable();
    
    /**
     * Find menu items by category
     */
    List<MenuItem> findByCategory(MenuCategory category);
    
    /**
     * Find available menu items by category
     */
    List<MenuItem> findAvailableByCategory(MenuCategory category);
    
    /**
     * Search menu items by name (case-insensitive)
     */
    List<MenuItem> searchByName(String name);
    
    /**
     * Check if menu item exists by name
     */
    boolean existsByName(String name);
    
    /**
     * Delete menu item by ID
     */
    void deleteById(UUID menuItemId);
    
    /**
     * Count total menu items
     */
    long count();
    
    /**
     * Count available menu items
     */
    long countAvailable();
    
    /**
     * Count menu items by category
     */
    long countByCategory(MenuCategory category);
}

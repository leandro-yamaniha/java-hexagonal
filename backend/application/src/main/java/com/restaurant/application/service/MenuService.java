package com.restaurant.application.service;

import com.restaurant.application.port.in.MenuUseCase;
import com.restaurant.application.port.out.CacheService;
import com.restaurant.application.port.out.MenuItemRepository;
import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.domain.valueobject.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Menu service implementation
 */
public class MenuService implements MenuUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);
    private static final String CACHE_PREFIX = "menu:";
    private static final String CACHE_CATEGORY_PREFIX = "menu:category:";
    private static final Duration CACHE_DURATION = Duration.ofHours(1);
    
    private final MenuItemRepository menuItemRepository;
    private final CacheService cacheService;
    
    public MenuService(MenuItemRepository menuItemRepository, CacheService cacheService) {
        this.menuItemRepository = menuItemRepository;
        this.cacheService = cacheService;
    }
    
    @Override
    public MenuItem createMenuItem(CreateMenuItemCommand command) {
        logger.info("Creating menu item: {}", command.name());
        
        if (menuItemRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Menu item with name already exists: " + command.name());
        }
        
        MenuItem menuItem = new MenuItem(
            command.name(),
            command.description(),
            command.price(),
            command.category()
        );
        
        if (command.imageUrl() != null) {
            menuItem.setImageUrl(command.imageUrl());
        }
        
        if (command.preparationTimeMinutes() > 0) {
            menuItem.setPreparationTime(command.preparationTimeMinutes());
        }
        
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        
        // Cache the menu item
        cacheService.put(CACHE_PREFIX + savedMenuItem.getId(), savedMenuItem, CACHE_DURATION);
        
        // Invalidate category cache
        cacheService.evictPattern(CACHE_CATEGORY_PREFIX + command.category().name() + "*");
        
        logger.info("Menu item created successfully with ID: {}", savedMenuItem.getId());
        return savedMenuItem;
    }
    
    @Override
    public MenuItem updateMenuItem(UpdateMenuItemCommand command) {
        logger.info("Updating menu item with ID: {}", command.menuItemId());
        
        MenuItem menuItem = findMenuItemById(command.menuItemId())
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + command.menuItemId()));
        
        // Check if name is being changed and if new name already exists
        if (!menuItem.getName().equals(command.name()) && 
            menuItemRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Menu item with name already exists: " + command.name());
        }
        
        MenuCategory oldCategory = menuItem.getCategory();
        
        menuItem.updateDetails(command.name(), command.description(), command.price(), command.category());
        menuItem.setImageUrl(command.imageUrl());
        menuItem.setPreparationTime(command.preparationTimeMinutes());
        
        if (command.available()) {
            menuItem.makeAvailable();
        } else {
            menuItem.makeUnavailable();
        }
        
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        
        // Update cache
        cacheService.evict(CACHE_PREFIX + menuItem.getId());
        cacheService.put(CACHE_PREFIX + updatedMenuItem.getId(), updatedMenuItem, CACHE_DURATION);
        
        // Invalidate category caches
        cacheService.evictPattern(CACHE_CATEGORY_PREFIX + oldCategory.name() + "*");
        cacheService.evictPattern(CACHE_CATEGORY_PREFIX + command.category().name() + "*");
        
        logger.info("Menu item updated successfully: {}", updatedMenuItem.getId());
        return updatedMenuItem;
    }
    
    @Override
    public Optional<MenuItem> findMenuItemById(UUID menuItemId) {
        logger.debug("Finding menu item by ID: {}", menuItemId);
        
        // Try cache first
        Optional<MenuItem> cachedMenuItem = cacheService.get(CACHE_PREFIX + menuItemId, MenuItem.class);
        if (cachedMenuItem.isPresent()) {
            logger.debug("Menu item found in cache: {}", menuItemId);
            return cachedMenuItem;
        }
        
        // Fallback to repository
        Optional<MenuItem> menuItem = menuItemRepository.findById(menuItemId);
        if (menuItem.isPresent()) {
            cacheService.put(CACHE_PREFIX + menuItemId, menuItem.get(), CACHE_DURATION);
            logger.debug("Menu item found in repository and cached: {}", menuItemId);
        }
        
        return menuItem;
    }
    
    @Override
    public List<MenuItem> getAllMenuItems() {
        logger.debug("Getting all menu items");
        return menuItemRepository.findAll();
    }
    
    @Override
    public List<MenuItem> getAvailableMenuItems() {
        logger.debug("Getting available menu items");
        
        String cacheKey = CACHE_PREFIX + "available";
        @SuppressWarnings("unchecked")
        Optional<List<MenuItem>> cached = cacheService.get(cacheKey, (Class<List<MenuItem>>) (Class<?>) List.class);
        if (cached.isPresent()) {
            return cached.get();
        }
        
        List<MenuItem> items = menuItemRepository.findAllAvailable();
        cacheService.put(cacheKey, items, Duration.ofMinutes(15));
        return items;
    }
    
    @Override
    public List<MenuItem> getMenuItemsByCategory(MenuCategory category) {
        logger.debug("Getting menu items by category: {}", category);
        
        String cacheKey = CACHE_CATEGORY_PREFIX + category.name();
        @SuppressWarnings("unchecked")
        Optional<List<MenuItem>> cached = cacheService.get(cacheKey, (Class<List<MenuItem>>) (Class<?>) List.class);
        if (cached.isPresent()) {
            return cached.get();
        }
        
        List<MenuItem> items = menuItemRepository.findByCategory(category);
        cacheService.put(cacheKey, items, Duration.ofMinutes(30));
        return items;
    }
    
    @Override
    public List<MenuItem> getAvailableMenuItemsByCategory(MenuCategory category) {
        logger.debug("Getting available menu items by category: {}", category);
        
        String cacheKey = CACHE_CATEGORY_PREFIX + category.name() + ":available";
        @SuppressWarnings("unchecked")
        Optional<List<MenuItem>> cached = cacheService.get(cacheKey, (Class<List<MenuItem>>) (Class<?>) List.class);
        if (cached.isPresent()) {
            return cached.get();
        }
        
        List<MenuItem> items = menuItemRepository.findAvailableByCategory(category);
        cacheService.put(cacheKey, items, Duration.ofMinutes(15));
        return items;
    }
    
    @Override
    public List<MenuItem> searchMenuItemsByName(String name) {
        logger.debug("Searching menu items by name: {}", name);
        return menuItemRepository.searchByName(name);
    }
    
    @Override
    public void updateMenuItemAvailability(UUID menuItemId, boolean available) {
        logger.info("Updating menu item availability: {} to {}", menuItemId, available);
        
        MenuItem menuItem = findMenuItemById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + menuItemId));
        
        if (available) {
            menuItem.makeAvailable();
        } else {
            menuItem.makeUnavailable();
        }
        
        menuItemRepository.save(menuItem);
        
        // Update cache
        cacheService.evict(CACHE_PREFIX + menuItemId);
        cacheService.evictPattern(CACHE_CATEGORY_PREFIX + menuItem.getCategory().name() + "*");
        cacheService.evict(CACHE_PREFIX + "available");
        
        logger.info("Menu item availability updated successfully: {}", menuItemId);
    }
    
    @Override
    public void updateMenuItemPrice(UUID menuItemId, Money newPrice) {
        logger.info("Updating menu item price: {} to {}", menuItemId, newPrice);
        
        MenuItem menuItem = findMenuItemById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + menuItemId));
        
        menuItem.updatePrice(newPrice);
        menuItemRepository.save(menuItem);
        
        // Update cache
        cacheService.evict(CACHE_PREFIX + menuItemId);
        cacheService.evictPattern(CACHE_CATEGORY_PREFIX + menuItem.getCategory().name() + "*");
        
        logger.info("Menu item price updated successfully: {}", menuItemId);
    }
    
    @Override
    public void deleteMenuItem(UUID menuItemId) {
        logger.info("Deleting menu item: {}", menuItemId);
        
        MenuItem menuItem = findMenuItemById(menuItemId)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + menuItemId));
        
        menuItemRepository.deleteById(menuItemId);
        
        // Remove from cache
        cacheService.evict(CACHE_PREFIX + menuItemId);
        cacheService.evictPattern(CACHE_CATEGORY_PREFIX + menuItem.getCategory().name() + "*");
        cacheService.evict(CACHE_PREFIX + "available");
        
        logger.info("Menu item deleted successfully: {}", menuItemId);
    }
}

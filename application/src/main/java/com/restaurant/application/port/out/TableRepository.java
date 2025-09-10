package com.restaurant.application.port.out;

import com.restaurant.domain.entity.RestaurantTable;
import com.restaurant.domain.valueobject.TableStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for table data persistence
 */
public interface TableRepository {
    
    /**
     * Save a table
     */
    RestaurantTable save(RestaurantTable table);
    
    /**
     * Find table by ID
     */
    Optional<RestaurantTable> findById(UUID tableId);
    
    /**
     * Find table by number
     */
    Optional<RestaurantTable> findByTableNumber(String tableNumber);
    
    /**
     * Find all tables
     */
    List<RestaurantTable> findAll();
    
    /**
     * Find tables by status
     */
    List<RestaurantTable> findByStatus(TableStatus status);
    
    /**
     * Find available tables
     */
    List<RestaurantTable> findAvailableTables();
    
    /**
     * Find available tables with minimum capacity
     */
    List<RestaurantTable> findAvailableTablesWithCapacity(int minCapacity);
    
    /**
     * Find tables by location
     */
    List<RestaurantTable> findByLocation(String location);
    
    /**
     * Check if table number exists
     */
    boolean existsByTableNumber(String tableNumber);
    
    /**
     * Delete table by ID
     */
    void deleteById(UUID tableId);
    
    /**
     * Count total tables
     */
    long count();
    
    /**
     * Count tables by status
     */
    long countByStatus(TableStatus status);
    
    /**
     * Count available tables
     */
    long countAvailable();
}

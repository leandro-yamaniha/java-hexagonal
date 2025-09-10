package com.restaurant.application.port.in;

import com.restaurant.domain.entity.RestaurantTable;
import com.restaurant.domain.valueobject.TableStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for table-related use cases
 */
public interface TableUseCase {
    
    /**
     * Create a new table
     */
    RestaurantTable createTable(CreateTableCommand command);
    
    /**
     * Update table information
     */
    RestaurantTable updateTable(UpdateTableCommand command);
    
    /**
     * Find table by ID
     */
    Optional<RestaurantTable> findTableById(UUID tableId);
    
    /**
     * Find table by number
     */
    Optional<RestaurantTable> findTableByNumber(String tableNumber);
    
    /**
     * Get all tables
     */
    List<RestaurantTable> getAllTables();
    
    /**
     * Get available tables
     */
    List<RestaurantTable> getAvailableTables();
    
    /**
     * Get tables by status
     */
    List<RestaurantTable> getTablesByStatus(TableStatus status);
    
    /**
     * Find available table for party size
     */
    Optional<RestaurantTable> findAvailableTableForPartySize(int partySize);
    
    /**
     * Occupy a table
     */
    RestaurantTable occupyTable(UUID tableId, UUID orderId);
    
    /**
     * Reserve a table
     */
    RestaurantTable reserveTable(UUID tableId);
    
    /**
     * Make table available
     */
    RestaurantTable makeTableAvailable(UUID tableId);
    
    /**
     * Mark table for cleaning
     */
    RestaurantTable markTableForCleaning(UUID tableId);
    
    /**
     * Mark table out of service
     */
    RestaurantTable markTableOutOfService(UUID tableId);
    
    /**
     * Delete a table
     */
    void deleteTable(UUID tableId);
    
    /**
     * Command for creating a table
     */
    record CreateTableCommand(
        String tableNumber,
        int capacity,
        String location
    ) {}
    
    /**
     * Command for updating a table
     */
    record UpdateTableCommand(
        UUID tableId,
        String tableNumber,
        int capacity,
        String location
    ) {}
}

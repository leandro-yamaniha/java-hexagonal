package com.restaurant.domain.entity;

import com.restaurant.domain.valueobject.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * RestaurantTable domain entity representing restaurant tables
 */
public class RestaurantTable {
    
    private UUID id;
    
    @NotBlank(message = "Table number is required")
    @Size(min = 1, max = 10, message = "Table number must be between 1 and 10 characters")
    private String tableNumber;
    
    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;
    
    @NotNull(message = "Table status is required")
    private TableStatus status;
    
    @Size(max = 200, message = "Location description cannot exceed 200 characters")
    private String location;
    
    private UUID currentOrderId;
    
    private LocalDateTime lastOccupiedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public RestaurantTable() {
        this.id = UUID.randomUUID();
        this.status = TableStatus.AVAILABLE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public RestaurantTable(String tableNumber, int capacity, String location) {
        this();
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.location = location;
    }
    
    // Business methods
    public void occupy(UUID orderId) {
        if (status != TableStatus.AVAILABLE) {
            throw new IllegalStateException("Table is not available for occupation");
        }
        this.status = TableStatus.OCCUPIED;
        this.currentOrderId = orderId;
        this.lastOccupiedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reserve() {
        if (status != TableStatus.AVAILABLE) {
            throw new IllegalStateException("Table is not available for reservation");
        }
        this.status = TableStatus.RESERVED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void makeAvailable() {
        this.status = TableStatus.AVAILABLE;
        this.currentOrderId = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markForCleaning() {
        if (status == TableStatus.OCCUPIED) {
            throw new IllegalStateException("Cannot mark occupied table for cleaning");
        }
        this.status = TableStatus.CLEANING;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markOutOfService() {
        this.status = TableStatus.OUT_OF_SERVICE;
        this.currentOrderId = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isAvailable() {
        return status == TableStatus.AVAILABLE;
    }
    
    public boolean isOccupied() {
        return status == TableStatus.OCCUPIED;
    }
    
    public boolean canAccommodate(int partySize) {
        return isAvailable() && capacity >= partySize;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getTableNumber() {
        return tableNumber;
    }
    
    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public TableStatus getStatus() {
        return status;
    }
    
    public void setStatus(TableStatus status) {
        this.status = status;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public UUID getCurrentOrderId() {
        return currentOrderId;
    }
    
    public void setCurrentOrderId(UUID currentOrderId) {
        this.currentOrderId = currentOrderId;
    }
    
    public LocalDateTime getLastOccupiedAt() {
        return lastOccupiedAt;
    }
    
    public void setLastOccupiedAt(LocalDateTime lastOccupiedAt) {
        this.lastOccupiedAt = lastOccupiedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTable that = (RestaurantTable) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", tableNumber='" + tableNumber + '\'' +
                ", capacity=" + capacity +
                ", status=" + status +
                ", location='" + location + '\'' +
                '}';
    }
}

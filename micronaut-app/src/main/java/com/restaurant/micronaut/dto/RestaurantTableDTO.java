package com.restaurant.micronaut.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for RestaurantTable entity - used for REST API serialization
 */
public class RestaurantTableDTO {
    
    private UUID id;
    
    @NotBlank(message = "Table number is required")
    @Size(min = 1, max = 10, message = "Table number must be between 1 and 10 characters")
    private String tableNumber;
    
    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;
    
    @NotNull(message = "Table status is required")
    private String status;
    
    @Size(max = 200, message = "Location description cannot exceed 200 characters")
    private String location;
    
    private UUID currentOrderId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastOccupiedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // Constructors
    public RestaurantTableDTO() {
    }

    public RestaurantTableDTO(UUID id, String tableNumber, int capacity, String status,
                             String location, UUID currentOrderId, LocalDateTime lastOccupiedAt,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
        this.location = location;
        this.currentOrderId = currentOrderId;
        this.lastOccupiedAt = lastOccupiedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
}

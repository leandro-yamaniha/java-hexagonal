package com.restaurant.domain.entity;

import com.restaurant.domain.valueobject.Money;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

/**
 * OrderItem domain entity representing individual items within an order
 */
public class OrderItem {
    
    private UUID id;
    
    @NotNull(message = "Menu item ID is required")
    private UUID menuItemId;
    
    @NotBlank(message = "Menu item name is required")
    private String menuItemName;
    
    @NotNull(message = "Unit price is required")
    @Valid
    private Money unitPrice;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    
    @Size(max = 200, message = "Notes cannot exceed 200 characters")
    private String notes;
    
    // Constructors
    public OrderItem() {
        this.id = UUID.randomUUID();
        this.quantity = 1;
    }
    
    public OrderItem(UUID menuItemId, String menuItemName, Money unitPrice, int quantity, String notes) {
        this();
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.notes = notes;
    }
    
    // Business methods
    public Money getSubtotal() {
        return unitPrice.multiply(quantity);
    }
    
    public void updateQuantity(int newQuantity) {
        if (newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = newQuantity;
    }
    
    public void addNotes(String additionalNotes) {
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = additionalNotes;
        } else {
            this.notes += " | " + additionalNotes;
        }
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getMenuItemId() {
        return menuItemId;
    }
    
    public void setMenuItemId(UUID menuItemId) {
        this.menuItemId = menuItemId;
    }
    
    public String getMenuItemName() {
        return menuItemName;
    }
    
    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }
    
    public Money getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", menuItemName='" + menuItemName + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}

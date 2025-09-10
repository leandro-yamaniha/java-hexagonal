package com.restaurant.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restaurant.domain.valueobject.Money;
import com.restaurant.domain.valueobject.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Order domain entity representing customer orders
 */
public class Order {
    
    private UUID id;
    
    @NotNull(message = "Customer ID is required")
    private UUID customerId;
    
    private UUID tableId;
    
    @NotNull(message = "Order status is required")
    private OrderStatus status;
    
    @Valid
    private List<OrderItem> items;
    
    @NotNull(message = "Total amount is required")
    @Valid
    private Money totalAmount;
    
    @Size(max = 500, message = "Special instructions cannot exceed 500 characters")
    private String specialInstructions;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime actualDeliveryTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Order() {
        this.id = UUID.randomUUID();
        this.status = OrderStatus.PENDING;
        this.items = new ArrayList<>();
        this.totalAmount = new Money(0.0);
        this.orderTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Order(UUID customerId, UUID tableId) {
        this();
        this.customerId = customerId;
        this.tableId = tableId;
    }
    
    // Business methods
    public void addItem(MenuItem menuItem, int quantity, String notes) {
        OrderItem orderItem = new OrderItem(menuItem.getId(), menuItem.getName(), 
                                           menuItem.getPrice(), quantity, notes);
        this.items.add(orderItem);
        recalculateTotal();
        updateEstimatedDeliveryTime();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeItem(UUID menuItemId) {
        this.items.removeIf(item -> item.getMenuItemId().equals(menuItemId));
        recalculateTotal();
        updateEstimatedDeliveryTime();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateItemQuantity(UUID menuItemId, int newQuantity) {
        items.stream()
            .filter(item -> item.getMenuItemId().equals(menuItemId))
            .findFirst()
            .ifPresent(item -> {
                item.setQuantity(newQuantity);
                recalculateTotal();
                updateEstimatedDeliveryTime();
                this.updatedAt = LocalDateTime.now();
            });
    }
    
    public void confirm() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm order without items");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void startPreparing() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order must be confirmed before preparing");
        }
        this.status = OrderStatus.PREPARING;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markReady() {
        if (status != OrderStatus.PREPARING) {
            throw new IllegalStateException("Order must be preparing before marking ready");
        }
        this.status = OrderStatus.READY;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deliver() {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Order must be ready before delivering");
        }
        this.status = OrderStatus.DELIVERED;
        this.actualDeliveryTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel(String reason) {
        if (status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel delivered order");
        }
        this.status = OrderStatus.CANCELLED;
        this.specialInstructions = (specialInstructions != null ? specialInstructions + " | " : "") + 
                                  "Cancelled: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    private void recalculateTotal() {
        this.totalAmount = items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(new Money(0.0), Money::add);
    }
    
    private void updateEstimatedDeliveryTime() {
        int totalPreparationTime = items.stream()
            .mapToInt(item -> item.getQuantity() * 15) // Assume 15 minutes per item
            .sum();
        this.estimatedDeliveryTime = orderTime.plusMinutes(totalPreparationTime);
    }
    
    public boolean canBeCancelled() {
        return status != OrderStatus.DELIVERED && status != OrderStatus.CANCELLED;
    }
    
    public boolean isCompleted() {
        return status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
    
    public UUID getTableId() {
        return tableId;
    }
    
    public void setTableId(UUID tableId) {
        this.tableId = tableId;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = new ArrayList<>(items);
        recalculateTotal();
    }
    
    public Money getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
    
    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }
    
    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }
    
    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }
    
    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
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
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", itemCount=" + items.size() +
                '}';
    }
}

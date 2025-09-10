package com.restaurant.domain.valueobject;

/**
 * Order status enumeration representing the lifecycle of an order
 */
public enum OrderStatus {
    PENDING("Pending", "Order has been created but not yet confirmed"),
    CONFIRMED("Confirmed", "Order has been confirmed and payment processed"),
    PREPARING("Preparing", "Order is being prepared in the kitchen"),
    READY("Ready", "Order is ready for pickup or delivery"),
    DELIVERED("Delivered", "Order has been delivered to the customer"),
    CANCELLED("Cancelled", "Order has been cancelled");
    
    private final String displayName;
    private final String description;
    
    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isActive() {
        return this != DELIVERED && this != CANCELLED;
    }
    
    public boolean isCompleted() {
        return this == DELIVERED || this == CANCELLED;
    }
    
    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == PREPARING || newStatus == CANCELLED;
            case PREPARING -> newStatus == READY || newStatus == CANCELLED;
            case READY -> newStatus == DELIVERED || newStatus == CANCELLED;
            case DELIVERED, CANCELLED -> false;
        };
    }
    
    public static OrderStatus fromDisplayName(String displayName) {
        for (OrderStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown order status: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

package com.restaurant.domain.valueobject;

/**
 * Table status enumeration representing the state of restaurant tables
 */
public enum TableStatus {
    AVAILABLE("Available", "Table is ready for new customers"),
    OCCUPIED("Occupied", "Table is currently occupied by customers"),
    RESERVED("Reserved", "Table is reserved for future customers"),
    CLEANING("Cleaning", "Table is being cleaned and prepared"),
    OUT_OF_SERVICE("Out of Service", "Table is temporarily unavailable");
    
    private final String displayName;
    private final String description;
    
    TableStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isAvailableForBooking() {
        return this == AVAILABLE;
    }
    
    public boolean isInUse() {
        return this == OCCUPIED || this == RESERVED;
    }
    
    public boolean canTransitionTo(TableStatus newStatus) {
        return switch (this) {
            case AVAILABLE -> newStatus == OCCUPIED || newStatus == RESERVED || newStatus == OUT_OF_SERVICE;
            case OCCUPIED -> newStatus == AVAILABLE || newStatus == CLEANING || newStatus == OUT_OF_SERVICE;
            case RESERVED -> newStatus == OCCUPIED || newStatus == AVAILABLE || newStatus == OUT_OF_SERVICE;
            case CLEANING -> newStatus == AVAILABLE || newStatus == OUT_OF_SERVICE;
            case OUT_OF_SERVICE -> newStatus == AVAILABLE || newStatus == CLEANING;
        };
    }
    
    public static TableStatus fromDisplayName(String displayName) {
        for (TableStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown table status: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

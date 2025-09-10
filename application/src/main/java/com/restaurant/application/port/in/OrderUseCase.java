package com.restaurant.application.port.in;

import com.restaurant.domain.entity.Order;
import com.restaurant.domain.valueobject.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for order-related use cases
 */
public interface OrderUseCase {
    
    /**
     * Create a new order
     */
    Order createOrder(CreateOrderCommand command);
    
    /**
     * Add item to order
     */
    Order addItemToOrder(AddOrderItemCommand command);
    
    /**
     * Remove item from order
     */
    Order removeItemFromOrder(UUID orderId, UUID menuItemId);
    
    /**
     * Update item quantity in order
     */
    Order updateOrderItemQuantity(UUID orderId, UUID menuItemId, int newQuantity);
    
    /**
     * Confirm an order
     */
    Order confirmOrder(UUID orderId);
    
    /**
     * Start preparing an order
     */
    Order startPreparingOrder(UUID orderId);
    
    /**
     * Mark order as ready
     */
    Order markOrderReady(UUID orderId);
    
    /**
     * Deliver an order
     */
    Order deliverOrder(UUID orderId);
    
    /**
     * Cancel an order
     */
    Order cancelOrder(UUID orderId, String reason);
    
    /**
     * Find order by ID
     */
    Optional<Order> findOrderById(UUID orderId);
    
    /**
     * Get orders by customer
     */
    List<Order> getOrdersByCustomer(UUID customerId);
    
    /**
     * Get orders by table
     */
    List<Order> getOrdersByTable(UUID tableId);
    
    /**
     * Get orders by status
     */
    List<Order> getOrdersByStatus(OrderStatus status);
    
    /**
     * Get active orders
     */
    List<Order> getActiveOrders();
    
    /**
     * Get orders by date range
     */
    List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get today's orders
     */
    List<Order> getTodaysOrders();
    
    /**
     * Command for creating an order
     */
    record CreateOrderCommand(
        UUID customerId,
        UUID tableId,
        String specialInstructions
    ) {}
    
    /**
     * Command for adding an item to an order
     */
    record AddOrderItemCommand(
        UUID orderId,
        UUID menuItemId,
        int quantity,
        String notes
    ) {}
}

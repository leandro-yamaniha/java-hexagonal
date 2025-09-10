package com.restaurant.application.port.out;

import com.restaurant.domain.entity.Order;
import com.restaurant.domain.valueobject.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for order data persistence
 */
public interface OrderRepository {
    
    /**
     * Save an order
     */
    Order save(Order order);
    
    /**
     * Find order by ID
     */
    Optional<Order> findById(UUID orderId);
    
    /**
     * Find all orders
     */
    List<Order> findAll();
    
    /**
     * Find orders by customer ID
     */
    List<Order> findByCustomerId(UUID customerId);
    
    /**
     * Find orders by table ID
     */
    List<Order> findByTableId(UUID tableId);
    
    /**
     * Find orders by status
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * Find active orders (not delivered or cancelled)
     */
    List<Order> findActiveOrders();
    
    /**
     * Find orders by date range
     */
    List<Order> findByOrderTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find today's orders
     */
    List<Order> findTodaysOrders();
    
    /**
     * Find orders by customer and date range
     */
    List<Order> findByCustomerIdAndOrderTimeBetween(UUID customerId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Delete order by ID
     */
    void deleteById(UUID orderId);
    
    /**
     * Count total orders
     */
    long count();
    
    /**
     * Count orders by status
     */
    long countByStatus(OrderStatus status);
    
    /**
     * Count orders by date range
     */
    long countByOrderTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Count today's orders
     */
    long countTodaysOrders();
}

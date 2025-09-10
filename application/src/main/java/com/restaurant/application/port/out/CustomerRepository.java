package com.restaurant.application.port.out;

import com.restaurant.domain.entity.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for customer data persistence
 */
public interface CustomerRepository {
    
    /**
     * Save a customer
     */
    Customer save(Customer customer);
    
    /**
     * Find customer by ID
     */
    Optional<Customer> findById(UUID customerId);
    
    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find all customers
     */
    List<Customer> findAll();
    
    /**
     * Find all active customers
     */
    List<Customer> findAllActive();
    
    /**
     * Search customers by name (case-insensitive)
     */
    List<Customer> searchByName(String name);
    
    /**
     * Check if customer exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Delete customer by ID
     */
    void deleteById(UUID customerId);
    
    /**
     * Count total customers
     */
    long count();
    
    /**
     * Count active customers
     */
    long countActive();
}

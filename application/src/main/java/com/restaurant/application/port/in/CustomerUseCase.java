package com.restaurant.application.port.in;

import com.restaurant.domain.entity.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port for customer-related use cases
 */
public interface CustomerUseCase {
    
    /**
     * Create a new customer
     */
    Customer createCustomer(CreateCustomerCommand command);
    
    /**
     * Update an existing customer
     */
    Customer updateCustomer(UpdateCustomerCommand command);
    
    /**
     * Find customer by ID
     */
    Optional<Customer> findCustomerById(UUID customerId);
    
    /**
     * Find customer by email
     */
    Optional<Customer> findCustomerByEmail(String email);
    
    /**
     * Search customers by name
     */
    List<Customer> searchCustomersByName(String name);
    
    /**
     * Get all active customers
     */
    List<Customer> getAllActiveCustomers();
    
    /**
     * Deactivate a customer
     */
    void deactivateCustomer(UUID customerId);
    
    /**
     * Activate a customer
     */
    void activateCustomer(UUID customerId);
    
    /**
     * Delete a customer (soft delete)
     */
    void deleteCustomer(UUID customerId);
    
    /**
     * Command for creating a customer
     */
    record CreateCustomerCommand(
        String name,
        String email,
        String phone,
        String address
    ) {}
    
    /**
     * Command for updating a customer
     */
    record UpdateCustomerCommand(
        UUID customerId,
        String name,
        String email,
        String phone,
        String address
    ) {}
}

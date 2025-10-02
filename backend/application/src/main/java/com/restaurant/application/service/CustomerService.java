package com.restaurant.application.service;

import com.restaurant.application.port.in.CustomerUseCase;
import com.restaurant.application.port.out.CacheService;
import com.restaurant.application.port.out.CustomerRepository;
import com.restaurant.domain.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Customer service implementation
 */
public class CustomerService implements CustomerUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private static final String CACHE_PREFIX = "customer:";
    private static final Duration CACHE_DURATION = Duration.ofMinutes(30);
    
    private final CustomerRepository customerRepository;
    private final CacheService cacheService;
    
    public CustomerService(CustomerRepository customerRepository, CacheService cacheService) {
        this.customerRepository = customerRepository;
        this.cacheService = cacheService;
    }
    
    @Override
    public Customer createCustomer(CreateCustomerCommand command) {
        logger.info("Creating customer with email: {}", command.email());
        
        // Check if customer already exists
        if (customerRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Customer with email already exists: " + command.email());
        }
        
        Customer customer = new Customer(
            command.name(),
            command.email(),
            command.phone(),
            command.address()
        );
        
        Customer savedCustomer = customerRepository.save(customer);
        
        // Cache the customer
        cacheService.put(CACHE_PREFIX + savedCustomer.getId(), savedCustomer, CACHE_DURATION);
        cacheService.put(CACHE_PREFIX + "email:" + savedCustomer.getEmail(), savedCustomer, CACHE_DURATION);
        
        logger.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return savedCustomer;
    }
    
    @Override
    public Customer updateCustomer(UpdateCustomerCommand command) {
        logger.info("Updating customer with ID: {}", command.customerId());
        
        Customer customer = findCustomerById(command.customerId())
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + command.customerId()));
        
        // Check if email is being changed and if new email already exists
        if (!customer.getEmail().equals(command.email()) && 
            customerRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Customer with email already exists: " + command.email());
        }
        
        customer.updateInfo(command.name(), command.email(), command.phone(), command.address());
        
        Customer updatedCustomer = customerRepository.save(customer);
        
        // Update cache
        cacheService.evict(CACHE_PREFIX + customer.getId());
        cacheService.evict(CACHE_PREFIX + "email:" + customer.getEmail());
        cacheService.put(CACHE_PREFIX + updatedCustomer.getId(), updatedCustomer, CACHE_DURATION);
        cacheService.put(CACHE_PREFIX + "email:" + updatedCustomer.getEmail(), updatedCustomer, CACHE_DURATION);
        
        logger.info("Customer updated successfully: {}", updatedCustomer.getId());
        return updatedCustomer;
    }
    
    @Override
    public Optional<Customer> findCustomerById(UUID customerId) {
        logger.debug("Finding customer by ID: {}", customerId);
        
        // Try cache first
        Optional<Customer> cachedCustomer = cacheService.get(CACHE_PREFIX + customerId, Customer.class);
        if (cachedCustomer.isPresent()) {
            logger.debug("Customer found in cache: {}", customerId);
            return cachedCustomer;
        }
        
        // Fallback to repository
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            cacheService.put(CACHE_PREFIX + customerId, customer.get(), CACHE_DURATION);
            logger.debug("Customer found in repository and cached: {}", customerId);
        }
        
        return customer;
    }
    
    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        logger.debug("Finding customer by email: {}", email);
        
        // Try cache first
        Optional<Customer> cachedCustomer = cacheService.get(CACHE_PREFIX + "email:" + email, Customer.class);
        if (cachedCustomer.isPresent()) {
            logger.debug("Customer found in cache by email: {}", email);
            return cachedCustomer;
        }
        
        // Fallback to repository
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            cacheService.put(CACHE_PREFIX + "email:" + email, customer.get(), CACHE_DURATION);
            cacheService.put(CACHE_PREFIX + customer.get().getId(), customer.get(), CACHE_DURATION);
            logger.debug("Customer found in repository by email and cached: {}", email);
        }
        
        return customer;
    }
    
    @Override
    public List<Customer> searchCustomersByName(String name) {
        logger.debug("Searching customers by name: {}", name);
        return customerRepository.searchByName(name);
    }
    
    @Override
    public List<Customer> getAllActiveCustomers() {
        logger.debug("Getting all active customers");
        return customerRepository.findAllActive();
    }
    
    @Override
    public void deactivateCustomer(UUID customerId) {
        logger.info("Deactivating customer: {}", customerId);
        
        Customer customer = findCustomerById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        
        customer.deactivate();
        customerRepository.save(customer);
        
        // Update cache
        cacheService.evict(CACHE_PREFIX + customerId);
        cacheService.evict(CACHE_PREFIX + "email:" + customer.getEmail());
        
        logger.info("Customer deactivated successfully: {}", customerId);
    }
    
    @Override
    public void activateCustomer(UUID customerId) {
        logger.info("Activating customer: {}", customerId);
        
        Customer customer = findCustomerById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        
        customer.activate();
        customerRepository.save(customer);
        
        // Update cache
        cacheService.evict(CACHE_PREFIX + customerId);
        cacheService.evict(CACHE_PREFIX + "email:" + customer.getEmail());
        
        logger.info("Customer activated successfully: {}", customerId);
    }
    
    @Override
    public void deleteCustomer(UUID customerId) {
        logger.info("Deleting customer: {}", customerId);
        
        Customer customer = findCustomerById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        
        customerRepository.deleteById(customerId);
        
        // Remove from cache
        cacheService.evict(CACHE_PREFIX + customerId);
        cacheService.evict(CACHE_PREFIX + "email:" + customer.getEmail());
        
        logger.info("Customer deleted successfully: {}", customerId);
    }
}

package com.restaurant.infrastructure.persistence.mapper;

import com.restaurant.domain.entity.Customer;
import com.restaurant.infrastructure.persistence.entity.CustomerEntity;

/**
 * Mapper between Customer domain entity and CustomerEntity JPA entity
 */
public class CustomerMapper {
    
    public static Customer toDomain(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Customer customer = new Customer();
        customer.setId(entity.getId());
        customer.setName(entity.getName());
        customer.setEmail(entity.getEmail());
        customer.setPhone(entity.getPhone());
        customer.setAddress(entity.getAddress());
        customer.setCreatedAt(entity.getCreatedAt());
        customer.setUpdatedAt(entity.getUpdatedAt());
        customer.setActive(entity.isActive());
        
        return customer;
    }
    
    public static CustomerEntity toEntity(Customer domain) {
        if (domain == null) {
            return null;
        }
        
        CustomerEntity entity = new CustomerEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setAddress(domain.getAddress());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setActive(domain.isActive());
        
        return entity;
    }
    
    public static void updateEntity(CustomerEntity entity, Customer domain) {
        if (entity == null || domain == null) {
            return;
        }
        
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setAddress(domain.getAddress());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setActive(domain.isActive());
    }
}

package com.restaurant.springboot.mapper;

import com.restaurant.domain.entity.Customer;
import com.restaurant.springboot.dto.CustomerDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper between Customer domain entity and CustomerDTO
 */
@Component
public class CustomerDTOMapper {
    
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        
        return new CustomerDTO(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getCreatedAt(),
            customer.getUpdatedAt(),
            customer.isActive()
        );
    }
    
    public Customer toDomain(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Customer customer = new Customer(
            dto.getName(),
            dto.getEmail(),
            dto.getPhone(),
            dto.getAddress()
        );
        
        // Set ID if present (for updates)
        if (dto.getId() != null) {
            customer.setId(dto.getId());
        }
        
        return customer;
    }
}

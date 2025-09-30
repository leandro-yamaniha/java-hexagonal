package com.restaurant.springboot.controller;

import com.restaurant.application.port.in.CustomerUseCase;
import com.restaurant.domain.entity.Customer;
import com.restaurant.springboot.dto.CustomerDTO;
import com.restaurant.springboot.mapper.CustomerDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Spring Boot REST controller for customer operations
 */
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Customer management operations")
public class CustomerController {
    
    @Autowired
    private CustomerUseCase customerUseCase;
    
    @Autowired
    private CustomerDTOMapper customerMapper;
    
    @PostMapping
    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid customer data")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        try {
            CustomerUseCase.CreateCustomerCommand command = new CustomerUseCase.CreateCustomerCommand(
                request.name(), request.email(), request.phone(), request.address()
            );
            Customer customer = customerUseCase.createCustomer(command);
            CustomerDTO dto = customerMapper.toDTO(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all customers")
    @ApiResponse(responseCode = "200", description = "List of customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
            @RequestParam(defaultValue = "true") boolean active) {
        List<Customer> customers = active ? 
            customerUseCase.getAllActiveCustomers() : 
            customerUseCase.getAllActiveCustomers(); // For now, always return active
        List<CustomerDTO> dtos = customers.stream()
            .map(customerMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer found")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<?> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable UUID id) {
        Optional<Customer> customer = customerUseCase.findCustomerById(id);
        return customer.map(c -> ResponseEntity.ok(customerMapper.toDTO(c)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get customer by email")
    @ApiResponse(responseCode = "200", description = "Customer found")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<?> getCustomerByEmail(
            @Parameter(description = "Customer email") @PathVariable String email) {
        Optional<Customer> customer = customerUseCase.findCustomerByEmail(email);
        return customer.map(c -> ResponseEntity.ok(customerMapper.toDTO(c)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search customers by name")
    @ApiResponse(responseCode = "200", description = "List of matching customers")
    public ResponseEntity<?> searchCustomers(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Name parameter is required"));
        }
        List<Customer> customers = customerUseCase.searchCustomersByName(name.trim());
        List<CustomerDTO> dtos = customers.stream()
            .map(customerMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @ApiResponse(responseCode = "400", description = "Invalid customer data")
    public ResponseEntity<?> updateCustomer(@PathVariable UUID id, 
                                          @Valid @RequestBody UpdateCustomerRequest request) {
        try {
            CustomerUseCase.UpdateCustomerCommand command = new CustomerUseCase.UpdateCustomerCommand(
                id, request.name(), request.email(), request.phone(), request.address()
            );
            Customer customer = customerUseCase.updateCustomer(command);
            CustomerDTO dto = customerMapper.toDTO(customer);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate customer")
    @ApiResponse(responseCode = "204", description = "Customer deactivated successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<?> deactivateCustomer(@PathVariable UUID id) {
        try {
            customerUseCase.deactivateCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate customer")
    @ApiResponse(responseCode = "204", description = "Customer activated successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<?> activateCustomer(@PathVariable UUID id) {
        try {
            customerUseCase.activateCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public ResponseEntity<?> deleteCustomer(@PathVariable UUID id) {
        try {
            customerUseCase.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DTOs
    public record CreateCustomerRequest(
        String name,
        String email,
        String phone,
        String address
    ) {}
    
    public record UpdateCustomerRequest(
        String name,
        String email,
        String phone,
        String address
    ) {}
    
    public record ErrorResponse(String message) {}
}

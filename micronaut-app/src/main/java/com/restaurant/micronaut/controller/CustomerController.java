package com.restaurant.micronaut.controller;

import com.restaurant.application.port.in.CustomerUseCase;
import com.restaurant.domain.entity.Customer;
import com.restaurant.micronaut.dto.CustomerDTO;
import com.restaurant.micronaut.mapper.CustomerDTOMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Micronaut REST controller for customer operations
 */
@Controller("/api/v1/customers")
@Tag(name = "Customers", description = "Customer management operations")
public class CustomerController {
    
    @Inject
    private CustomerUseCase customerUseCase;
    
    @Inject
    private CustomerDTOMapper customerMapper;
    
    @Post
    @Operation(summary = "Create a new customer")
    public HttpResponse<CustomerDTO> createCustomer(@Body CreateCustomerRequest request) {
        try {
            CustomerUseCase.CreateCustomerCommand command = new CustomerUseCase.CreateCustomerCommand(
                request.name(), request.email(), request.phone(), request.address()
            );
            Customer customer = customerUseCase.createCustomer(command);
            CustomerDTO dto = customerMapper.toDTO(customer);
            return HttpResponse.created(dto);
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest();
        }
    }
    
    @Get
    @Operation(summary = "Get all customers")
    public HttpResponse<List<CustomerDTO>> getAllCustomers(@QueryValue(defaultValue = "true") boolean active) {
        List<Customer> customers = active ? 
            customerUseCase.getAllActiveCustomers() : 
            customerUseCase.getAllActiveCustomers();
        List<CustomerDTO> dtos = customers.stream()
            .map(customerMapper::toDTO)
            .collect(Collectors.toList());
        return HttpResponse.ok(dtos);
    }
    
    @Get("/{id}")
    @Operation(summary = "Get customer by ID")
    public HttpResponse<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        return customerUseCase.findCustomerById(id)
            .map(customerMapper::toDTO)
            .map(HttpResponse::ok)
            .orElse(HttpResponse.notFound());
    }
    
    @Get("/email/{email}")
    @Operation(summary = "Get customer by email")
    public HttpResponse<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        return customerUseCase.findCustomerByEmail(email)
            .map(customerMapper::toDTO)
            .map(HttpResponse::ok)
            .orElse(HttpResponse.notFound());
    }
    
    @Get("/search")
    @Operation(summary = "Search customers by name")
    public HttpResponse<List<CustomerDTO>> searchCustomers(@QueryValue String name) {
        if (name == null || name.trim().isEmpty()) {
            return HttpResponse.badRequest();
        }
        List<Customer> customers = customerUseCase.searchCustomersByName(name.trim());
        List<CustomerDTO> dtos = customers.stream()
            .map(customerMapper::toDTO)
            .collect(Collectors.toList());
        return HttpResponse.ok(dtos);
    }
    
    @Put("/{id}")
    @Operation(summary = "Update customer")
    public HttpResponse<CustomerDTO> updateCustomer(@PathVariable UUID id, @Body UpdateCustomerRequest request) {
        try {
            CustomerUseCase.UpdateCustomerCommand command = new CustomerUseCase.UpdateCustomerCommand(
                id, request.name(), request.email(), request.phone(), request.address()
            );
            Customer customer = customerUseCase.updateCustomer(command);
            CustomerDTO dto = customerMapper.toDTO(customer);
            return HttpResponse.ok(dto);
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }
    
    @Put("/{id}/activate")
    @Operation(summary = "Activate customer")
    public HttpResponse<Void> activateCustomer(@PathVariable UUID id) {
        try {
            customerUseCase.activateCustomer(id);
            return HttpResponse.ok();
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }
    
    @Put("/{id}/deactivate")
    @Operation(summary = "Deactivate customer")
    public HttpResponse<Void> deactivateCustomer(@PathVariable UUID id) {
        try {
            customerUseCase.deactivateCustomer(id);
            return HttpResponse.ok();
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }
    
    @Delete("/{id}")
    @Operation(summary = "Delete customer")
    public HttpResponse<Void> deleteCustomer(@PathVariable UUID id) {
        try {
            customerUseCase.deleteCustomer(id);
            return HttpResponse.noContent();
        } catch (IllegalArgumentException e) {
            return HttpResponse.notFound();
        }
    }
    
    // Request records
    public record CreateCustomerRequest(String name, String email, String phone, String address) {}
    public record UpdateCustomerRequest(String name, String email, String phone, String address) {}
}

package com.restaurant.springboot.mapper;

import com.restaurant.domain.entity.Order;
import com.restaurant.domain.valueobject.OrderStatus;
import com.restaurant.springboot.dto.OrderDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper between Order domain entity and OrderDTO
 */
@Component
public class OrderDTOMapper {
    
    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }
        
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setTableId(order.getTableId());
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount().getAmount());
        dto.setCurrency(order.getTotalAmount().getCurrencyCode());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        dto.setOrderTime(order.getOrderTime());
        dto.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
        dto.setActualDeliveryTime(order.getActualDeliveryTime());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        // Map order items
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                .map(item -> new OrderDTO.OrderItemDTO(
                    item.getMenuItemId(),
                    item.getMenuItemName(),
                    item.getQuantity(),
                    item.getUnitPrice().getAmount(),
                    item.getSubtotal().getAmount(),
                    item.getNotes()
                ))
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    // Note: toDomain is more complex for Order due to aggregates
    // Usually orders are created through commands, not directly from DTOs
    // This is a simplified version
    public Order toDomainBasic(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Order order = new Order(dto.getCustomerId(), dto.getTableId());
        
        if (dto.getId() != null) {
            order.setId(dto.getId());
        }
        
        if (dto.getStatus() != null) {
            order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        }
        
        if (dto.getSpecialInstructions() != null) {
            order.setSpecialInstructions(dto.getSpecialInstructions());
        }
        
        return order;
    }
}

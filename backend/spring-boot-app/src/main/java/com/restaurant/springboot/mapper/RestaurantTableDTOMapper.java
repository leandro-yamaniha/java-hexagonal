package com.restaurant.springboot.mapper;

import com.restaurant.domain.entity.RestaurantTable;
import com.restaurant.domain.valueobject.TableStatus;
import com.restaurant.springboot.dto.RestaurantTableDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper between RestaurantTable domain entity and RestaurantTableDTO
 */
@Component
public class RestaurantTableDTOMapper {
    
    public RestaurantTableDTO toDTO(RestaurantTable table) {
        if (table == null) {
            return null;
        }
        
        return new RestaurantTableDTO(
            table.getId(),
            table.getTableNumber(),
            table.getCapacity(),
            table.getStatus().name(),
            table.getLocation(),
            table.getCurrentOrderId(),
            table.getLastOccupiedAt(),
            table.getCreatedAt(),
            table.getUpdatedAt()
        );
    }
    
    public RestaurantTable toDomain(RestaurantTableDTO dto) {
        if (dto == null) {
            return null;
        }
        
        TableStatus status = TableStatus.valueOf(dto.getStatus());
        
        RestaurantTable table = new RestaurantTable(
            dto.getTableNumber(),
            dto.getCapacity(),
            dto.getLocation()
        );
        
        // Set ID if present (for updates)
        if (dto.getId() != null) {
            table.setId(dto.getId());
        }
        
        table.setStatus(status);
        
        if (dto.getCurrentOrderId() != null) {
            table.setCurrentOrderId(dto.getCurrentOrderId());
        }
        
        return table;
    }
}

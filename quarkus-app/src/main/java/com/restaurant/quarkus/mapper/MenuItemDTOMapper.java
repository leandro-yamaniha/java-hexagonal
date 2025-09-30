package com.restaurant.quarkus.mapper;

import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.domain.valueobject.Money;
import com.restaurant.quarkus.dto.MenuItemDTO;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper between MenuItem domain entity and MenuItemDTO
 */
@ApplicationScoped
public class MenuItemDTOMapper {
    
    public MenuItemDTO toDTO(MenuItem menuItem) {
        if (menuItem == null) {
            return null;
        }
        
        return new MenuItemDTO(
            menuItem.getId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice().getAmount(),
            menuItem.getPrice().getCurrencyCode(),
            menuItem.getCategory().name(),
            menuItem.getImageUrl(),
            menuItem.isAvailable(),
            menuItem.getPreparationTimeMinutes(),
            menuItem.getCreatedAt(),
            menuItem.getUpdatedAt()
        );
    }
    
    public MenuItem toDomain(MenuItemDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Money price = new Money(dto.getPrice(), dto.getCurrency());
        MenuCategory category = MenuCategory.valueOf(dto.getCategory());
        
        MenuItem menuItem = new MenuItem(
            dto.getName(),
            dto.getDescription(),
            price,
            category
        );
        
        // Set ID if present (for updates)
        if (dto.getId() != null) {
            menuItem.setId(dto.getId());
        }
        
        menuItem.setImageUrl(dto.getImageUrl());
        menuItem.setPreparationTimeMinutes(dto.getPreparationTimeMinutes());
        menuItem.setAvailable(dto.isAvailable());
        
        return menuItem;
    }
}

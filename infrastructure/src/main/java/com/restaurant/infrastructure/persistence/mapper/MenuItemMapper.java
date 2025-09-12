package com.restaurant.infrastructure.persistence.mapper;

import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.domain.valueobject.Money;
import com.restaurant.infrastructure.persistence.entity.MenuCategoryEntity;
import com.restaurant.infrastructure.persistence.entity.MenuItemEntity;

/**
 * Mapper between MenuItem domain entity and MenuItemEntity JPA entity
 */
public class MenuItemMapper {
    
    public static MenuItem toDomain(MenuItemEntity entity) {
        if (entity == null) {
            return null;
        }
        
        MenuItem menuItem = new MenuItem();
        menuItem.setId(entity.getId());
        menuItem.setName(entity.getName());
        menuItem.setDescription(entity.getDescription());
        menuItem.setPrice(new Money(entity.getPrice(), entity.getCurrency()));
        menuItem.setCategory(toDomainCategory(entity.getCategory()));
        menuItem.setImageUrl(entity.getImageUrl());
        menuItem.setAvailable(entity.isAvailable());
        menuItem.setPreparationTimeMinutes(entity.getPreparationTimeMinutes());
        menuItem.setCreatedAt(entity.getCreatedAt());
        menuItem.setUpdatedAt(entity.getUpdatedAt());
        
        return menuItem;
    }
    
    public static MenuItemEntity toEntity(MenuItem domain) {
        if (domain == null) {
            return null;
        }
        
        MenuItemEntity entity = new MenuItemEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice().getAmount());
        entity.setCurrency(domain.getPrice().getCurrencyCode());
        entity.setCategory(toEntityCategory(domain.getCategory()));
        entity.setImageUrl(domain.getImageUrl());
        entity.setAvailable(domain.isAvailable());
        entity.setPreparationTimeMinutes(domain.getPreparationTimeMinutes());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        
        return entity;
    }
    
    public static void updateEntity(MenuItemEntity entity, MenuItem domain) {
        if (entity == null || domain == null) {
            return;
        }
        
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice().getAmount());
        entity.setCurrency(domain.getPrice().getCurrencyCode());
        entity.setCategory(toEntityCategory(domain.getCategory()));
        entity.setImageUrl(domain.getImageUrl());
        entity.setAvailable(domain.isAvailable());
        entity.setPreparationTimeMinutes(domain.getPreparationTimeMinutes());
        entity.setUpdatedAt(domain.getUpdatedAt());
    }
    
    private static MenuCategory toDomainCategory(MenuCategoryEntity entityCategory) {
        if (entityCategory == null) {
            return null;
        }
        
        return switch (entityCategory) {
            case APPETIZER -> MenuCategory.APPETIZER;
            case MAIN_COURSE -> MenuCategory.MAIN_COURSE;
            case DESSERT -> MenuCategory.DESSERT;
            case BEVERAGE -> MenuCategory.BEVERAGE;
            case SALAD -> MenuCategory.SALAD;
            case SOUP -> MenuCategory.SOUP;
            case PASTA -> MenuCategory.PASTA;
            case PIZZA -> MenuCategory.PIZZA;
            case SEAFOOD -> MenuCategory.SEAFOOD;
            case MEAT -> MenuCategory.MEAT;
            case VEGETARIAN -> MenuCategory.VEGETARIAN;
            case VEGAN -> MenuCategory.VEGAN;
            case GLUTEN_FREE -> MenuCategory.GLUTEN_FREE;
            case KIDS_MENU -> MenuCategory.KIDS_MENU;
            case SPECIAL -> MenuCategory.SPECIAL;
        };
    }
    
    private static MenuCategoryEntity toEntityCategory(MenuCategory domainCategory) {
        if (domainCategory == null) {
            return null;
        }
        
        return switch (domainCategory) {
            case APPETIZER -> MenuCategoryEntity.APPETIZER;
            case MAIN_COURSE -> MenuCategoryEntity.MAIN_COURSE;
            case DESSERT -> MenuCategoryEntity.DESSERT;
            case BEVERAGE -> MenuCategoryEntity.BEVERAGE;
            case SALAD -> MenuCategoryEntity.SALAD;
            case SOUP -> MenuCategoryEntity.SOUP;
            case PASTA -> MenuCategoryEntity.PASTA;
            case PIZZA -> MenuCategoryEntity.PIZZA;
            case SEAFOOD -> MenuCategoryEntity.SEAFOOD;
            case MEAT -> MenuCategoryEntity.MEAT;
            case VEGETARIAN -> MenuCategoryEntity.VEGETARIAN;
            case VEGAN -> MenuCategoryEntity.VEGAN;
            case GLUTEN_FREE -> MenuCategoryEntity.GLUTEN_FREE;
            case KIDS_MENU -> MenuCategoryEntity.KIDS_MENU;
            case SPECIAL -> MenuCategoryEntity.SPECIAL;
        };
    }
}

package com.restaurant.infrastructure.persistence.repository;

import com.restaurant.application.port.out.MenuItemRepository;
import com.restaurant.domain.entity.MenuItem;
import com.restaurant.domain.valueobject.MenuCategory;
import com.restaurant.infrastructure.persistence.entity.MenuCategoryEntity;
import com.restaurant.infrastructure.persistence.entity.MenuItemEntity;
import com.restaurant.infrastructure.persistence.mapper.MenuItemMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA implementation of MenuItemRepository
 */
public class JpaMenuItemRepository implements MenuItemRepository {
    
    private final EntityManager entityManager;

    public JpaMenuItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    @Transactional
    public MenuItem save(MenuItem menuItem) {
        MenuItemEntity entity = entityManager.find(MenuItemEntity.class, menuItem.getId());
        
        if (entity == null) {
            // Create new entity
            entity = MenuItemMapper.toEntity(menuItem);
            entityManager.persist(entity);
        } else {
            // Update existing entity
            MenuItemMapper.updateEntity(entity, menuItem);
            entity = entityManager.merge(entity);
        }
        
        return MenuItemMapper.toDomain(entity);
    }
    
    @Override
    public Optional<MenuItem> findById(UUID menuItemId) {
        MenuItemEntity entity = entityManager.find(MenuItemEntity.class, menuItemId);
        return Optional.ofNullable(MenuItemMapper.toDomain(entity));
    }
    
    @Override
    public List<MenuItem> findAll() {
        TypedQuery<MenuItemEntity> query = entityManager.createQuery(
            "SELECT m FROM MenuItemEntity m ORDER BY m.category, m.name", MenuItemEntity.class);
        return query.getResultList().stream()
            .map(MenuItemMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MenuItem> findAllAvailable() {
        TypedQuery<MenuItemEntity> query = entityManager.createQuery(
            "SELECT m FROM MenuItemEntity m WHERE m.available = true ORDER BY m.category, m.name", 
            MenuItemEntity.class);
        return query.getResultList().stream()
            .map(MenuItemMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MenuItem> findByCategory(MenuCategory category) {
        MenuCategoryEntity entityCategory = toEntityCategory(category);
        TypedQuery<MenuItemEntity> query = entityManager.createQuery(
            "SELECT m FROM MenuItemEntity m WHERE m.category = :category ORDER BY m.name", 
            MenuItemEntity.class);
        query.setParameter("category", entityCategory);
        return query.getResultList().stream()
            .map(MenuItemMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MenuItem> findAvailableByCategory(MenuCategory category) {
        MenuCategoryEntity entityCategory = toEntityCategory(category);
        TypedQuery<MenuItemEntity> query = entityManager.createQuery(
            "SELECT m FROM MenuItemEntity m WHERE m.category = :category AND m.available = true ORDER BY m.name", 
            MenuItemEntity.class);
        query.setParameter("category", entityCategory);
        return query.getResultList().stream()
            .map(MenuItemMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MenuItem> searchByName(String name) {
        TypedQuery<MenuItemEntity> query = entityManager.createQuery(
            "SELECT m FROM MenuItemEntity m WHERE LOWER(m.name) LIKE LOWER(:name) ORDER BY m.name", 
            MenuItemEntity.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList().stream()
            .map(MenuItemMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByName(String name) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(m) FROM MenuItemEntity m WHERE m.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() > 0;
    }
    
    @Override
    @Transactional
    public void deleteById(UUID menuItemId) {
        MenuItemEntity entity = entityManager.find(MenuItemEntity.class, menuItemId);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }
    
    @Override
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(m) FROM MenuItemEntity m", Long.class);
        return query.getSingleResult();
    }
    
    @Override
    public long countAvailable() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(m) FROM MenuItemEntity m WHERE m.available = true", Long.class);
        return query.getSingleResult();
    }
    
    @Override
    public long countByCategory(MenuCategory category) {
        MenuCategoryEntity entityCategory = toEntityCategory(category);
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(m) FROM MenuItemEntity m WHERE m.category = :category", Long.class);
        query.setParameter("category", entityCategory);
        return query.getSingleResult();
    }
    
    private MenuCategoryEntity toEntityCategory(MenuCategory domainCategory) {
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

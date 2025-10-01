package com.restaurant.micronaut.config;

import com.restaurant.application.port.in.CustomerUseCase;
import com.restaurant.application.port.in.MenuUseCase;
import com.restaurant.application.port.out.CacheService;
import com.restaurant.application.port.out.CustomerRepository;
import com.restaurant.application.port.out.MenuItemRepository;
import com.restaurant.application.service.CustomerService;
import com.restaurant.application.service.MenuService;
import com.restaurant.infrastructure.cache.RedisCacheService;
import com.restaurant.infrastructure.persistence.repository.JpaCustomerRepository;
import com.restaurant.infrastructure.persistence.repository.JpaMenuItemRepository;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Micronaut configuration for dependency injection
 */
@Factory
public class ApplicationConfig {
    
    @Singleton
    @Primary
    public JedisPool jedisPool() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(128);
            poolConfig.setMaxIdle(128);
            poolConfig.setMinIdle(16);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);
            return new JedisPool(poolConfig, "localhost", 6379);
        } catch (Exception e) {
            // Fallback: create a simple pool if Redis is not available
            return new JedisPool("localhost", 6379);
        }
    }
    
    @Singleton
    @Primary
    public JpaCustomerRepository jpaCustomerRepository(EntityManager entityManager) {
        return new JpaCustomerRepository(entityManager);
    }
    
    @Singleton
    @Primary
    public JpaMenuItemRepository jpaMenuItemRepository(EntityManager entityManager) {
        return new JpaMenuItemRepository(entityManager);
    }
    
    @Singleton
    @Primary
    public RedisCacheService redisCacheService(JedisPool jedisPool) {
        return new RedisCacheService(jedisPool);
    }
    
    @Singleton
    @Primary
    public CustomerRepository customerRepository(JpaCustomerRepository jpaCustomerRepository) {
        return jpaCustomerRepository;
    }
    
    @Singleton
    @Primary
    public MenuItemRepository menuItemRepository(JpaMenuItemRepository jpaMenuItemRepository) {
        return jpaMenuItemRepository;
    }
    
    @Singleton
    @Primary
    public CacheService cacheService(RedisCacheService redisCacheService) {
        return redisCacheService;
    }
    
    @Singleton
    @Primary
    public CustomerUseCase customerUseCase(
            CustomerRepository customerRepository,
            CacheService cacheService) {
        return new CustomerService(customerRepository, cacheService);
    }
    
    @Singleton
    @Primary
    public MenuUseCase menuUseCase(
            MenuItemRepository menuItemRepository,
            CacheService cacheService) {
        return new MenuService(menuItemRepository, cacheService);
    }
}

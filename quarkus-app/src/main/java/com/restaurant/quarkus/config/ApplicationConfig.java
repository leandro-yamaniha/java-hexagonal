package com.restaurant.quarkus.config;

import com.restaurant.application.port.in.CustomerUseCase;
import com.restaurant.application.port.in.MenuUseCase;
import com.restaurant.application.port.in.OrderUseCase;
import com.restaurant.application.port.in.TableUseCase;
import com.restaurant.application.port.out.CacheService;
import com.restaurant.application.port.out.CustomerRepository;
import com.restaurant.application.port.out.MenuItemRepository;
import com.restaurant.application.port.out.OrderRepository;
import com.restaurant.application.port.out.TableRepository;
import com.restaurant.application.service.CustomerService;
import com.restaurant.application.service.MenuService;
import com.restaurant.infrastructure.cache.RedisCacheService;
import com.restaurant.infrastructure.persistence.repository.JpaCustomerRepository;
import com.restaurant.infrastructure.persistence.repository.JpaMenuItemRepository;
import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Quarkus application configuration for dependency injection
 */
@ApplicationScoped
public class ApplicationConfig {
    
    // Use Quarkus-native injection for JPA EntityManager
    @Inject
    EntityManager entityManager;

    @Produces
    @Singleton
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        
        String redisHost = System.getenv().getOrDefault("REDIS_HOST", "localhost");
        int redisPort = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));
        
        return new JedisPool(poolConfig, redisHost, redisPort);
    }
    
    @Produces
    @Singleton
    public CacheService cacheService(JedisPool jedisPool) {
        return new RedisCacheService(jedisPool);
    }
    
    @Produces
    @Singleton
    public CustomerUseCase customerUseCase(CustomerRepository customerRepository, CacheService cacheService) {
        return new CustomerService(customerRepository, cacheService);
    }
    
    @Produces
    @Singleton
    public CustomerRepository customerRepository() {
        return new JpaCustomerRepository(entityManager);
    }
    
    @Produces
    @Singleton
    public MenuItemRepository menuItemRepository() {
        return new JpaMenuItemRepository(entityManager);
    }

    @Produces
    @Singleton
    public MenuUseCase menuUseCase(MenuItemRepository menuItemRepository, CacheService cacheService) {
        return new MenuService(menuItemRepository, cacheService);
    }
}

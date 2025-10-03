package com.restaurant.springboot.config;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import jakarta.persistence.EntityManager;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Spring Boot application configuration
 */
@Configuration
public class ApplicationConfig {
    
    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;
    
    @Value("${spring.data.redis.port:6379}")
    private int redisPort;
    
    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        
        return new JedisPool(poolConfig, redisHost, redisPort);
    }
    
    @Bean
    public CacheService cacheService(JedisPool jedisPool) {
        return new RedisCacheService(jedisPool);
    }
    
    @Bean
    public CustomerRepository customerRepository(EntityManager entityManager) {
        return new JpaCustomerRepository(entityManager);
    }
    
    @Bean
    public MenuItemRepository menuItemRepository(EntityManager entityManager) {
        return new JpaMenuItemRepository(entityManager);
    }
    
    @Bean
    public CustomerUseCase customerUseCase(CustomerRepository customerRepository, CacheService cacheService) {
        return new CustomerService(customerRepository, cacheService);
    }
    
    @Bean
    public MenuUseCase menuUseCase(MenuItemRepository menuItemRepository, CacheService cacheService) {
        return new MenuService(menuItemRepository, cacheService);
    }
}

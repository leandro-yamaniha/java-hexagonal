package com.restaurant.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Spring Boot main application class
 */
@SpringBootApplication
@EntityScan("com.restaurant.infrastructure.persistence.entity")
public class RestaurantManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementApplication.class, args);
    }
}

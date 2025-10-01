package com.restaurant.springboot.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

/**
 * Component to measure and log application startup time
 */
@Component
public class StartupTimeConfig implements ApplicationListener<ApplicationReadyEvent> {

    private static final long JVM_START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        long startupTime = System.currentTimeMillis() - JVM_START_TIME;
        
        System.out.println("=".repeat(80));
        System.out.println("🚀 SPRING BOOT STARTUP COMPLETED");
        System.out.println("📊 Framework: Spring Boot 3.2.1");
        System.out.println("🌐 Web Server: Undertow (High-Performance NIO)");
        System.out.println("⏱️  Total Startup Time: " + startupTime + " ms");
        System.out.println("🧵 Virtual Threads: ENABLED");
        System.out.println("🏗️  Architecture: Hexagonal (Domain-Driven)");
        System.out.println("=".repeat(80));
    }
}

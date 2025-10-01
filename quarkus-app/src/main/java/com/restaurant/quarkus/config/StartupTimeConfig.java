package com.restaurant.quarkus.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.lang.management.ManagementFactory;

/**
 * Component to measure and log application startup time for Quarkus
 */
@ApplicationScoped
public class StartupTimeConfig {

    private static final long JVM_START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();

    void onStart(@Observes StartupEvent ev) {
        long startupTime = System.currentTimeMillis() - JVM_START_TIME;
        
        System.out.println("=".repeat(80));
        System.out.println("🚀 QUARKUS STARTUP COMPLETED");
        System.out.println("📊 Framework: Quarkus 3.6.4");
        System.out.println("🌐 Web Server: Eclipse Vert.x (Reactive)");
        System.out.println("⏱️  Total Startup Time: " + startupTime + " ms");
        System.out.println("🧵 Virtual Threads: EXPERIMENTAL");
        System.out.println("🏗️  Architecture: Hexagonal (Domain-Driven)");
        System.out.println("=".repeat(80));
    }
}

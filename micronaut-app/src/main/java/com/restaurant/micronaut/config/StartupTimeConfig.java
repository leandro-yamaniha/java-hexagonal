package com.restaurant.micronaut.config;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;

import java.lang.management.ManagementFactory;

/**
 * Component to measure and log application startup time for Micronaut
 */
@Singleton
public class StartupTimeConfig implements ApplicationEventListener<ServerStartupEvent> {

    private static final long JVM_START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        long startupTime = System.currentTimeMillis() - JVM_START_TIME;
        
        System.out.println("=".repeat(80));
        System.out.println("🚀 MICRONAUT STARTUP COMPLETED");
        System.out.println("📊 Framework: Micronaut 4.6.3");
        System.out.println("🌐 Web Server: Netty (Asynchronous NIO)");
        System.out.println("⏱️  Total Startup Time: " + startupTime + " ms");
        System.out.println("🧵 Virtual Threads: BASIC");
        System.out.println("🏗️  Architecture: Hexagonal (Domain-Driven)");
        System.out.println("=".repeat(80));
    }
}

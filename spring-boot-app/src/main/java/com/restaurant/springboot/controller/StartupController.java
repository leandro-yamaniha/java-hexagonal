package com.restaurant.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to provide startup metrics and information
 */
@RestController
@RequestMapping("/api/v1/startup")
@Tag(name = "Startup Metrics", description = "Application startup time and performance metrics")
public class StartupController {

    private static final long JVM_START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();

    @GetMapping("/metrics")
    @Operation(summary = "Get startup metrics", description = "Retrieve application startup time and performance information")
    public Map<String, Object> getStartupMetrics() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        Runtime runtime = Runtime.getRuntime();
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("framework", "Spring Boot");
        metrics.put("version", "3.2.1");
        metrics.put("webServer", "Undertow");
        metrics.put("webServerType", "High-Performance NIO");
        metrics.put("jvmStartTime", JVM_START_TIME);
        metrics.put("uptimeMs", uptime);
        metrics.put("startupTimeMs", uptime);
        metrics.put("virtualThreads", "ENABLED");
        metrics.put("architecture", "Hexagonal");
        metrics.put("timestamp", LocalDateTime.now());
        
        Map<String, Object> memory = new HashMap<>();
        memory.put("totalMB", runtime.totalMemory() / 1024 / 1024);
        memory.put("freeMB", runtime.freeMemory() / 1024 / 1024);
        memory.put("usedMB", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
        memory.put("maxMB", runtime.maxMemory() / 1024 / 1024);
        metrics.put("memory", memory);
        
        Map<String, Object> performance = new HashMap<>();
        performance.put("processors", runtime.availableProcessors());
        performance.put("javaVersion", System.getProperty("java.version"));
        performance.put("jvmName", System.getProperty("java.vm.name"));
        metrics.put("performance", performance);
        
        return metrics;
    }
}

package com.restaurant.micronaut.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to provide startup metrics and information for Micronaut
 */
@Controller("/api/v1/startup")
@Tag(name = "Startup Metrics", description = "Application startup time and performance metrics")
public class StartupController {

    private static final long JVM_START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();

    @Get("/metrics")
    @Operation(summary = "Get startup metrics", description = "Retrieve application startup time and performance information")
    public HttpResponse<Map<String, Object>> getStartupMetrics() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        Runtime runtime = Runtime.getRuntime();
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("framework", "Micronaut");
        metrics.put("version", "4.2.3");
        metrics.put("webServer", "Netty");
        metrics.put("webServerType", "Asynchronous NIO");
        metrics.put("jvmStartTime", JVM_START_TIME);
        metrics.put("uptimeMs", uptime);
        metrics.put("startupTimeMs", uptime);
        metrics.put("virtualThreads", "BASIC");
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
        
        return HttpResponse.ok(metrics);
    }
}

package com.restaurant.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Benchmark controller to test Virtual Threads performance
 */
@RestController
@RequestMapping("/api/v1/benchmark")
@Tag(name = "Benchmark", description = "Virtual Threads performance testing")
public class BenchmarkController {

    private boolean isVirtualThread(Thread thread) {
        // Check if thread is virtual (compatible with Java 19+)
        return thread.toString().contains("VirtualThread") || 
               thread.getClass().getSimpleName().contains("Virtual");
    }

    @GetMapping("/virtual-threads")
    @Operation(summary = "Test Virtual Threads", description = "Simulate I/O intensive work to test Virtual Threads performance")
    public Map<String, Object> testVirtualThreads(
            @Parameter(description = "Delay in milliseconds") 
            @RequestParam(defaultValue = "1000") int delayMs) {
        
        long startTime = System.currentTimeMillis();
        String threadName = Thread.currentThread().toString();
        boolean isVirtual = isVirtualThread(Thread.currentThread());
        
        try {
            // Simulate I/O intensive work
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        
        return Map.of(
            "framework", "Spring Boot",
            "threadName", threadName,
            "isVirtualThread", isVirtual,
            "delayMs", delayMs,
            "executionTimeMs", endTime - startTime,
            "timestamp", LocalDateTime.now(),
            "javaVersion", System.getProperty("java.version")
        );
    }

    @GetMapping("/async")
    @Operation(summary = "Test Async Processing", description = "Test asynchronous processing with Virtual Threads")
    public CompletableFuture<Map<String, Object>> testAsync(
            @Parameter(description = "Number of parallel tasks") 
            @RequestParam(defaultValue = "10") int tasks) {
        
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            String threadName = Thread.currentThread().toString();
            boolean isVirtual = isVirtualThread(Thread.currentThread());
            
            // Simulate multiple parallel I/O operations
            CompletableFuture<Void>[] futures = new CompletableFuture[tasks];
            for (int i = 0; i < tasks; i++) {
                futures[i] = CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(100); // Simulate I/O
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            
            CompletableFuture.allOf(futures).join();
            long endTime = System.currentTimeMillis();
            
            return Map.of(
                "framework", "Spring Boot",
                "threadName", threadName,
                "isVirtualThread", isVirtual,
                "parallelTasks", tasks,
                "totalExecutionTimeMs", endTime - startTime,
                "timestamp", LocalDateTime.now()
            );
        });
    }
}

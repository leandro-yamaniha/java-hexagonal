package com.restaurant.micronaut.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.scheduling.TaskExecutors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Benchmark controller to test Virtual Threads performance in Micronaut
 */
@Controller("/api/v1/benchmark")
@Tag(name = "Benchmark", description = "Virtual Threads performance testing")
public class BenchmarkController {

    private boolean isVirtualThread(Thread thread) {
        // Check if thread is virtual (compatible with Java 19+)
        return thread.toString().contains("VirtualThread") || 
               thread.getClass().getSimpleName().contains("Virtual");
    }

    @Get("/virtual-threads")
    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Test Virtual Threads", description = "Simulate I/O intensive work to test Virtual Threads performance")
    public HttpResponse<Map<String, Object>> testVirtualThreads(
            @Parameter(description = "Delay in milliseconds") 
            @QueryValue(defaultValue = "1000") int delayMs) {
        
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
        
        Map<String, Object> response = Map.of(
            "framework", "Micronaut",
            "threadName", threadName,
            "isVirtualThread", isVirtual,
            "delayMs", delayMs,
            "executionTimeMs", endTime - startTime,
            "timestamp", LocalDateTime.now(),
            "javaVersion", System.getProperty("java.version")
        );
        
        return HttpResponse.ok(response);
    }

    @Get("/async")
    @ExecuteOn(TaskExecutors.IO)
    @Operation(summary = "Test Async Processing", description = "Test asynchronous processing with Virtual Threads")
    public HttpResponse<Map<String, Object>> testAsync(
            @Parameter(description = "Number of parallel tasks") 
            @QueryValue(defaultValue = "10") int tasks) {
        
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
        
        Map<String, Object> response = Map.of(
            "framework", "Micronaut",
            "threadName", threadName,
            "isVirtualThread", isVirtual,
            "parallelTasks", tasks,
            "totalExecutionTimeMs", endTime - startTime,
            "timestamp", LocalDateTime.now()
        );
        
        return HttpResponse.ok(response);
    }
}

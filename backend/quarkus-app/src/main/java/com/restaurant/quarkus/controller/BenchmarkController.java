package com.restaurant.quarkus.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Benchmark controller to test Virtual Threads performance in Quarkus
 */
@Path("/api/v1/benchmark")
@Tag(name = "Benchmark", description = "Virtual Threads performance testing")
public class BenchmarkController {

    private boolean isVirtualThread(Thread thread) {
        // Check if thread is virtual (compatible with Java 19+)
        return thread.toString().contains("VirtualThread") || 
               thread.getClass().getSimpleName().contains("Virtual");
    }

    @GET
    @Path("/virtual-threads")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @Operation(summary = "Test Virtual Threads", description = "Simulate I/O intensive work to test Virtual Threads performance")
    public Map<String, Object> testVirtualThreads(
            @Parameter(description = "Delay in milliseconds") 
            @QueryParam("delayMs") Integer delayMs) {
        
        if (delayMs == null) delayMs = 1000;
        
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
            "framework", "Quarkus",
            "threadName", threadName,
            "isVirtualThread", isVirtual,
            "delayMs", delayMs,
            "executionTimeMs", endTime - startTime,
            "timestamp", LocalDateTime.now(),
            "javaVersion", System.getProperty("java.version")
        );
    }

    @GET
    @Path("/async")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @Operation(summary = "Test Async Processing", description = "Test asynchronous processing with Virtual Threads")
    public Map<String, Object> testAsync(
            @Parameter(description = "Number of parallel tasks") 
            @QueryParam("tasks") Integer tasks) {
        
        if (tasks == null) tasks = 10;
        
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
            "framework", "Quarkus",
            "threadName", threadName,
            "isVirtualThread", isVirtual,
            "parallelTasks", tasks,
            "totalExecutionTimeMs", endTime - startTime,
            "timestamp", LocalDateTime.now()
        );
    }
}

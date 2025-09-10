package com.restaurant.application.port.out;

import java.time.Duration;
import java.util.Optional;

/**
 * Output port for caching operations
 */
public interface CacheService {
    
    /**
     * Store a value in cache with key
     */
    void put(String key, Object value);
    
    /**
     * Store a value in cache with key and expiration time
     */
    void put(String key, Object value, Duration expiration);
    
    /**
     * Get a value from cache by key
     */
    <T> Optional<T> get(String key, Class<T> type);
    
    /**
     * Check if key exists in cache
     */
    boolean exists(String key);
    
    /**
     * Remove a key from cache
     */
    void evict(String key);
    
    /**
     * Remove all keys matching pattern
     */
    void evictPattern(String pattern);
    
    /**
     * Clear all cache
     */
    void clear();
    
    /**
     * Get cache statistics
     */
    CacheStats getStats();
    
    /**
     * Cache statistics record
     */
    record CacheStats(
        long hitCount,
        long missCount,
        long evictionCount,
        double hitRate
    ) {}
}

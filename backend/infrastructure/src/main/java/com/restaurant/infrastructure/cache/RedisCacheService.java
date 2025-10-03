package com.restaurant.infrastructure.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurant.application.port.out.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Redis implementation of CacheService
 */
public class RedisCacheService implements CacheService {
    
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
    
    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    private final AtomicLong evictionCount = new AtomicLong(0);
    
    public RedisCacheService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Override
    public void put(String key, Object value) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonValue = objectMapper.writeValueAsString(value);
            jedis.set(key, jsonValue);
            logger.debug("Cached value for key: {}", key);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing value for key: {}", key, e);
        } catch (Exception e) {
            logger.error("Error putting value to cache for key: {}", key, e);
        }
    }
    
    @Override
    public void put(String key, Object value, Duration expiration) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonValue = objectMapper.writeValueAsString(value);
            jedis.setex(key, (int) expiration.getSeconds(), jsonValue);
            logger.debug("Cached value for key: {} with expiration: {}", key, expiration);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing value for key: {}", key, e);
        } catch (Exception e) {
            logger.error("Error putting value to cache for key: {} with expiration", key, e);
        }
    }
    
    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonValue = jedis.get(key);
            if (jsonValue == null) {
                missCount.incrementAndGet();
                logger.debug("Cache miss for key: {}", key);
                return Optional.empty();
            }
            
            T value = objectMapper.readValue(jsonValue, type);
            hitCount.incrementAndGet();
            logger.debug("Cache hit for key: {}", key);
            return Optional.of(value);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing value for key: {}", key, e);
            missCount.incrementAndGet();
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error getting value from cache for key: {}", key, e);
            missCount.incrementAndGet();
            return Optional.empty();
        }
    }
    
    @Override
    public boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("Error checking existence for key: {}", key, e);
            return false;
        }
    }
    
    @Override
    public void evict(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long deleted = jedis.del(key);
            if (deleted > 0) {
                evictionCount.incrementAndGet();
                logger.debug("Evicted key: {}", key);
            }
        } catch (Exception e) {
            logger.error("Error evicting key: {}", key, e);
        }
    }
    
    @Override
    public void evictPattern(String pattern) {
        try (Jedis jedis = jedisPool.getResource()) {
            ScanParams scanParams = new ScanParams().match(pattern).count(100);
            String cursor = "0";
            int totalDeleted = 0;
            
            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                cursor = scanResult.getCursor();
                
                if (!scanResult.getResult().isEmpty()) {
                    String[] keys = scanResult.getResult().toArray(new String[0]);
                    Long deleted = jedis.del(keys);
                    totalDeleted += deleted.intValue();
                }
            } while (!"0".equals(cursor));
            
            if (totalDeleted > 0) {
                evictionCount.addAndGet(totalDeleted);
                logger.debug("Evicted {} keys matching pattern: {}", totalDeleted, pattern);
            }
        } catch (Exception e) {
            logger.error("Error evicting keys with pattern: {}", pattern, e);
        }
    }
    
    @Override
    public void clear() {
        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.flushDB();
            logger.info("Cleared all cache entries: {}", result);
        } catch (Exception e) {
            logger.error("Error clearing cache", e);
        }
    }
    
    @Override
    public CacheStats getStats() {
        long hits = hitCount.get();
        long misses = missCount.get();
        long evictions = evictionCount.get();
        double hitRate = hits + misses > 0 ? (double) hits / (hits + misses) : 0.0;
        
        return new CacheStats(hits, misses, evictions, hitRate);
    }
}

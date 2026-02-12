package com.mutzin.droneplatform.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@Slf4j
public class RedisHeartbeatRepository {

    private static final String HEARTBEAT_ZSET = "drone:heartbeat";

    private final StringRedisTemplate redisTemplate;

    public RedisHeartbeatRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Create or refresh heartbeat.
     * Updates the timestamp in ZSET.
     */
    public void heartbeat(String serial) {
        long now = Instant.now().getEpochSecond();

        redisTemplate.opsForZSet()
                .add(HEARTBEAT_ZSET, serial, now);
    }

    /**
     * Remove heartbeat (called on disconnect or timeout cleanup).
     */
    public void remove(String serial) {
        redisTemplate.opsForZSet()
                .remove(HEARTBEAT_ZSET, serial);
    }

    /**
     * Find drones that exceeded heartbeat timeout.
     * Used by monitoring server.
     */
    public Set<String> findTimeoutDrones(long timeoutSeconds) {
        long threshold = Instant.now().getEpochSecond() - timeoutSeconds;

        return redisTemplate.opsForZSet()
                .rangeByScore(HEARTBEAT_ZSET, 0, threshold);
    }

    /**
     * Find alive drones (heartbeat within timeoutSeconds)
     * return [serials]
     */
public Set<String> findAliveDrones(long timeoutSeconds) {
    long now = Instant.now().getEpochSecond();
    long threshold = now - timeoutSeconds;
//    log.info("now={}, threshold={}, timeoutSeconds={}", now, threshold, timeoutSeconds);
    Set<String> result = redisTemplate.opsForZSet()
            .rangeByScore(HEARTBEAT_ZSET, threshold, Double.POSITIVE_INFINITY);
//    log.info("alive drones: {}", result);
    return result;
}
}

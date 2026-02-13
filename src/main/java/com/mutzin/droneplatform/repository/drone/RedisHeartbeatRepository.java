package com.mutzin.droneplatform.repository.drone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${drone.ttl}")
    private long TTL;
//    =Long.parseLong(TTL)
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
    public Set<String> findTimeoutDrones() {
        long threshold = Instant.now().getEpochSecond() - TTL;

        return redisTemplate.opsForZSet()
                .rangeByScore(HEARTBEAT_ZSET, 0, threshold);
    }

    /**
     * Find alive drones (heartbeat)
     * return [serials]
     */
    public Set<String> findAliveDrones() {
        long now = Instant.now().getEpochSecond();
        long threshold = now - TTL;
    //    log.info("now={}, threshold={}, timeoutSeconds={}", now, threshold, timeoutSeconds);
        Set<String> result = redisTemplate.opsForZSet()
                .rangeByScore(HEARTBEAT_ZSET, threshold, Double.POSITIVE_INFINITY);
    //    log.info("alive drones: {}", result);
        return result;
    }

    public boolean isAlive(String serial) {
        Double lastSeen = redisTemplate.opsForZSet()
                .score(HEARTBEAT_ZSET, serial);

        if (lastSeen == null) {
            return false;
        }

        long now = Instant.now().getEpochSecond();
        long threshold = now - TTL;

        return lastSeen >= threshold;
    }
}

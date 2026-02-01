package com.mutzin.droneplatform.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String token, String serial, long ttlSeconds) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(token, serial, Duration.ofSeconds(ttlSeconds));
        ops.set(serial, token, Duration.ofSeconds(ttlSeconds));
    }
    public String getSerialByToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }
    public String getTokenlBySerial(String serial) {
        return redisTemplate.opsForValue().get(serial);
    }

    public void deleteByToken(String token) {
        String serial = getSerialByToken(token);
        if (serial != null) {
            redisTemplate.delete(serial);
        }
        redisTemplate.delete(token);
    }

    public void deleteBySerial(String serial) {
        String token = getSerialByToken(serial);
        if (token != null) {
            redisTemplate.delete(token);
        }
        redisTemplate.delete(serial);
    }

}

package com.mutzin.droneplatform.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class RedisTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String saveToken(String serial, long ttlSeconds) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String token = UUID.randomUUID().toString();
        ops.set(token, serial, Duration.ofSeconds(ttlSeconds));
        ops.set(serial, token, Duration.ofSeconds(ttlSeconds));
        return token;
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

    public String updateTokenBySerial(String serial, long ttlSeconds) {
        deleteBySerial(serial);
        return saveToken(serial, ttlSeconds);
    }
}

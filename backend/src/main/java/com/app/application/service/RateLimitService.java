package com.app.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    @Autowired
    private StringRedisTemplate redis;

    public boolean allowed(Long userId) {

        String key = "rate:" + userId;

        Long count = redis.opsForValue().increment(key);

        if (count == 1) {
            redis.expire(key, Duration.ofMinutes(1));
        }

        return count <= 10;
    }
}
package com.app.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViralityService {

    @Autowired
    private StringRedisTemplate redis;

    public void addScore(Long postId, int score) {
        redis.opsForValue().increment("post:" + postId + ":virality", score);
    }
}
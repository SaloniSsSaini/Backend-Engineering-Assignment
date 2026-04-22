package com.app.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    private StringRedisTemplate redis;

    @Scheduled(fixedRate = 300000)
    public void sweep() {

        Set<String> keys = redis.keys("user:*:pending_notifs");

        for (String k : keys) {
            List<String> msgs = redis.opsForList().range(k, 0, -1);

            if (msgs != null && !msgs.isEmpty()) {
                System.out.println("Summary: " + msgs.size());
                redis.delete(k);
            }
        }
    }
}
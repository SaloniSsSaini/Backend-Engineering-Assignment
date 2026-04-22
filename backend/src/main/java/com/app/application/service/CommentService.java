package com.app.application.service;

import com.app.domain.model.Comment;
import com.app.infrastructure.redis.RedisLuaScripts;
import com.app.infrastructure.repository.CommentRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repo;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private RedisLuaScripts scripts;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private ViralityService viralityService;

    @Autowired
    private NotificationService notificationService;

    // 🔥 BOT COMMENT
    public void addBotComment(Comment c) {

        // 1. Depth check
        if (c.getDepthLevel() > 20) {
            throw new RuntimeException("Depth exceeded");
        }

        // 2. Horizontal cap (Lua atomic)
        Long res = redis.execute(
                scripts.botLimitScript(),
                Collections.singletonList("post:" + c.getPostId() + ":bot_count"),
                "100"
        );

        if (res == null || res == -1) {
            throw new RuntimeException("Bot limit reached (100)");
        }

        // 3. Cooldown check (10 min)
        String cooldownKey = "cooldown:bot_" + c.getAuthorId() + ":post_" + c.getPostId();

        Boolean exists = redis.hasKey(cooldownKey);
        if (Boolean.TRUE.equals(exists)) {
            throw new RuntimeException("Cooldown active (10 min)");
        }

        // set cooldown
        redis.opsForValue().set(cooldownKey, "1", 10, TimeUnit.MINUTES);

        // 4. Distributed lock
        RLock lock = redisson.getLock("post-lock-" + c.getPostId());

        try {
            lock.lock();

            repo.save(c);

            // 5. Virality +1
            viralityService.addScore(c.getPostId(), 1);

            // 6. Notification
            notificationService.handleBotInteraction(c.getPostId(), c.getAuthorId());

        } finally {
            lock.unlock();
        }
    }

    // 🔥 HUMAN COMMENT
    public void addHumanComment(Comment c) {

        if (c.getDepthLevel() > 20) {
            throw new RuntimeException("Depth exceeded");
        }

        repo.save(c);

        // virality +50
        viralityService.addScore(c.getPostId(), 50);
    }
}
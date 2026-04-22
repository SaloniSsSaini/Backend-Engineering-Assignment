package com.app.infrastructure.redis;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
public class RedisLuaScripts {

    public DefaultRedisScript<Long> botLimitScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/atomic_bot_limit.lua"));
        script.setResultType(Long.class);
        return script;
    }
}
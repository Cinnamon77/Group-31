package com.bookkeeping.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author bookkeeping
 **/
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 设置值
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 获取值
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除键
    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    // 设置带过期时间的值
    public void setValueWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
}

package com.bookkeeping.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author bookkeeping
 **/
@Service
public class StringRedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public StringRedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public Boolean hashKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 删除键
    public Boolean deleteKey(String key) {
        return stringRedisTemplate.delete(key);
    }

    // 设置带过期时间的值
    public void setValueWithExpire(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }
}

package by.poptsov.testcontainers.services.persist.impl;

import by.poptsov.testcontainers.services.persist.PersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * TODO: description
 *
 * @author Alexey Poptsov
 */
@Service
public class RedisPersistService implements PersistService {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    private RedisPersistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String name() {
        return "redis";
    }

    @Override
    public void store(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String fetch(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}

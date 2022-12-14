package com.spring.util.cache.redisson;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

/**
 * RedissonManager
 *
 * @author arthas
 */
@Component
public class RedissonManager {

    private static final String RAtomicName = "genId_";

    private static final Config config = new Config();
    private static volatile RedissonClient redissonClient = null;

    public static RedissonClient getRedisson() {

//        if (redissonClient == null) {
//            synchronized (RedissonManager.class) {
//                if (redissonClient == null) {
//                    config.useSingleServer().setAddress("redis://47.97.177.55:6379").setPassword("box123$%^");
//                    redissonClient = Redisson.create(config);
//                }
//            }
//        }
        return redissonClient;
    }

    /**
     * 获取redis中的原子ID
     */
    public static Long nextID() {
        RAtomicLong atomicLong = getRedisson().getAtomicLong(RAtomicName);
        atomicLong.incrementAndGet();
        return atomicLong.get();
    }
}
package com.tomdog.webboot.util.redisson;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * RedissonManager
 *
 * @author arthas
 */
public class RedissonManager {

    private static final String RAtomicName = "genId_";

    private static final Config config = new Config();
    private static volatile RedissonClient redissonClient = null;

    public static RedissonClient getRedisson() {

        if (redissonClient == null) {
            synchronized (RedissonManager.class) {
                if (redissonClient == null) {
                    config.useSingleServer().setAddress("redis://10.241.117.38:6379");
                    redissonClient = Redisson.create(config);
                }
            }
        }
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
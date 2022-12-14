package com.spring.util;

import com.spring.util.cache.redisson.RedisDistributedLocker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * redisson的原子性是靠lua脚本保证的
 *
 * @author home
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Test
    public void testConnect() {

        // 默认连接上 127.0.0.1:6379
        Config config = new Config();
        config.useSingleServer().setAddress("redis://10.241.117.38:6379");
        RedissonClient client = Redisson.create(config);

        // RList 继承了 java.util.List 接口
        RList<String> nameList = client.getList("nameList");
        nameList.clear();
        nameList.add("bingo");
        nameList.add("yanglbme");
        nameList.add("https://github.com/yanglbme");

        try {

            RList<String> nameList2 = client.getList("nameList");
            nameList2.forEach(System.out::println);

        } catch (Exception ignored) {
        } finally {
            client.shutdown();
        }
    }

    /**
     * 分布式锁
     */
    @Test
    public void testLock() throws InterruptedException {

        String redisKey = "123";
        RedisDistributedLocker redisDistributedLocker = new RedisDistributedLocker();

        Thread thread1 = new Thread(() -> {
            redisDistributedLocker.lock(redisKey);
            try {
                System.out.println("thread 1 start");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                redisDistributedLocker.unlock(redisKey);
                System.out.println("thread 1 end");
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 这里thread2在等thread1释放锁
            redisDistributedLocker.lock(redisKey);

            System.out.println("thread 2 start");

            redisDistributedLocker.unlock(redisKey);

            System.out.println("thread 2 end");
        });

        thread1.start();
        thread2.start();

        Thread.sleep(10);
    }

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    ZSetOperations<String, Object> zSetOperations;

    @Test
    public void testValueOption() {

//        hashOperations.put("hashMap", "userId", Sets.newHashSet(1, 2, 3));

        System.out.println(LocalDateTime.now().getNano());

//        System.out.println(zSetOperations.add("zset", "123", 234));
//        zSetOperations.add("zset", 234, nowTime);

        System.out.println(hashOperations.get("hashMap", "userId"));
        System.out.println(zSetOperations.rangeWithScores("zet", 0, 300));

    }


}

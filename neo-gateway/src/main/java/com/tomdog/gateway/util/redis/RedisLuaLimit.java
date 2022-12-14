package com.tomdog.gateway.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RedisLuaLimit {

    @Autowired
    private RedisTemplate redisTemplateBean;

    /**
     * 限流
     *
     * @param keys        限流规则，例如根据ip、接口限流
     * @param limitCount  请求上限
     * @param limitPeriod 单位时间
     */
    public boolean limit(List<String> keys, int limitCount, int limitPeriod) {

        // 获取Lua脚本内容
        String luaScript = buildLuaScript();

        // Redis整合Lua
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);

        // 执行Lua,并返回key值
        Number count = (Number) redisTemplateBean.execute(redisScript, keys, limitCount, limitPeriod);

        // 判断是否阻止请求
        if (count != null && count.intValue() <= limitCount) {
            return false;
        } else {
            log.info("limit rule is opening");
            return true;
        }
    }

    /**
     * 编写 redis Lua 限流脚本
     */
    public String buildLuaScript() {

        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +

                // 调用超过最大值，则直接返回
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +

                // 执行计算器自加
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +

                // 从第一次调用开始限流，设置对应键值的过期
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";

    }


}

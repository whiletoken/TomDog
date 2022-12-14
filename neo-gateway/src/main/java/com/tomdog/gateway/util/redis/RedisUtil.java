package com.tomdog.gateway.util.redis;

import com.tomdog.gateway.service.SpringLifeCycleAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
public class RedisUtil {

    /**
     * 静态获取单例
     *
     * @return RedisTemplate
     */
    private static RedisTemplate getRedis() {
        return (RedisTemplate) SpringLifeCycleAware.getBean("redisTemplateBean");
    }

    /**
     * 创建消费组
     *
     * @param key   streamKey
     * @param group groupName
     * @return recordId
     */
    public static String createGroup(String key, String group) {
        return getRedis().opsForStream().createGroup(key, group);
    }

    /**
     * 向流中追加记录，若流不存在，则创建
     *
     * @param record    记录类型为Map<String,String>
     * @param streamKey streamKey
     * @return 追加消息的RecordId
     */
    public static RecordId xAdd(Map<String, String> record, String streamKey) {
        try {
            StringRecord stringRecord = StreamRecords.string(record).withStreamKey(streamKey);
            // 刚追加记录的记录ID
            RecordId recordId = getRedis().opsForStream().add(stringRecord);
            log.info("{}", recordId.getValue());
            return recordId;
        } catch (Exception e) {
            log.error("xAdd error：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 流消息消费确认
     *
     * @return 成功确认的消息数
     */
    public static Long xAck(String groupName, Record<?, ?> record) {
        try {
            return getRedis().opsForStream().acknowledge(groupName, record);
        } catch (Exception e) {
            log.error("xAck error：{}", e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取消息列表，会自动过滤已经删除的消息
     */
    public static List<MapRecord<String, Object, Object>> xRange(String streamKey) {
        try {
            return getRedis().opsForStream().range(streamKey,
                    Range.from(Range.Bound.inclusive("-")).to(Range.Bound.inclusive("+")));
        } catch (Exception e) {
            log.error("xRange error：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取流包含的元素数量，即消息长度
     */
    public static Long xLen(String streamKey) {
        try {
            return getRedis().opsForStream().size(streamKey);
        } catch (Exception e) {
            log.error("xLen error：{}", e.getMessage(), e);
            return 0L;
        }
    }

}

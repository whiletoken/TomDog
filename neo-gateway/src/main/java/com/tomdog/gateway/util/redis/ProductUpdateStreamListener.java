package com.tomdog.gateway.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;

import java.util.Map;

@Slf4j
public class ProductUpdateStreamListener
        implements StreamListener<String, MapRecord<String, String, String>> {

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        RecordId recordId = message.getId();
        String stream = message.getStream();
        Map<String, String> record = message.getValue();

        log.info("recordId {},stream {}, record {}", recordId, stream, record);

        // 消息处理完毕后，确认该消息，以便从PEL中删除该消息ID
        RedisUtil.xAck("yyj_group", message);
    }
}

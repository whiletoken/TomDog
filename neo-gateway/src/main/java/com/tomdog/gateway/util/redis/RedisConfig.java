package com.tomdog.gateway.util.redis;

import bitbucket.neo.util.JacksonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    /**
     * 自定义序列化方式
     *
     * @param factory LettuceConnectionFactory
     * @return RedisTemplate
     */
    @Bean(name = "redisTemplateBean")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jacksonSerializer.setObjectMapper(JacksonUtils.getNewDefaultMapper());

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // value序列化方式采用Jackson
        template.setValueSerializer(jacksonSerializer);

        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // hash的value序列化采用Jackson
        template.setHashValueSerializer(jacksonSerializer);

        // 依赖注入，属性设置
        template.afterPropertiesSet();

        return template;
    }

    /**
     * StreamMessageListenerContainer
     * XGROUP [CREATE key groupname id-or-$]
     * <p>
     * key ：队列名称，如果不存在就创建
     * groupname ：组名。
     * $ ： 表示从尾部开始消费，只接受新消息，当前 Stream 消息会全部忽略。
     */
    @Bean
    public StreamMessageListenerContainer streamMessageListenerContainer(LettuceConnectionFactory factory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions
                = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                // 一次最多拉取5条消息
                .batchSize(5)
                // 拉取消息的超时时间
                .pollTimeout(Duration.ofMillis(100))
                .build();

        // 流消息订阅者容器
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer
                = StreamMessageListenerContainer.create(factory, containerOptions);
        // 使用消费组,ReadOffset.lastConsumed()在消费组最后消耗的消息ID之后读取。消费组内的消费者名称需在配置文件中配置
        // 需要注意stream和消费组需提前创建好，XGROUP CREATE yyj_stream yyj_group 0-0 MKSTREAM
        // 要在接收时自动确认消息，请使用receiveAutoAck代替receive
        // 经验证一个消费组内的多个消费者名称可以相同，不会重复消费，解决了集群部署不好区别消费者名称的问题
        streamMessageListenerContainer.receive(
                Consumer.from("yyj_group", "consumer_01"),
                StreamOffset.create("yyj_stream", ReadOffset.lastConsumed()),
                new ProductUpdateStreamListener());
        streamMessageListenerContainer.start();
        return streamMessageListenerContainer;
    }

}

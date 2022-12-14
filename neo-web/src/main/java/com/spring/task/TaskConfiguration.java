package com.spring.task;

import com.spring.util.ThreadPoolTaskExecutorMdcWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TaskConfiguration
 * <p>
 * 为什么需要这个配置，因为async默认最大线程是100,显然不符合
 *
 * @author willian
 **/

@Configuration
public class TaskConfiguration {

    @Bean("taskExecutorOrder")
    public Executor taskExecutor() {
        int coreSize = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorMdcWrapper();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(2 * coreSize);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("taskExecutorOrder-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}


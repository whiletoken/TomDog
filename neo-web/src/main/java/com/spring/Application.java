package com.spring;

import com.spring.base.AppApplicationListener;
import com.spring.util.SpringLifeCycleAware;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringApplication
 *
 * @author willian
 **/
@SpringBootApplication(scanBasePackages = {"com.tomdog.spring"},
        exclude = org.redisson.spring.starter.RedissonAutoConfiguration.class)
@EnableAsync
@MapperScan(basePackages = {"com.tomdog.spring.dao"})
@EnableTransactionManagement
@EnableConfigurationProperties
@ServletComponentScan("com.tomdog.spring.servlet")
public class Application {

    @Value("${kong.url}")
    private static String str;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("args = " + str);
    }

    @Bean
    public AppApplicationListener appApplicationListener() {
        return new AppApplicationListener();
    }

    @Bean
    public SpringLifeCycleAware springLifeCycleAware() {
        return new SpringLifeCycleAware();
    }

}

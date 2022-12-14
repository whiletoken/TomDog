package com.tomdog.gateway;

import com.tomdog.gateway.service.SpringLifeCycleAware;
import com.tomdog.gateway.servlet.AsyncDispatcherServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication(scanBasePackages = "com.tomdog.gateway")
@ServletComponentScan(basePackageClasses = AsyncDispatcherServlet.class)
public class App {

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);  // 单位为ms
        factory.setConnectTimeout(30000);  // 单位为ms
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public SpringLifeCycleAware springLifeCycleAware() {
        return new SpringLifeCycleAware();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
package com.tomdog.reward;

import com.spring.ioc.Bean;
import lombok.extern.slf4j.Slf4j;

@Bean
@Slf4j
public class ApplicationStarter extends AbstractApplication {

    public static void main(String[] args) {
        start();
    }
}

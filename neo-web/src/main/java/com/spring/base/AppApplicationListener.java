package com.spring.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * AppApplicationListener
 *
 * @author william
 **/

public class AppApplicationListener implements ApplicationListener {

    private final static Logger log = LoggerFactory.getLogger(AppApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStartedEvent) {
            log.info("================:{}", "ContextStartedEvent");
            return;
        }
        if (event instanceof ContextRefreshedEvent) {
            log.info("================:{}", "ContextRefreshedEvent");
            return;
        }
        if (event instanceof ContextClosedEvent) {
            log.info("================:{}", "ContextClosedEvent");
            return;
        }
        if (event instanceof ContextStoppedEvent) {
            log.info("================:{}", "ContextStoppedEvent");
            return;
        }
        if (event instanceof ServletWebServerInitializedEvent) {
            log.info("================:{}", "ServletWebServerInitializedEvent");
            return;
        }
        if (event instanceof ApplicationReadyEvent) {
            log.info("================:{}", "ApplicationReadyEvent");
            return;
        }
        log.info(">>>>>>>>>>>>>>>>:{}\n", event.getClass().getName());
    }

}

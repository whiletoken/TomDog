package com.spring.filter.aop;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动扫描 原理：利用spi spring.factories
 * <p>
 * 这里也可以用组件扫描，执行在Aspect上加@Component注解即可，但是这样的话有个问题。
 * 就是，如果你的这个Aspect所在包不是Spring Boot启动类所在的包或者子包下就需要指定@ComponentScan，
 * 因为Spring Boot默认只扫描和启动类同一级或者下一级包。
 *
 * @author home
 */
@Configuration
@AutoConfigureOrder(2147483647)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConditionalOnClass(SystemLogAspect.class)
@ConditionalOnMissingBean(SystemLogAspect.class)
public class SystemLogAutoConfiguration {

    @Bean
    public SystemLogAspect systemLogAspect() {
        return new SystemLogAspect();
    }
}

package com.spring.filter;

import bitbucket.neo.util.ThreadMdcUtil;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * HttpTrace
 * <p>
 * 1、urlPatterns 不能少
 * <p>
 * 2、多个filter时，需要order注解区分顺序
 *
 * @author willian
 **/
@Configuration
@WebFilter(filterName = "httpTrace", urlPatterns = "/*")
@Order(1)
public class HttpTrace implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {
        ThreadMdcUtil.remove();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ThreadMdcUtil.setTraceIdIfAbsent();
        chain.doFilter(request, response);
    }
}

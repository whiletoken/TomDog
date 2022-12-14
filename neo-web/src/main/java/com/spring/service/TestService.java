//package com.spring.service;
//
//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import org.springframework.stereotype.Service;
//
///**
// * Create Time: 2020/9/24
// *
// * @author <a href="mailto:liujj@shinemo.com">liujunjie</a>
// */
//@Service
//public class TestService {
//
//    @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
//    public void test() {
//        System.out.println("Test");
//    }
//
//    @SentinelResource(value = "hello", fallback = "helloFallback")
//    public String hello(long s) {
//        if (s < 0) {
//            throw new IllegalArgumentException("invalid arg");
//        }
//        return String.format("Hello at %d", s);
//    }
//
//
//    @SentinelResource(value = "helloAnother", defaultFallback = "defaultFallback",
//            exceptionsToIgnore = {IllegalStateException.class})
//    public String helloAnother(String name) {
//        if (name == null || "bad".equals(name)) {
//            throw new IllegalArgumentException("oops");
//        }
//        if ("foo".equals(name)) {
//            throw new IllegalStateException("oops");
//        }
//        return "Hello, " + name;
//    }
//
//    public String helloFallback(long s, Throwable ex) {
//        // Do some log here.
//        ex.printStackTrace();
//        return "Oops, error occurred at " + s;
//    }
//
//    public String defaultFallback() {
//        System.out.println("Go to default fallback");
//        return "default_fallback";
//    }
//
//}

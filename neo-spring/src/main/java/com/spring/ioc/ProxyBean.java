package com.spring.ioc;

public interface ProxyBean {

    <T> T newProxyInstance(T target);

}

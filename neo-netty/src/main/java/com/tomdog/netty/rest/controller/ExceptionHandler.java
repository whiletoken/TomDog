package com.tomdog.netty.rest.controller;

/**
 * 异常处理器
 *
 * @author Leo
 */
public interface ExceptionHandler {

    /**
     * 处理异常
     */
    void doHandle(Exception e);

}

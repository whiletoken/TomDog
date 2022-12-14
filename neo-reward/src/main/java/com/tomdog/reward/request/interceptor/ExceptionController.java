package com.tomdog.reward.request.interceptor;

import com.tomdog.netty.exception.ResourceNotFoundException;
import com.tomdog.netty.rest.HttpContextHolder;
import com.tomdog.netty.rest.HttpResponse;
import com.tomdog.netty.rest.HttpStatus;
import com.tomdog.netty.rest.controller.ExceptionHandler;

public class ExceptionController implements ExceptionHandler {

    /**
     * 处理异常
     */
    @Override
    public void doHandle(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        String errorMessage = e.getCause() == null ? "" : e.getCause().getMessage();
        if (errorMessage == null) {
            errorMessage = e.getMessage();
        }
        HttpResponse response = HttpContextHolder.getResponse();
        response.write(status, errorMessage);
        response.closeChannel();
    }

}

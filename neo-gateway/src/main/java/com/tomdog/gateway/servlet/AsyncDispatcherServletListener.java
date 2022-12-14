package com.tomdog.gateway.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.FutureTask;

/**
 * 异步监听器，当servlet异步处理完成时异步回调方法
 */
@Slf4j
public class AsyncDispatcherServletListener implements AsyncListener {

    private final FutureTask<Boolean> future;

    public AsyncDispatcherServletListener(FutureTask<Boolean> future) {
        this.future = future;
    }

    @Override
    public void onTimeout(AsyncEvent event) {
        log.warn("Async request did not complete timeout occur");
        handleTimeoutOrError(event, "Request timed out");
    }

    @Override
    public void onComplete(AsyncEvent event) {
        log.info("Completed async request");
    }

    @Override
    public void onError(AsyncEvent event) {
        String error = (event.getThrowable() == null ? "UNKNOWN ERROR" : event.getThrowable().getMessage());
        log.error("Error in async request " + error);
        handleTimeoutOrError(event, "Error processing " + error);
    }

    @Override
    public void onStartAsync(AsyncEvent event) {
        log.info("Async Event started..");
    }

    private void handleTimeoutOrError(AsyncEvent event, String message) {
        PrintWriter writer = null;
        try {
            // 请求超时，取消接收返回结果
            future.cancel(true);

            // 异步返回结果 getAsyncContext 异步上下文
            HttpServletResponse response = (HttpServletResponse) event.getAsyncContext().getResponse();
            writer = response.getWriter();
            writer.print(message);
            writer.flush();
        } catch (Exception ex) {
            log.error("ex is ", ex);
        } finally {
            // 请求结束
            event.getAsyncContext().complete();
            if (writer != null) {
                writer.close();
            }
        }
    }

}

package com.tomdog.netty.exception;

/**
 * 处理请求异常类
 *
 * @author Leo
 */
public final class HandleRequestException extends RuntimeException {

    private static final long serialVersionUID = -1;

    public HandleRequestException() {
    }

    public HandleRequestException(String message) {
        super(message);
    }

    public HandleRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleRequestException(Throwable cause) {
        super(cause);
    }

}

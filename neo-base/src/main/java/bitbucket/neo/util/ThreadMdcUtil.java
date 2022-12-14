package bitbucket.neo.util;

import bitbucket.neo.util.constants.Constants;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * ThreadMdcUtil
 *
 * @author willian
 **/
public class ThreadMdcUtil {

    public static void setTraceIdIfAbsent() {
        if (MDC.get(Constants.LOG_TRACE_ID) == null) {
            MDC.put(Constants.LOG_TRACE_ID, IdUtil.getShortUuId());
        }
    }

    public static void setTraceId() {
        MDC.put(Constants.LOG_TRACE_ID, IdUtil.getShortUuId());
    }

    public static void setTraceId(String traceId) {
        MDC.put(Constants.LOG_TRACE_ID, traceId);
    }

    public static void remove() {
        MDC.remove(Constants.LOG_TRACE_ID);
    }

    public static <T> Callable<T> wrap(Callable<T> callable, Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(Runnable runnable, Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}

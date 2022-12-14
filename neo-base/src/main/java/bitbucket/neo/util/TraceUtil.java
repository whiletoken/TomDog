package bitbucket.neo.util;


import org.apache.commons.lang3.StringUtils;

/**
 * TraceUtil
 *
 * @author willian
 **/

public class TraceUtil {

    private static final ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> StringUtils.EMPTY);

    public static String getTraceId() {
        return threadLocal.get();
    }

    public static void setTraceId() {
        if (StringUtils.isBlank(threadLocal.get())) {
            threadLocal.set(IdUtil.getShortUuId());
        }
    }

    public static void remove() {
        threadLocal.remove();
    }

}

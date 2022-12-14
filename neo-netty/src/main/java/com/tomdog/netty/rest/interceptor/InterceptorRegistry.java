package com.tomdog.netty.rest.interceptor;

import java.util.*;

/**
 * 拦截器注册类
 *
 * @author Leo
 */
public final class InterceptorRegistry {

    private final static Set<Interceptor> interceptors = new LinkedHashSet<>(8);

    private final static Map<String, List<String>> excludeMappings = new HashMap<>(8);

    public static void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public static void addInterceptor(Interceptor interceptor, String... excludeMappings) {
        interceptors.add(interceptor);
        if (excludeMappings == null || excludeMappings.length == 0) {
            return;
        }
        List<String> excludeMappingList = new ArrayList<>(8);
        Collections.addAll(excludeMappingList, excludeMappings);
        InterceptorRegistry.excludeMappings.put(interceptor.getClass().getName(), excludeMappingList);
    }

    public static Set<Interceptor> getInterceptors() {
        return interceptors;
    }

    public static List<String> getExcludeMappings(Interceptor interceptor) {
        return excludeMappings.get(interceptor.getClass().getName());
    }

}

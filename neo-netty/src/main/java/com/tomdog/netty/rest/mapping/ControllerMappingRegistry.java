package com.tomdog.netty.rest.mapping;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求映射注册类
 *
 * @author Leo
 */
@Slf4j
public final class ControllerMappingRegistry {

    private final Map<String, ControllerMapping> getMappings = new HashMap<>(64);

    private final Map<String, ControllerMapping> postMappings = new HashMap<>(64);

    private final Map<String, ControllerMapping> putMappings = new HashMap<>(64);

    private final Map<String, ControllerMapping> deleteMappings = new HashMap<>(64);

    private final Map<String, ControllerMapping> patchMappings = new HashMap<>(64);

    /**
     * 缓存 REST 控制器类
     */
    private final Map<String, ControllerBean> beans = new HashMap<>(128);

    /**
     * 缓存 REST 控制器类单例
     */
    private final Map<String, Object> singletons = new ConcurrentHashMap<>(128);

    private static final ControllerMappingRegistry controllerMappingRegistry = new ControllerMappingRegistry();

    public static ControllerMappingRegistry newInstance() {
        return controllerMappingRegistry;
    }

    /**
     * 注册Controller Bean
     */
    public void registerBean(String name, ControllerBean bean) {
        beans.put(name, bean);
    }

    /**
     * 得到Controller Bean
     */
    public ControllerBean getBean(String name) {
        return beans.get(name);
    }

    /**
     * 注册Controller类的单例
     */
    public void registerSingleton(String name, Object singleton) {
        singletons.put(name, singleton);
    }

    /**
     * 得到单例
     */
    public Object getSingleton(String name) {
        if (singletons.containsKey(name)) {
            return singletons.get(name);
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            log.error("Class not found: {}", name);
            return null;
        }
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Create class instance failure: {}", name);
            return null;
        }
        Object result = singletons.putIfAbsent(name, instance);
        if (result == null) {
            return instance;
        }
        return result;
    }

    /**
     * 注册 Get Mapping
     */
    public void registerGetMapping(String url, ControllerMapping mapping) {
        getMappings.put(url, mapping);
    }

    /**
     * 得到Get映射哈希表
     */
    public Map<String, ControllerMapping> getGetMappings() {
        return getMappings;
    }

    /**
     * 注册 Post Mapping
     */
    public void registerPostMapping(String url, ControllerMapping mapping) {
        postMappings.put(url, mapping);
    }

    /**
     * 得到Post映射哈希表
     */
    public Map<String, ControllerMapping> getPostMappings() {
        return postMappings;
    }

    /**
     * 注册 Put Mapping
     */
    public void registerPutMapping(String url, ControllerMapping mapping) {
        putMappings.put(url, mapping);
    }

    /**
     * 得到Put映射哈希表
     */
    public Map<String, ControllerMapping> getPutMappings() {
        return putMappings;
    }

    /**
     * 注册 Delete Mapping
     */
    public void registerDeleteMapping(String url, ControllerMapping mapping) {
        deleteMappings.put(url, mapping);
    }

    /**
     * 得到Delete映射哈希表
     */
    public Map<String, ControllerMapping> getDeleteMappings() {
        return deleteMappings;
    }

    /**
     * 注册 Patch Mapping
     */
    public void registerPatchMapping(String url, ControllerMapping mapping) {
        patchMappings.put(url, mapping);
    }

    /**
     * 得到Patch映射哈希表
     */
    public Map<String, ControllerMapping> getPatchMappings() {
        return patchMappings;
    }

}

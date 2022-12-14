package com.tomdog.netty.rest.mapping;

/**
 * 控制器 Bean 类
 *
 * @author Leo
 */
public final class ControllerBean {

    public ControllerBean(Class<?> clazz, boolean singleton) {
        this.clazz = clazz;
        this.singleton = singleton;
    }

    private Class<?> clazz;

    private boolean singleton;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean getSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

}

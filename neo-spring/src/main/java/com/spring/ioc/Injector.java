package com.spring.ioc;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.DataSourceService;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Injector {

    // 已经生成的单例
    private final Map<Class<?>, Object> finalSingletonMap = new ConcurrentHashMap<>();

    // 准备进行构造的类
    private final Set<Class<?>> processingInstances = new ConcurrentHashSet<>();

    private static final Injector injector = new Injector();

    public static Injector getInstance() {
        return injector;
    }

    public Map<Class<?>, Object> getFinalSingletonMap() {
        return finalSingletonMap;
    }

    public void setBean(Object obj) {
        finalSingletonMap.put(obj.getClass(), obj);
    }

    /**
     * 获取对象
     *
     * @param clazz clz
     */
    public <T> T getBean(Class<T> clazz) {
        return createNew(clazz);
    }

    public synchronized void injectAll() {
        Set<Class<?>> classSet = ClassUtil.scanPackage("com.tomdog.reward");
        log.debug("classSet size is {}", classSet.size());
        for (Class<?> clazz : classSet) {
            createNew(clazz);
        }
    }


    @SuppressWarnings("unchecked")
    private <T> T createNew(Class<T> clazz) {
        if (!isNeedCreate(clazz)) {
            log.debug("createNew clazz {} no need", clazz);
            return null;
        }
        log.debug("createNew clazz {}", clazz);
        Object o = finalSingletonMap.get(clazz);
        if (o != null) {
            return (T) o;
        }
        ArrayList<Constructor<T>> constructors = new ArrayList<>();
        T target;
        for (Constructor<?> con : clazz.getDeclaredConstructors()) {
            // 优先处理 Inject 构造器
            if (con.isAnnotationPresent(Inject.class)) {
                ReflectUtil.setAccessible(con);
                constructors.add(0, (Constructor<T>) con);
            } else if (con.getParameterCount() == 0) {
                ReflectUtil.setAccessible(con);
                constructors.add((Constructor<T>) con);
            }
            if (constructors.size() > 2) {
                throw new InjectException("dupcated constructor for injection class " + clazz.getCanonicalName());
            }
        }

        if (constructors.size() == 0) {
            throw new InjectException("no accessible constructor for injection class " + clazz.getCanonicalName());
        }

        processingInstances.add(clazz); // 放入表示未完成的容器

        target = createFromConstructor(constructors.get(0)); // 构造器注入

        injectField(target);
        injectMethod(target);

        processingInstances.remove(clazz); // 从未完成的容器取出

        Bean bean = clazz.getAnnotation(Bean.class);
        if (bean.isAop()) {
            try {
                ProxyBean proxyBean = (ProxyBean) Class.forName(bean.proxyClassName()).newInstance();
                target = proxyBean.newProxyInstance(target);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        finalSingletonMap.put(clazz, target);
        log.info("{} Injected", clazz);
        return target;
    }

    private boolean isNeedCreate(Class<?> clazz) {
        Bean bean = clazz.getAnnotation(Bean.class);
        if (bean == null) {
            return false;
        }
        if (clazz.isInterface()) {
            if (bean.isDao()) {
                finalSingletonMap.put(clazz, ProxyUtil.newProxyInstance(clazz.getClassLoader(),
                        (proxy, method, args) -> Objects.requireNonNull(createNew(DataSourceService.class)).execute(clazz, method, args), clazz));
                return true;
            }
            return false;
        }
        return true;
    }

    private <T> T createFromConstructor(Constructor<T> con) {
        Object[] params = new Object[con.getParameterCount()];
        int i = 0;
        for (Parameter parameter : con.getParameters()) {
            if (processingInstances.contains(parameter.getType())) {
                throw new InjectException(
                        String.format("循环依赖 on constructor, class is %s", con.getDeclaringClass().getCanonicalName()));
            }
            Object param = createFromParameter(parameter);
            params[i++] = param;
        }
        try {
            return con.newInstance(params);
        } catch (Exception e) {
            throw new InjectException("create instance from constructor error", e);
        }
    }

    /**
     * 注入成员
     *
     * @param body body
     */
    private <T> void injectField(T body) {
        List<Field> fields = new ArrayList<>();
        for (Field field : body.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                ReflectUtil.setAccessible(field);
                fields.add(field);
            }
        }
        for (Field field : fields) {
            Object f = createFromField(field);
            try {
                field.set(body, f);
            } catch (Exception e) {
                throw new InjectException(
                        String.format("set field for %s@%s error", body.getClass().getCanonicalName(), field.getName()),
                        e);
            }
        }
    }

    private <T> void injectMethod(T body) {
        List<Method> methods = new ArrayList<>();
        Method[] declaredMethods = body.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getParameterCount() > 0
                    && declaredMethod.isAnnotationPresent(Inject.class)) {
                ReflectUtil.setAccessible(declaredMethod);
                methods.add(declaredMethod);
            }
        }

        int i;
        for (Method method : methods) {
            Object[] params = new Object[method.getParameterCount()];
            i = 0;
            for (Parameter parameter : method.getParameters()) {
                if (processingInstances.contains(parameter.getType())) {
                    throw new InjectException(
                            String.format("循环依赖 on method , the root class is %s", body.getClass().getCanonicalName()));
                }
                Object param = createFromParameter(parameter);
                params[i++] = param;
            }
            try {
                method.invoke(body, params);
            } catch (Exception e) {
                throw new InjectException("injectMethod ", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createFromParameter(Parameter parameter) {
        Class<?> clazz = parameter.getType();
        return (T) createNew(clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T createFromField(Field field) {
        Class<?> clazz = field.getType();
        return (T) createNew(clazz);
    }

}

package com.tomdog.netty.rest.mapping;

import java.lang.reflect.Method;

import com.tomdog.netty.annotation.PutMapping;

/**
 * PUT 请求映射注册策略类
 *
 * @author Leo
 */
public final class PutMappingRegisterStrategy extends AbstractRequestMappingRegisterStrategy implements RequestMappingRegisterStrategy {

    /**
     * 得到控制器方法的Url
     */
    @Override
    public String getMethodUrl(Method method) {
        if (method.getAnnotation(PutMapping.class) != null) {
            return method.getAnnotation(PutMapping.class).value();
        }
        return "";
    }

    /**
     * 得到Http请求的方法类型
     */
    @Override
    public String getHttpMethod() {
        return "PUT";
    }

    /**
     * 注册Mapping
     */
    @Override
    public void registerMapping(String url, ControllerMapping mapping) {
        ControllerMappingRegistry.newInstance().getPutMappings().put(url, mapping);
    }

}
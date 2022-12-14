package com.tomdog.netty.rest.mapping;

import java.lang.reflect.Method;

import com.tomdog.netty.annotation.PostMapping;

/**
 * POST 请求映射注册策略类
 *
 * @author Leo
 */
public final class PostMappingRegisterStrategy extends AbstractRequestMappingRegisterStrategy implements RequestMappingRegisterStrategy {

    /**
     * 得到控制器方法的Url
     */
    @Override
    public String getMethodUrl(Method method) {
        if (method.getAnnotation(PostMapping.class) != null) {
            return method.getAnnotation(PostMapping.class).value();
        }
        return "";
    }

    /**
     * 得到Http请求的方法类型
     */
    @Override
    public String getHttpMethod() {
        return "POST";
    }

    /**
     * 注册Mapping
     */
    @Override
    public void registerMapping(String url, ControllerMapping mapping) {
        ControllerMappingRegistry.newInstance().getPostMappings().put(url, mapping);
    }

}

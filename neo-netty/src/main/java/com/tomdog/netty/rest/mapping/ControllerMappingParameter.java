package com.tomdog.netty.rest.mapping;

import lombok.Data;

/**
 * 请求映射参数类
 *
 * @author Leo
 */
@Data
public final class ControllerMappingParameter {

    private String name;

    private Class<?> dataType;

    private ControllerMappingParameterTypeEnum type;

    private boolean required = true;

}

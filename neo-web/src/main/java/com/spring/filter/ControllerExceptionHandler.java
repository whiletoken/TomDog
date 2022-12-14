package com.spring.filter;

import com.spring.convert.CustomDateConverter;
import com.spring.convert.StringTrimConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * ControllerExceptionHandler
 *
 * @author willian
 * @date 2019-12-04 12:56
 **/

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @InitBinder
    public void initMyBinder(ServletRequestDataBinder binder) {

        FormattingConversionService formattingConversionService =
                (FormattingConversionService) binder.getConversionService();
        if (formattingConversionService != null) {
            formattingConversionService.addConverter(new StringTrimConverter());
            formattingConversionService.addConverter(new CustomDateConverter());
        }

    }

    /**
     * 如果使用了@RestControllerAdvice，这里就不需要@ResponseBody了
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> handler(Exception ex) {
        log.error("统一异常处理", ex);
        Map<String, Object> map = new HashMap<>(8);
        map.put("code", 400);
        map.put("msg", ex.getMessage());
        return map;
    }


}

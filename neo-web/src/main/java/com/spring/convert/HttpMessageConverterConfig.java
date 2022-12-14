package com.spring.convert;


import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;


/**
 * HttpMessageConverterConfig
 *
 * @author liujunjie
 */

@Configuration
public class HttpMessageConverterConfig {

    @Bean
    public HttpMessageConverters messageConverters() {

        List<HttpMessageConverter<?>> list = new ArrayList<>();
        list.add(new GsonHttpMessageConverter());
        list.add(new StringHttpMessageConverter());

        return new HttpMessageConverters(list);
    }

}

package org.apache.ibatis;

import cn.hutool.core.io.resource.ResourceUtil;
import com.spring.ioc.Bean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Bean
@Slf4j
public class DataSourceService {

    private final static Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public void init() {
        SqlSessionFactory sqlSessionFactory;
        try (InputStream inputStream = ResourceUtil.getStream("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            log.info("DataSourceService init");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Configuration configuration = sqlSessionFactory.getConfiguration();
        for (Class<?> clazz : configuration.getMapperRegistry().getMappers()) {
            log.info("dao bean {} is founded", clazz.getName());
            knownMappers.put(clazz, new MapperProxyFactory<>(clazz));
        }
        SqlSessionFactoryUtil.setSqlSessionFactory(sqlSessionFactory);
    }

    public Object execute(Class tClass, Method method, Object[] args) {
        try {
            SqlSession sqlSession = SqlSessionFactoryUtil.getOpenSession();
            MapperProxyFactory mapperProxyFactory = knownMappers.get(tClass);
            MapperProxy mapperProxy = new MapperProxy<>(sqlSession,
                    mapperProxyFactory.getMapperInterface(), mapperProxyFactory.getMethodCache());
            return mapperProxy.invoke(mapperProxy, method, args);
        } catch (Throwable e) {
            SqlSessionFactoryUtil.rollback();
            throw new RuntimeException(e);
        } finally {
            SqlSessionFactoryUtil.closeSession();
        }
    }

}

package com.tomdog.netty.util;

import cn.hutool.core.io.resource.ResourceUtil;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * 定义一个单例的 资源管理器
 */
public class ResourcesService {

    final String WEB_PATH_PREFIX = "web.static.path";
    final String CONFIG_FILE_NAME = "config.properties";

    private final HashMap<String, String> urlMap = new HashMap<>();
    private final HashMap<String, String> propertiesMap = new HashMap<>();
    private static final ResourcesService INSTANCE = new ResourcesService();

    private ResourcesService() {
        // 先读取默认配置文件,再读取外部配置文件
        Properties prop = new Properties();
        try {
            InputStream inputStream = ResourceUtil.getStream(CONFIG_FILE_NAME);
            prop.load(inputStream);
        } catch (Exception e) {
            System.err.println("未读取到默认配置文件");
        }
        setConfig(prop);
        prop.clear();
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex) + File.separator + CONFIG_FILE_NAME;
        InputStream in = ResourcesService.class.getClassLoader().getResourceAsStream(path);
        try {
            prop.load(in);
        } catch (Exception e) {
            System.err.println("未读取到外部配置文件");
        }
        setConfig(prop);
    }

    private void setConfig(Properties prop) {
        Enumeration<Object> keys = prop.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(WEB_PATH_PREFIX)) {
                String url = key.substring(16);
                url = url.replaceAll("\\.", "/");
                urlMap.put("/" + url, prop.getProperty(key));
            } else {
                propertiesMap.put(key, prop.getProperty(key));
            }
        }
    }

    public static ResourcesService getInstance() {
        return INSTANCE;
    }

    public String getPath(String url) {
        return urlMap.getOrDefault(url, null);
    }

    public String getProperties(String key) {
        return propertiesMap.getOrDefault(key, null);
    }
}

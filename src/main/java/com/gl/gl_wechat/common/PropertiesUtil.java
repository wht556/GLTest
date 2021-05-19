package com.gl.gl_wechat.common;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@PropertySource("classpath:conf/system.properties")
public class PropertiesUtil {

    private static Properties _prop;

    /**
     * 读取配置文件
     *
     * @param fileName
     */
    public static void readProperties(String fileName) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileName);
//            InputStream in = classPathResource.getInputStream();

            InputStream in = PropertiesUtil.class.getResourceAsStream("/" + fileName);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            _prop.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key读取对应的value
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        if (_prop == null) {
            _prop = new Properties();
            readProperties("conf/system.properties");
        }
        return _prop.getProperty(key);
    }

    public static void main(String[] args) {
        String url = PropertiesUtil.getProperty("language");
        System.out.println(url);
    }

}

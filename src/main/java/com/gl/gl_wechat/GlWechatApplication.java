package com.gl.gl_wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.gl.gl_wechat.dao")
public class GlWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlWechatApplication.class, args);
    }

}

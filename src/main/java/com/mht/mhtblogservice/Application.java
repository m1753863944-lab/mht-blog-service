package com.mht.mhtblogservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mht.mhtblogservice.mapper") // 🌟 极其重要！精准指向你 Mapper 接口所在的包路径
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("🚀 mht博客后端一键启动成功！");
    }
}

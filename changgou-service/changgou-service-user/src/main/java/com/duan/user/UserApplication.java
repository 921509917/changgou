package com.duan.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName UserApplication
 * @Author DuanJinFei
 * @Date 2021/4/6 11:21
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.duan.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class);
    }
}

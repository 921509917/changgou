package com.duan.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName EurekaSpringBootApplication
 * @Author DuanJinFei
 * @Date 2021/3/30 22:56
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaServer // 开启Eureka服务
public class EurekaSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaSpringBootApplication.class);
    }
}

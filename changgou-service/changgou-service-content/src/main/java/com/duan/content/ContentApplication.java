package com.duan.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName ContentApplication
 * @Author DuanJinFei
 * @Date 2021/4/1 21:20
 * @Version 1.0
 */

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.duan.content.mapper"})
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class);
    }
}

package com.duan.canal;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName CanalApplication
 * @Author DuanJinFei
 * @Date 2021/4/1 18:34
 * @Version 1.0
 */
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.duan.content.feign"})
@EnableCanalClient // 启用Canal
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CanalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class,args);
    }
}

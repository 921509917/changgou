package com.duan.order;

import com.duan.entity.IdWorker;
import com.duan.entity.FeignHeaderInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName OrderApplication
 * @Author DuanJinFei
 * @Date 2021/4/12 8:36
 * @Version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages = {"com.duan.order.mapper"})
@EnableFeignClients({"com.duan.goods.feign","com.duan.user.feign","com.duan.pay.feign"})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class);
    }

    @Bean
    public FeignHeaderInterceptor feignHeaderInterceptor() {
        return new FeignHeaderInterceptor();
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(0,0);
    }
}

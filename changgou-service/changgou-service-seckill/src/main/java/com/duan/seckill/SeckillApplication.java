package com.duan.seckill;

import com.duan.entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName SeckillApplication
 * @Author DuanJinFei
 * @Date 2021/4/14 9:52
 * @Version 1.0
 */


@EnableScheduling   //开启对定时任务的支持
@EnableAsync        //开启对异步任务的支持
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.duan.seckill.feign")
@MapperScan(basePackages = {"com.duan.seckill.mapper"})
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }
}

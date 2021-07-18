package com.duan.goods;

import com.duan.entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName GoodsApplicatioon
 * @Author DuanJinFei
 * @Date 2021/3/30 23:35
 * @Version 1.0
 */
@EnableEurekaClient // 开启Eureka客户端
@SpringBootApplication
@EnableTransactionManagement // 开启事务管理
@MapperScan("com.duan.goods.mapper")
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class);
    }
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(0, 1);
    }
}

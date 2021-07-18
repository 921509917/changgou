package com.duan.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @ClassName SearchApplication
 * @Author DuanJinFei
 * @Date 2021/4/3 16:18
 * @Version 1.0
 */
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.duan.goods.feign") // 前期导入商品 需要依赖goods服务
@EnableElasticsearchRepositories(basePackages = "com.duan.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SearchApplication {
    public static void main(String[] args) {
        /**
         * SpringBoot整合ElasticSearch在项目启动前设置一下的属性，防止报错
         * 解决netty冲突后初始化client时还会抛出异常
         * availableProcessors is already set to [12],rejecting[12]
         */
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(SearchApplication.class);
    }
}

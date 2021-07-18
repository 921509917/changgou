package com.duan.gateWay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @ClassName GatewayApplication
 * @Author DuanJinFei
 * @Date 2021/4/6 9:59
 * @Version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class);
    }

    // 需要限流Key，用IP来当做限流的Key，限制某一个IP在一定的时间段的访问次数
    @Bean(name = "ipKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName();
            return Mono.just(ip);
        };
    }
}

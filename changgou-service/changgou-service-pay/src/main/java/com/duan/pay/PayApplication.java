package com.duan.pay;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @ClassName PayApplication
 * @Author DuanJinFei
 * @Date 2021/4/13 21:59
 * @Version 1.0
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class);
    }

    @Autowired
    private Environment env;

    /***
     * 创建DirectExchange交换机
     * @return
     */
    @Bean
    public DirectExchange basicExchange(){

        return new DirectExchange(env.getProperty("mq.pay.exchange.order"), true,false);
    }

    /***
     * 创建队列
     * @return
     */
    @Bean(name = "queueOrder")
    public Queue queueOrder(){
        return new Queue(env.getProperty("mq.pay.queue.order"), true);
    }

    /****
     * 队列绑定到交换机上
     * @return
     */
    @Bean
    public Binding basicBinding(){
        return BindingBuilder.bind(queueOrder()).to(basicExchange()).with(env.getProperty("mq.pay.routing.key"));
    }
}

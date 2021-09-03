package com.duan.order.controller;

import com.duan.order.service.OrderService;
import com.netflix.hystrix.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @ClassName GetOrderCommand
 * @Author DuanJinFei
 * @Date 2021/9/1 10:30
 * @Version 1.0
 */
public class GetOrderCommand extends HystrixCommand<List> {

    private OrderService orderService;

    public GetOrderCommand(String name){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ThreadPoolTestGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("testCommandKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(name))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(5000)
                )
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                //配置队列大小
                                .withMaxQueueSize(10)
                                // 配置线程池里的线程数
                                .withCoreSize(2)
                )
        );
    }

    @Override
    protected List run() throws Exception {
        return orderService.findAll();
    }

    public static class UnitTest {
        @Test
        public void testGetOrder(){
//            new GetOrderCommand("hystrix-order").execute();
            Future<List> future =new GetOrderCommand("hystrix-order").queue();
        }

    }
}

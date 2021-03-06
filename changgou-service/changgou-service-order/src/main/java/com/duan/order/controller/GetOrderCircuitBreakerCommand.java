package com.duan.order.controller;

import com.netflix.hystrix.*;
import org.junit.Test;

import java.util.Random;

/**
 * @ClassName GetOrderCircuitBreakerCommand
 * @Author DuanJinFei
 * @Date 2021/9/1 10:49
 * @Version 1.0
 */
public class GetOrderCircuitBreakerCommand extends HystrixCommand<String> {

    public GetOrderCircuitBreakerCommand(String name){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ThreadPoolTestGroup"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("testCommandKey"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(name))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        //默认是true，本例中为了展现该参数
                                        .withCircuitBreakerEnabled(true)
                                        //默认是false，本例中为了展现该参数
                                        .withCircuitBreakerForceOpen(false)
                                        //默认是false，本例中为了展现该参数
                                        .withCircuitBreakerForceClosed(false)
                                        //(1)错误百分比超过5%
                                        .withCircuitBreakerErrorThresholdPercentage(5)
                                        //(2)10s以内调用次数10次，同时满足(1)(2)熔断器打开
                                        .withCircuitBreakerRequestVolumeThreshold(10)
                                        //隔5s之后，熔断器会尝试半开(关闭)，重新放进来请求
                                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
//                                .withExecutionTimeoutInMilliseconds(1000)
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
    protected String run() throws Exception {
        Random rand = new Random();
        //模拟错误百分比(方式比较粗鲁但可以证明问题)
        if(1==rand.nextInt(2)){
//            System.out.println("make exception");
            throw new Exception("make exception");
        }
        return "running:  ";
    }

    @Override
    protected String getFallback() {
//        System.out.println("FAILBACK");
        return "fallback: ";
    }

    public static class UnitTest{

        @Test
        public void testCircuitBreaker() throws Exception{
            for(int i=0;i<25;i++){
                Thread.sleep(500);
                HystrixCommand<String> command = new GetOrderCircuitBreakerCommand("testCircuitBreaker");
                String result = command.execute();
                //本例子中从第11次，熔断器开始打开
                System.out.println("call times:"+(i+1)+"   result:"+result +" isCircuitBreakerOpen: "+command.isCircuitBreakerOpen());
                //本例子中5s以后，熔断器尝试关闭，放开新的请求进来
            }
        }
    }
}

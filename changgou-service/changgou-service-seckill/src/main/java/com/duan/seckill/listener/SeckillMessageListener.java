package com.duan.seckill.listener;

import com.alibaba.fastjson.JSONObject;
import com.duan.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName SeckillMessageListener
 * @Author DuanJinFei
 * @Date 2021/4/14 10:29
 * @Version 1.0
 */
@Component
@RabbitListener(queues = "queue.seckillorder")
public class SeckillMessageListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    //https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7&index=8
    @RabbitHandler
    public void getMessage(String message) {
        try {
            // 将消息转为Map集合
            Map<String, String> resultMap = JSONObject.parseObject(message,Map.class);
            String returnCode = resultMap.get("return_code");   //状态码

            if ("SUCCESS".equals(returnCode)) {
                //业务结果
                String resultCode = resultMap.get("result_code");
                String attach = resultMap.get("attach");

                Map<String,String> attachMap = JSONObject.parseObject(attach,Map.class);

                if ("SUCCESS".equals(resultCode)) {
                    //改订单状态
                    seckillOrderService.updatePayStatus(attachMap.get("username"),
                            resultMap.get("transaction_id"),resultMap.get("time_end"));
                } else {
                    //删除订单
                    seckillOrderService.deleteOrder(attachMap.get("username"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


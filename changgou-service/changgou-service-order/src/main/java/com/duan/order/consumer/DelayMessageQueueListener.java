package com.duan.order.consumer;

import com.duan.order.service.OrderService;
import com.duan.pay.feign.WeChatPayFeign;
import com.duan.order.pojo.Order;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName DelayMessageQueueListener
 * @Author DuanJinFei
 * @Date 2021/4/13 22:28
 * @Version 1.0
 */
@Component
@RabbitListener(queues = "orderListenerQueue")
public class DelayMessageQueueListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeChatPayFeign weChatPayFeign;

    @RabbitHandler
    public void getMessage(String orderId) throws Exception {
        Order order = orderService.findById(orderId);
        if ("0".equals(order.getPayStatus())) {
            //0表示未支付，通知微信服务器取消订单，从数据库中删除订单，回滚库存
            Map<String, String> closeResult = weChatPayFeign.closeOrder(orderId).getData();
            //如果错误代码为ORDERPAID则说明订单已经支付，当作正常订单处理,反之 回滚库存
            if (!("FAIL".equals(closeResult.get("result_code")) && "ORDERPAID".equals(closeResult.get("err_code")))) {
                orderService.deleteOrder(orderId);
            }
        }
    }

}


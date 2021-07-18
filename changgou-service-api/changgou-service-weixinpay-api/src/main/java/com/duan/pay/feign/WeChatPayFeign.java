package com.duan.pay.feign;

/**
 * @ClassName WeChatPayFeign
 * @Author DuanJinFei
 * @Date 2021/4/13 22:31
 * @Version 1.0
 */
import com.duan.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient("wechatpay")
@RequestMapping("/wechat/pay")
public interface WeChatPayFeign {

    /**
     * 关闭订单
     * @param outTradeNo    商户订单号
     * @return
     * @throws Exception
     */
    @RequestMapping("/cancel/order")
    Result<Map<String,String>> closeOrder(String outTradeNo) throws Exception;

}

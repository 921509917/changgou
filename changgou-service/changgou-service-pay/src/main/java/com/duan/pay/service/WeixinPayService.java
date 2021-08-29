package com.duan.pay.service;

import java.util.Map;

/**
 * @ClassName WeixinPayService
 * @Author DuanJinFei
 * @Date 2021/4/13 22:00
 * @Version 1.0
 */
public interface WeixinPayService {

    /*****
     * 创建二维码
     * @param out_trade_no : 客户端自定义订单编号
     * @param total_fee    : 交易金额,单位：分
     * @return
     */
    public Map<String,String> createNative(String out_trade_no, String total_fee);
    /***
     * 查询订单状态
     * @param out_trade_no : 客户端自定义订单编号
     * @return
     */
    public Map<String,String> queryPayStatus(String out_trade_no);
}

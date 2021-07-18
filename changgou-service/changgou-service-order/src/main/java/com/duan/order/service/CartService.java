package com.duan.order.service;

import com.duan.order.pojo.OrderItem;

import java.util.List;

/**
 * @ClassName CartService
 * @Author DuanJinFei
 * @Date 2021/4/12 9:21
 * @Version 1.0
 */
public interface CartService {
    /**
     * 添加到购物车
     * @param id
     * @param num
     * @param username
     */
    void add(long id, int num, String username);

    /**
     * 查询购物车数据
     * @param username  用户名
     * @return  订单集合数据
     */
    List<OrderItem> list(String username);
}

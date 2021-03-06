package com.duan.order.controller;

import com.duan.entity.Result;
import com.duan.entity.StatusCode;
import com.duan.order.service.CartService;
import com.duan.order.utils.TokenDecodeUtil;
import com.duan.order.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CartController
 * @Author DuanJinFei
 * @Date 2021/4/12 9:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecodeUtil tokenDecodeUtil;

    /**
     * 添加商品到购物车
     * @param id    商品sku的id
     * @param num   商品的数量
     * @return
     */
    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Result add(long id, int num) {
        String username = tokenDecodeUtil.getUserInfo().get("username");
        cartService.add(id,num,username);
        return new Result(true, StatusCode.OK,"成功添加到购物车");
    }

    /**
     * 查询购物车数据
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Result<List<OrderItem>> list() {
        String username = tokenDecodeUtil.getUserInfo().get("username");
        List<OrderItem> orderItems = cartService.list(username);
        return new Result<>(true,StatusCode.OK,"查询购物车成功",orderItems);
    }

}

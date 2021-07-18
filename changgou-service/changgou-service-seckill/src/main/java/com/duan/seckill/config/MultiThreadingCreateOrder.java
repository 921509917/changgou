package com.duan.seckill.config;

import com.duan.seckill.mapper.SeckillGoodsMapper;
import com.duan.entity.SystemConstants;
import com.duan.seckill.pojo.SeckillGoods;
import com.duan.seckill.pojo.SeckillOrder;
import com.duan.seckill.pojo.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @ClassName MultiThreadingCreateOrder
 * @Author DuanJinFei
 * 处理redis队列中的秒杀消息
 * @Date 2021/4/14 10:09
 * @Version 1.0
 */
@Component
public class MultiThreadingCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 异步抢单
     */
    @Async  //声明该方法是个异步任务，另开一个线程去运行
    public void createOrder() {
        //从Redis队列中取出 秒杀的订单信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).rightPop();
        // 通过key值获取商品的键值对
        BoundHashOperations seckillGoodsBoundHashOps = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + seckillStatus.getTime());

        //从redis中查询出秒杀商品
        SeckillGoods seckillGoods = (SeckillGoods)seckillGoodsBoundHashOps.get(seckillStatus.getGoodsId());

        if (seckillGoods == null || seckillGoods.getStockCount() <=0 ) {
            throw new RuntimeException("已售罄");
        }

        //从秒杀商品队列中获取数据，如果获取不到则说明已经卖完了，清除掉排队信息
        Object o = redisTemplate.boundListOps(SystemConstants.SEC_KILL_GOODS_COUNT_LIST + seckillGoods.getId()).rightPop();

        if (o == null) {
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_QUEUE_COUNT).delete(seckillStatus.getUsername());  //清除排队队列
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).delete(seckillStatus.getUsername());   //排队状态队列
            return;
        }

        //创建秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setSeckillId(seckillGoods.getId());
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(seckillStatus.getUsername());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");
        //将秒杀订单存入redis，键为用户名，确保一个用户只有一个秒杀订单
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(seckillStatus.getUsername(),seckillOrder);

        //减库存，如果库存没了就从redis中删除，并将库存数据写到MySQL中
        //seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        Long size = redisTemplate.boundListOps(SystemConstants.SEC_KILL_GOODS_COUNT_LIST + seckillGoods.getId()).size();//获取库存
        //if (seckillGoods.getStockCount() <= 0) {
        seckillGoods.setNum(size.intValue());
        // 如果redis中没有库存了 就会将队列信息进行删除，并且更新数据的库存信息
        if (size <= 0) {
            seckillGoodsBoundHashOps.delete(seckillStatus.getGoodsId());
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
        } else {
            /**
             * 将商品id 和秒杀订单 存入redis中
             */
            seckillGoodsBoundHashOps.put(seckillStatus.getGoodsId(),seckillGoods);
        }
        //下单成功，更改seckillstatus的状态，再存入redis中
        seckillStatus.setOrderId(seckillOrder.getId());
        seckillStatus.setMoney(Float.valueOf(seckillGoods.getCostPrice()));
        seckillStatus.setStatus(2);
        // 将目前秒杀订单状态更新至redis
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(seckillStatus.getUsername(),seckillStatus);
    }

}

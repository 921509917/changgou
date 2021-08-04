package com.duan.seckill.config;

import com.duan.entity.IdWorker;
import com.duan.seckill.mapper.SeckillGoodsMapper;
import com.duan.entity.DateUtil;
import com.duan.entity.SystemConstants;
import com.duan.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SeckillGoodsPushTask
 * @Author DuanJinFei
 * @Date 2021/4/14 9:30
 * @Version 1.0
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsMapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // 定时任务器
    @Scheduled(cron = "0/5 * * * * ?")
    public void loadGoodsPushRedis(){
        // 获取前端秒杀列表的时间集合
        List<Date> dateMenu = DateUtil.getDateMenus();

        for (Date date : dateMenu) {
            //2019-6-1 为了方便测试
            date.setYear(2021-1900);
            date.setMonth(Calendar.APRIL);
            date.setDate(1);
            String dateString = SystemConstants.SEC_KILL_GOODS_PREFIX +DateUtil.data2str(date,"yyyyMMddHH");

            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(dateString);
            //获取Redis中已有的商品的id集合
            Set<Long> keys = boundHashOperations.keys();
            List<SeckillGoods> seckillGoods;
            //将秒杀商品的信息从数据库中加载出来
            if (keys!=null && keys.size()>0) {
                seckillGoods = mapper.findSeckillGoodsNotIn(date,keys);
            } else {
                seckillGoods = mapper.findSeckillGoods(date);
            }
            // 将商品添加到Redis中并且添加到Redis队列中
            for (SeckillGoods seckillGood : seckillGoods) {
                //把商品存入到redis
                boundHashOperations.put(seckillGood.getId(),seckillGood);
                //存到Redis队列
                redisTemplate.boundListOps(SystemConstants.SEC_KILL_GOODS_COUNT_LIST + seckillGood.getId())
                        .leftPushAll(getGoodsNumber(seckillGood.getNum()));
            }
        }
    }
    //获取秒杀商品数量的数组
    public Byte[] getGoodsNumber(int num) {
        Byte[] arr = new Byte[num];
        for (int i = 0; i < num; i++) {
            arr[i] = '0';
        }
        return arr;
    }
}

package com.duan.seckill.service.impl;
import com.duan.entity.SystemConstants;
import com.duan.seckill.config.MultiThreadingCreateOrder;
import com.duan.seckill.mapper.SeckillGoodsMapper;
import com.duan.seckill.mapper.SeckillOrderMapper;
import com.duan.seckill.pojo.SeckillGoods;
import com.duan.seckill.pojo.SeckillOrder;
import com.duan.seckill.pojo.SeckillStatus;
import com.duan.seckill.service.SeckillOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Override
    public SeckillStatus queryStatus(String username) {
        return (SeckillStatus) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).get(username);
    }

    @Override
    public void updatePayStatus(String username, String transactionId, String endTime) {
        //从Redis中将订单信息查询出来
        SeckillOrder order = (SeckillOrder) redisTemplate
                .boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY)
                .get(username);
        if (order != null) {
            try {
                order.setStatus("1");
                order.setTransactionId(transactionId);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                order.setPayTime(simpleDateFormat.parse(endTime));
                //将订单信息存到mysql中
                seckillOrderMapper.insertSelective(order);

                //删除redis中的订单信息
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).delete(username);

                //删除用户的排队信息
                //清除排队队列
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_QUEUE_COUNT).delete(username);
                //排队状态队列
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).delete(username);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteOrder(String username) {
        //删除Redis中的订单
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).delete(username);

        //删除用户的排队信息
        //清除排队队列
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_QUEUE_COUNT).delete(username);
        //排队状态队列
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).delete(username);

        //查询出秒杀的状态信息
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY)
                .get(username);

        //回滚库存
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate
                .boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + seckillStatus.getTime())
                .get(seckillStatus.getGoodsId());
        if (seckillGoods == null) {
            seckillGoodsMapper.selectByPrimaryKey(seckillGoods.getId());
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
        } else {
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
        }
        redisTemplate
                .boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + seckillStatus.getTime())
                .put(seckillGoods.getId(),seckillGoods);

        //将商品放入队列
        redisTemplate.boundListOps(SystemConstants.SEC_KILL_GOODS_COUNT_LIST + seckillGoods.getId())
                .leftPush("0");
    }

    @Override
    public boolean add(Long id, String time, String username) {
        // 记录用户的秒杀数量
        Long increment = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_QUEUE_COUNT).increment(username, 1);
        //记录指定hashkey的增量，大于1说明排队次数超过1次，重复排队
        if (increment>1) {
            throw new RuntimeException("重复排队");
        }
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(),1,id,time);
        //将seckillStatus存入redis队列
        redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).leftPush(seckillStatus);
        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(username,seckillStatus);
        multiThreadingCreateOrder.createOrder();
        return true;
    }
    /**
     * SeckillOrder条件+分页查询
     * @param seckillOrder 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder){
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder){
        Example example=new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillOrder!=null){
            // 主键
            if(!StringUtils.isEmpty(seckillOrder.getId())){
                    criteria.andEqualTo("id",seckillOrder.getId());
            }
            // 秒杀商品ID
            if(!StringUtils.isEmpty(seckillOrder.getSeckillId())){
                    criteria.andEqualTo("seckillId",seckillOrder.getSeckillId());
            }
            // 支付金额
            if(!StringUtils.isEmpty(seckillOrder.getMoney())){
                    criteria.andEqualTo("money",seckillOrder.getMoney());
            }
            // 用户
            if(!StringUtils.isEmpty(seckillOrder.getUserId())){
                    criteria.andEqualTo("userId",seckillOrder.getUserId());
            }
            // 商家
            if(!StringUtils.isEmpty(seckillOrder.getSellerId())){
                    criteria.andEqualTo("sellerId",seckillOrder.getSellerId());
            }
            // 创建时间
            if(!StringUtils.isEmpty(seckillOrder.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillOrder.getCreateTime());
            }
            // 支付时间
            if(!StringUtils.isEmpty(seckillOrder.getPayTime())){
                    criteria.andEqualTo("payTime",seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if(!StringUtils.isEmpty(seckillOrder.getStatus())){
                    criteria.andEqualTo("status",seckillOrder.getStatus());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(seckillOrder.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if(!StringUtils.isEmpty(seckillOrder.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",seckillOrder.getReceiverMobile());
            }
            // 收货人
            if(!StringUtils.isEmpty(seckillOrder.getReceiver())){
                    criteria.andEqualTo("receiver",seckillOrder.getReceiver());
            }
            // 交易流水
            if(!StringUtils.isEmpty(seckillOrder.getTransactionId())){
                    criteria.andEqualTo("transactionId",seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 增加SeckillOrder
     * @param seckillOrder
     */
    @Override
    public void add(SeckillOrder seckillOrder){
        seckillOrderMapper.insert(seckillOrder);
    }

    /**
     * 根据ID查询SeckillOrder
     * @param id
     * @return
     */
    @Override
    public SeckillOrder findById(Long id){
        return  seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询SeckillOrder全部数据
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }
}

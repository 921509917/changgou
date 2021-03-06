package com.duan.order.service.impl;
import com.duan.entity.IdWorker;
import com.duan.goods.pojo.Sku;
import com.duan.order.mapper.OrderItemMapper;
import com.duan.order.mapper.OrderMapper;
import com.duan.order.pojo.Order;
import com.duan.order.pojo.OrderItem;
import com.duan.order.service.OrderService;
import com.duan.user.feign.UserFeign;
import com.duan.goods.feign.SkuFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/****
 * @Author:admin
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * Order条件+分页查询
     * @param order 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order条件查询
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order){
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }


    /**
     * Order构建查询对象
     * @param order
     * @return
     */
    public Example createExample(Order order){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(order!=null){
            // 订单id
            if(!StringUtils.isEmpty(order.getId())){
                    criteria.andEqualTo("id",order.getId());
            }
            // 数量合计
            if(!StringUtils.isEmpty(order.getTotalNum())){
                    criteria.andEqualTo("totalNum",order.getTotalNum());
            }
            // 金额合计
            if(!StringUtils.isEmpty(order.getTotalMoney())){
                    criteria.andEqualTo("totalMoney",order.getTotalMoney());
            }
            // 优惠金额
            if(!StringUtils.isEmpty(order.getPreMoney())){
                    criteria.andEqualTo("preMoney",order.getPreMoney());
            }
            // 邮费
            if(!StringUtils.isEmpty(order.getPostFee())){
                    criteria.andEqualTo("postFee",order.getPostFee());
            }
            // 实付金额
            if(!StringUtils.isEmpty(order.getPayMoney())){
                    criteria.andEqualTo("payMoney",order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if(!StringUtils.isEmpty(order.getPayType())){
                    criteria.andEqualTo("payType",order.getPayType());
            }
            // 订单创建时间
            if(!StringUtils.isEmpty(order.getCreateTime())){
                    criteria.andEqualTo("createTime",order.getCreateTime());
            }
            // 订单更新时间
            if(!StringUtils.isEmpty(order.getUpdateTime())){
                    criteria.andEqualTo("updateTime",order.getUpdateTime());
            }
            // 付款时间
            if(!StringUtils.isEmpty(order.getPayTime())){
                    criteria.andEqualTo("payTime",order.getPayTime());
            }
            // 发货时间
            if(!StringUtils.isEmpty(order.getConsignTime())){
                    criteria.andEqualTo("consignTime",order.getConsignTime());
            }
            // 交易完成时间
            if(!StringUtils.isEmpty(order.getEndTime())){
                    criteria.andEqualTo("endTime",order.getEndTime());
            }
            // 交易关闭时间
            if(!StringUtils.isEmpty(order.getCloseTime())){
                    criteria.andEqualTo("closeTime",order.getCloseTime());
            }
            // 物流名称
            if(!StringUtils.isEmpty(order.getShippingName())){
                    criteria.andEqualTo("shippingName",order.getShippingName());
            }
            // 物流单号
            if(!StringUtils.isEmpty(order.getShippingCode())){
                    criteria.andEqualTo("shippingCode",order.getShippingCode());
            }
            // 用户名称
            if(!StringUtils.isEmpty(order.getUsername())){
                    criteria.andLike("username","%"+order.getUsername()+"%");
            }
            // 买家留言
            if(!StringUtils.isEmpty(order.getBuyerMessage())){
                    criteria.andEqualTo("buyerMessage",order.getBuyerMessage());
            }
            // 是否评价
            if(!StringUtils.isEmpty(order.getBuyerRate())){
                    criteria.andEqualTo("buyerRate",order.getBuyerRate());
            }
            // 收货人
            if(!StringUtils.isEmpty(order.getReceiverContact())){
                    criteria.andEqualTo("receiverContact",order.getReceiverContact());
            }
            // 收货人手机
            if(!StringUtils.isEmpty(order.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",order.getReceiverMobile());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(order.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(!StringUtils.isEmpty(order.getSourceType())){
                    criteria.andEqualTo("sourceType",order.getSourceType());
            }
            // 交易流水号
            if(!StringUtils.isEmpty(order.getTransactionId())){
                    criteria.andEqualTo("transactionId",order.getTransactionId());
            }
            // 订单状态 
            if(!StringUtils.isEmpty(order.getOrderStatus())){
                    criteria.andEqualTo("orderStatus",order.getOrderStatus());
            }
            // 支付状态 0:未支付 1:已支付
            if(!StringUtils.isEmpty(order.getPayStatus())){
                    criteria.andEqualTo("payStatus",order.getPayStatus());
            }
            // 发货状态 0:未发货 1:已发货 2:已送达
            if(!StringUtils.isEmpty(order.getConsignStatus())){
                    criteria.andEqualTo("consignStatus",order.getConsignStatus());
            }
            // 是否删除
            if(!StringUtils.isEmpty(order.getIsDelete())){
                    criteria.andEqualTo("isDelete",order.getIsDelete());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Order
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 增加Order
     * @param order
     */
    @Override
    public synchronized void add(Order order) {
        order.setId(String.valueOf(idWorker.nextId()));
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps("Cart_" + order.getUsername());
        //总数量，总金额
        int totalNum = 0 , totalMoney = 0;
        // 解决LocalDateTime转为Date问题
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        //从购物车中获取订单明细
        List<OrderItem> orderItems = boundHashOperations.values();
        if (CollectionUtils.isEmpty(orderItems) || orderItems.size()==0) {
            throw new RuntimeException("购物车数据异常,下单失败");
        }
        //数据库中对应的sku集合
        List<Sku> skuList = skuFeign.findBySkuIds(order.getSkuIds()).getData();
        //如果数据库中查询出来的sku集合数量与前端传过来的sku数量不一致，说明数据有误，下单失败
        if (skuList.size() != order.getSkuIds().size()){
            throw new RuntimeException("sku数据库数据异常,下单失败");
        }
        Map<Long,Sku> skuMap = skuList.stream().collect(Collectors.toMap(Sku::getId, a -> a,(a1,a2)->a1));
        //遍历购物车中的数据，判断是否是选中的，将选中的订单明细数据补充完整
        for (OrderItem orderItem : orderItems) {
            //判断当前遍历到的orderItem是否是选中的
            if (order.getSkuIds().contains(orderItem.getSkuId())) {
                orderItem.setId(String.valueOf(idWorker.nextId()));
                orderItem.setOrderId(order.getId());
                orderItem.setIsReturn("0");
                //数据库中的sku
                Sku sku = skuMap.get(orderItem.getSkuId());
                //判断库存是否充足，不足则报异常订单提交失败
                if (orderItem.getNum() <= sku.getNum()) {
                    totalNum += orderItem.getNum();
                } else {
                    throw new RuntimeException("库存不足,下单失败");
                }
                totalMoney += sku.getPrice();
            }
        }
        //减库存，删购物车
        for (OrderItem orderItem : orderItems) {
            if (order.getSkuIds().contains(orderItem.getSkuId())) {
                //数据库中的sku
                Sku sku = skuMap.get(orderItem.getSkuId());
                //减库存
                sku.setNum(sku.getNum() - orderItem.getNum());
                //删购物车
                boundHashOperations.delete(orderItem.getSkuId());
                //添加到订单明细表
                orderItemMapper.insertSelective(orderItem);
            }
        }
        //将sku信息提交到数据库中的sku表
        skuFeign.updateMap(skuMap);

        order.setCreateTime(Date.from(localDateTime.atZone(zoneId).toInstant()));
        order.setUpdateTime(Date.from(localDateTime.atZone(zoneId).toInstant()));
        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        //1.web
        order.setSourceType("1");
        order.setOrderStatus("0");
        order.setPayStatus("0");
        order.setIsDelete("0");
        //添加到订单表
        orderMapper.insertSelective(order);
        //添加积分，1块钱1分
        userFeign.addPoints(totalMoney);

        rabbitTemplate.convertAndSend("orderDelayQueue", order.getId(), message -> {
                    message.getMessageProperties().setExpiration("7000");   //定时7秒过时
                    return message;
        });
    }

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Order全部数据
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    @Override
    public void updateStatus(String orderId, String transactionid) {
        //1.修改订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        //时间也可以从微信接口返回过来，这里为了方便，我们就直接使用当前时间了
        order.setUpdateTime(new Date());
        //不允许这么写
        order.setPayTime(order.getUpdateTime());
        //交易流水号
        order.setTransactionId(transactionid);
        //已支付
        order.setPayStatus("1");
        orderMapper.updateByPrimaryKeySelective(order);

        //2.删除Redis中的订单记录
        redisTemplate.boundHashOps("Order").delete(orderId);
    }

    /***
     * 订单的删除操作
     */
    @Override
    public void deleteOrder(String id) {
        //改状态
        Order order = (Order) redisTemplate.boundHashOps("Order").get(id);
        order.setUpdateTime(new Date());
        order.setPayStatus("2");    //支付失败
        orderMapper.updateByPrimaryKeySelective(order);

        //删除缓存
        redisTemplate.boundHashOps("Order").delete(id);
    }
}

package com.duan.order.mapper;
import com.duan.order.pojo.OrderItem;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:OrderItem的Dao
 * @Date 2019/6/14 0:12
 *****/
@Repository
public interface OrderItemMapper extends Mapper<OrderItem> {
}

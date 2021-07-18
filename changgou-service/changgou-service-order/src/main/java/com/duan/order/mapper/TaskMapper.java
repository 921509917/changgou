package com.duan.order.mapper;
import com.duan.order.pojo.Task;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:Task的Dao
 * @Date 2019/6/14 0:12
 *****/
@Repository
public interface TaskMapper extends Mapper<Task> {
}

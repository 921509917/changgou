package com.duan.goods.mapper;
import com.duan.goods.pojo.Pref;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:Pref的Dao
 * @Date 2019/6/14 0:12
 *****/
@Repository("prefMapper")
public interface PrefMapper extends Mapper<Pref> {
}

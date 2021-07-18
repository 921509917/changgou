package com.duan.goods.mapper;
import com.duan.goods.pojo.Spu;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:Spu的Dao
 * @Date 2019/6/14 0:12
 *****/
@Repository("spuMapper")
public interface SpuMapper extends Mapper<Spu> {
}

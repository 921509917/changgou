package com.duan.goods.mapper;
import com.duan.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
@Repository("skuMapper")
public interface SkuMapper extends Mapper<Sku> {


    @Select("select * from tb_sku where id in (${skuIds})")
    List<Sku> findBySkuIds(@Param("skuIds") String skuIds);
}

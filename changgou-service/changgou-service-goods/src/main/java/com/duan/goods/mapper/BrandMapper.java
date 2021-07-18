package com.duan.goods.mapper;

import com.duan.goods.pojo.Brand;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName BrandMapper
 * @Author DuanJinFei
 * @Date 2021/3/30 23:37
 * @Version 1.0
 */
@Repository("brandMapper")
public interface BrandMapper extends Mapper<Brand> {
    /**
     * 查询所有的品牌信息
     * @return
     */
    @Select("select * from tb_brand")
    List<Brand> findAll();

    /***
     * 查询分类对应的品牌集合
     */
    @Select("SELECT tb.* FROM tb_category_brand tcb,tb_brand tb WHERE tcb.category_id=#{categoryId} AND tb.id=tcb.brand_id")
    List<Brand> findByCategory(Integer categoryId);

}

package com.duan.goods.mapper;
import com.duan.goods.pojo.CategoryBrand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:CategoryBrand的Dao
 * @Date 2019/6/14 0:12
 *****/
@Repository("categoryBrandMapper")
public interface CategoryBrandMapper extends Mapper<CategoryBrand> {
}

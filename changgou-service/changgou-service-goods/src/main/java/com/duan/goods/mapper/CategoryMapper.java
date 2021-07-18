package com.duan.goods.mapper;

import com.duan.goods.pojo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName CategoryMapper
 * @Author DuanJinFei
 * @Date 2021/3/31 21:27
 * @Version 1.0
 */
@Repository("categoryMapper")
public interface CategoryMapper extends Mapper<Category> {
}

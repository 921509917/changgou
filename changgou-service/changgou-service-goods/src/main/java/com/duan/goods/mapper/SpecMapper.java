package com.duan.goods.mapper;

import com.duan.goods.pojo.Spec;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName SpecMapper
 * @Author DuanJinFei
 * @Date 2021/3/31 21:22
 * @Version 1.0
 */
@Repository("specMapper")
public interface SpecMapper extends Mapper<Spec> {
}

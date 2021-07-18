package com.duan.goods.mapper;

import com.duan.goods.pojo.Template;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName TemplateMapper
 * @Author DuanJinFei
 * @Date 2021/3/31 21:19
 * @Version 1.0
 */
@Repository("templateMapper")
public interface TemplateMapper extends Mapper<Template> {

}

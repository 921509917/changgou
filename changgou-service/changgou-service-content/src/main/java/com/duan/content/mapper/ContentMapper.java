package com.duan.content.mapper;
import com.duan.content.pojo.Content;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:Content的Dao
 * @Date 2019/6/14 0:12
 *****/
@Repository("contentMapper")
public interface ContentMapper extends Mapper<Content> {
}

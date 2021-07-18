package com.duan.user.mapper;
import com.duan.user.pojo.Address;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Address的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface AddressMapper extends Mapper<Address> {
    //	AddressMapper
    @Select("select * from tb_address where username = #{username}")
    List<Address> list(String username);
}

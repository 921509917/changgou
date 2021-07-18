package com.duan.seckill.mapper;

import com.duan.seckill.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SeckillGoodsMapper
 * @Author DuanJinFei
 * 秒杀商品===》数据库
 * @Date 2021/4/14 9:33
 * @Version 1.0
 */
@Repository("seckillGoodsMapper")
public interface SeckillGoodsMapper extends Mapper<SeckillGoods> {

    //查找符合条件的秒杀商品
    @Select("SELECT" +
            " * " +
            " FROM " +
            " tb_seckill_goods " +
            " WHERE " +
            " status = 1 " +
            " AND stock_count > 0 " +
            " AND start_time >= #{date} " +
            " AND end_time < DATE_ADD(#{date},INTERVAL 2 HOUR)")
    List<SeckillGoods> findSeckillGoods(@Param("date") Date date);

    //查询出符合条件的秒杀商品，排除之前已存入的
    @SelectProvider(type = SeckillGoodsMapper.SeckillProvider.class, method = "findSeckillGoodsNotIn")
    List<SeckillGoods> findSeckillGoodsNotIn(@Param("date") Date date, @Param("keys") Set<Long> keys);

    class SeckillProvider {
        public String findSeckillGoodsNotIn(@Param("date") Date date, @Param("keys") Set<Long> keys) {
            StringBuilder sql = new StringBuilder("SELECT" +
                    " * " +
                    " FROM " +
                    " tb_seckill_goods " +
                    " WHERE " +
                    " status = 1 " +
                    " AND stock_count > 0 " +
                    " AND start_time >=  ");

            sql.append("'").append(date.toLocaleString()).append("'")
                    .append(" AND end_time < DATE_ADD(")
                    .append("'").append(date.toLocaleString()).append("'")
                    .append(" ,INTERVAL 2 HOUR) ")
                    .append(" AND id NOT IN (");

            for (Long key : keys) {
                sql.append(key).append(",");
            }

            sql.deleteCharAt(sql.length() - 1).append(")");
            System.out.println(sql.toString());

            return sql.toString();
        }
    }

}

package com.duan.goods.mapper;

import com.duan.goods.pojo.Album;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName AlbumMapper
 * @Author DuanJinFei
 * @Date 2021/3/31 19:53
 * @Version 1.0
 */
@Repository("albumMapper")
public interface AlbumMapper extends Mapper<Album> {
}

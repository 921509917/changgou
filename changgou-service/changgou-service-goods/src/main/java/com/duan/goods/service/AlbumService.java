package com.duan.goods.service;

import com.duan.goods.pojo.Album;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName AlbumService
 * @Author DuanJinFei
 * @Date 2021/3/31 20:57
 * @Version 1.0
 */
public interface AlbumService {
    /***
     * Album多条件分页查询
     * @param album
     * @param page
     * @param size
     * @return
     */
    PageInfo<Album> findPage(Album album, int page, int size);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Album> findPage(int page, int size);

    /**
     * Album多条件搜索
     * @param album
     * @return
     */
    List<Album> findList(Album album);
    /***
     * 删除Album
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Album数据
     * @param album
     */
    void update(Album album);

    /***
     * 新增Album
     * @param album
     */
    void add(Album album);

    /**
     * 根据ID查询Album
     * @param id
     * @return
     */
    Album findById(Long id);

    /***
     * 查询所有Album
     * @return
     */
    List<Album> findAll();
}

package com.duan.goods.service;

import com.duan.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName BrandService
 * @Author DuanJinFei
 * @Date 2021/3/30 23:37
 * @Version 1.0
 */
public interface BrandService {
    /**
     * 查找所有品牌信息
     * @return
     */
    List<Brand> findAll();
    /**
     * 通过Id查找品牌
     * @param id
     * @return
     */
    Brand findById(Integer id);
    /**
     * 添加品牌
     * @param brand
     */
    void  add(Brand brand);
    /**
     * 更新品牌
     * @param brand
     */
    void update(Brand brand);
    /**
     * 删除品牌
     * @param id
     */
    void delete(Integer id);
    /**
     * 多条件搜索品牌
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);
    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(int page, int size);

    /**
     * 多条件分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(Brand brand, int page, int size);
    /***
     * 根据分类ID查询品牌集合
     * @param categoryid:分类ID
     */
    List<Brand> findByCategory(Integer categoryid);
}

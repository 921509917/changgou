package com.duan.search.service;

import com.duan.search.pojo.SearchEntity;

/**
 * @ClassName SkuEsService
 * @Author DuanJinFei
 * @Date 2021/4/3 16:52
 * @Version 1.0
 */
public interface SkuEsService {
    // 导入数据
    void importData();

    // 通过各种关键词搜索 并且高亮显示
    SearchEntity searchByKeywords(SearchEntity searchEntity);
}

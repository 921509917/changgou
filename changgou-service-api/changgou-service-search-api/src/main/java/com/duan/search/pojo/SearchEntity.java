package com.duan.search.pojo;

import com.duan.entity.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SearchEntity
 * @Author DuanJinFei
 * @Date 2021/4/3 23:12
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchEntity {

    /**
     * 前端传过来的关键词信息
     */
    private String keywords;

    /**
     * 前端传过来的品牌信息
     */
    private String brand;
    /**
     * 前端传过来的分类信息
     */
    private String category;
    /**
     * 需要第几页的数据
     */
    private String searchPage;
    /**
     * 分页信息
     */
    private Page<SkuInfo> pageInfo;
    /**
     * 前端穿过来的价格区间字符串 300-500元   3000元以上
     */
    private String price;

    /**
     * 指定要排序的域
     */
    private String sortField;
    /**
     * 指定要排序的方式 ASC/DESC
     */
    private String sortRule;
    /**
     * 带参数的url，用于搜索条件的增加，删除，不带排序
     */
    private String url;
    /**
     * 带排序的url
     */
    private String sortUrl;
    /**
     * 前端传过来的规格信息
     */
    private Map<String, String> searchSpec;

    private Map<Integer, Integer> testMap;
    /**
     * 搜索结果的总记录数
     */
    private long total;
    /**
     * 查询结果的总页数
     */
    private int totalPages;
    /**
     * 分类集合
     */
    private List<String> categoryList;
    /**
     * 品牌集合
     */
    private List<String> brandList;
    /**
     *
     */
    private Map<String, Set<String>> specMap;
    /**
     * 搜索结果的集合
     */
    private List<SkuInfo> rows;
    /**
     * 将结果进行Num数的分页
     */
    private String pageNum;

    public SearchEntity(String keywords) {
        this.keywords = keywords;
    }
}


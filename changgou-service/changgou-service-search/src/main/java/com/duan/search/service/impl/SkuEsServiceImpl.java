package com.duan.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duan.entity.Page;
import com.duan.goods.feign.SkuFeign;
import com.duan.goods.pojo.Sku;
import com.duan.search.mapper.SkuEsMapper;
import com.duan.search.pojo.SkuInfo;
import com.duan.search.pojo.SearchEntity;
import com.duan.search.service.SkuEsService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.*;

/**
 * @ClassName SkuEsServiceImpl
 * @Author DuanJinFei
 * 通过点击导航栏构建查询信息 在ES中查询商品信息
 * @Date 2021/4/3 16:52
 * @Version 1.0
 */
@Service
public class SkuEsServiceImpl implements SkuEsService {
    // 默认分页为10
    private static final int SEARCH_PAGE_SIZE = 10;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ElasticsearchTemplate template;

    // 导入数据时需要使用goods服务
    @Override
    public void importData() {
        // 查询所有的商品
        List<Sku> skuList = skuFeign.findAll().getData();
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(skuList), SkuInfo.class);

        //将spec字符串转化成map，map的key会自动生成Field
        for (SkuInfo skuInfo : skuInfos) {
            Map<String, Object> map = JSONObject.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }

        // 通过ESMapper内部的方法将在Goods服务中获取到的数据添加到ES中
        skuEsMapper.saveAll(skuInfos);
    }

    @Override
    public SearchEntity searchByKeywords(SearchEntity searchEntity) {
        if (searchEntity != null && !StringUtils.isEmpty(searchEntity.getKeywords())) {
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

            //配置高亮
            HighlightBuilder.Field field = new HighlightBuilder.Field("name");
            field.preTags("<em style=\"color:red;\">");
            field.postTags("</em>");
            nativeSearchQueryBuilder.withHighlightFields(field);

            //条件筛选或者分组统计
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //分类过滤
            if (!StringUtils.isEmpty(searchEntity.getCategory())) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", searchEntity.getCategory()));
            } else {
                nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms
                        ("categories_grouping").field("categoryName").size(10000));
            }
            //品牌过滤
            if (!StringUtils.isEmpty(searchEntity.getBrand())) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", searchEntity.getBrand()));
            } else {
                nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms
                        ("brands_grouping").field("brandName").size(10000));
            }
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms
                    ("spec_grouping").field("spec.keyword").size(10000));
            //价格过滤
            if (!StringUtils.isEmpty(searchEntity.getPrice())) {
                String[] price = searchEntity.getPrice().replace("元", "")
                        .replace("以上", "").split("-");
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(Integer.parseInt(price[0])));
                if (price.length > 1) {
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lt(Integer.parseInt(price[1])));
                }
            }
            Map<String, String> searchSpec = (Map<String, String>) searchEntity.getSearchSpec();
            if (searchSpec != null && searchSpec.size() > 0) {
                for (String key : searchSpec.keySet()) {
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key + ".keyword", searchSpec.get(key)));
                }
            }
            //分页
            int pageNum = (!StringUtils.isEmpty(searchEntity.getPageNum()))
                    ? (Integer.parseInt(searchEntity.getPageNum())) : 1;
            nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, SEARCH_PAGE_SIZE));
            //排序
            String sortField = searchEntity.getSortField();
            String sortRule = searchEntity.getSortRule();
            if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
                nativeSearchQueryBuilder
                        .withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule)));
            }
            //这两行顺序不能颠倒
            nativeSearchQueryBuilder
                    .withQuery(QueryBuilders.queryStringQuery(searchEntity.getKeywords()).field("name"))
                    .withFilter(boolQueryBuilder);

            AggregatedPage<SkuInfo> skuInfos = template
                    .queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapperImpl());

            Aggregations aggregations = skuInfos.getAggregations();
            List<String> categoryList = buildGroupList(aggregations.get("categories_grouping"));
            List<String> brandList = buildGroupList(aggregations.get("brands_grouping"));
            Map<String, Set<String>> specMap = specGroup(aggregations.get("spec_grouping"));
            // 为反馈前端分页信息 添加到searchEntity中
            Page<SkuInfo> pageInfo = new Page<>(skuInfos.getTotalElements(),
                    skuInfos.getPageable().getPageNumber()+1,
                    skuInfos.getPageable().getPageSize());
            searchEntity.setPageInfo(pageInfo);

            searchEntity.setTotal(skuInfos.getTotalElements());
            searchEntity.setTotalPages(skuInfos.getTotalPages());
            searchEntity.setCategoryList(categoryList);
            searchEntity.setBrandList(brandList);
            searchEntity.setSpecMap(specMap);
            searchEntity.setRows(skuInfos.getContent());
            return searchEntity;
        }
        return null;
    }

    /**
     * 将过滤搜索出来的StringTerms转换成List集合
     * @param stringTerms
     * @return
     */
    private List<String> buildGroupList(StringTerms stringTerms) {
        List<String> list = new ArrayList<>();
        if (stringTerms != null) {
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                list.add(bucket.getKeyAsString());
            }
        }
        return list;
    }

    //规格统计
    private Map<String, Set<String>> specGroup(StringTerms specTerms) {
        Map<String, Set<String>> specMap = new HashMap<>(16);
        for (StringTerms.Bucket bucket : specTerms.getBuckets()) {
            Map<String, String> map = JSON.parseObject(bucket.getKeyAsString(), Map.class);
            for (String key : map.keySet()) {
                Set<String> specSet;
                if (!specMap.containsKey(key)) {
                    specSet = new HashSet<>();
                    specMap.put(key, specSet);
                } else {
                    specSet = specMap.get(key);
                }
                specSet.add(map.get(key));
            }
        }
        return specMap;
    }
}

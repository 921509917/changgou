package com.duan.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.duan.search.pojo.SkuInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SearchResultMapperImpl
 * @Author DuanJinFei
 * @Date 2021/4/4 21:47
 * @Version 1.0
 */
public class SearchResultMapperImpl implements SearchResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        List<T> list = new ArrayList<>();
        //遍历所有数据
        for (SearchHit hit : response.getHits()) {
            //非高亮数据
            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
            //高亮数据
            HighlightField highlightField = hit.getHighlightFields().get("name");
            //将非高亮数据替换成高亮数据
            if (highlightField != null && highlightField.getFragments() != null) {
                Text[] fragments = highlightField.getFragments();
                StringBuilder builder = new StringBuilder();
                for (Text fragment : fragments) {
                    builder.append(fragment.toString());
                }
                skuInfo.setName(builder.toString());
                list.add((T) skuInfo);
            }
        }
        return new AggregatedPageImpl<T>(list, pageable,
                response.getHits().getTotalHits(),response.getAggregations());
    }
}

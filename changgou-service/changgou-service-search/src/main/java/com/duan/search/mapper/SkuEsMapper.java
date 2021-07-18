package com.duan.search.mapper;

import com.duan.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @ClassName SkuEsMapper
 * @Author DuanJinFei
 * @Date 2021/4/3 16:53
 * @Version 1.0
 */
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo, Long> {
}

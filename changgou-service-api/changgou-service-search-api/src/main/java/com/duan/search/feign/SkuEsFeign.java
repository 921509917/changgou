package com.duan.search.feign;

import com.duan.entity.Result;
import com.duan.goods.pojo.Sku;
import com.duan.search.pojo.SearchEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @ClassName SkuFeign
 * @Author DuanJinFei
 * @Date 2021/4/3 16:41
 * @Version 1.0
 */
@FeignClient("search")
@RequestMapping("/search")
public interface SkuEsFeign {

    /**
     * 搜索
     * @param searchEntity
     * @return
     */
    @GetMapping
    Result<SearchEntity> searchByKeywords(@RequestParam(required = false) SearchEntity searchEntity);

    /**
     * 删除Sku集合
     * @param list
     * @return
     */
    @DeleteMapping
    Result deleteList(@RequestBody List<Sku> list);

    /**
     * 修改Sku集合
     * @param list
     * @return
     */
    @PostMapping
    Result updateList(@RequestBody List<Sku> list);
}

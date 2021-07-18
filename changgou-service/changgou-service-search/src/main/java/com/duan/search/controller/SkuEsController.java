package com.duan.search.controller;

import com.duan.entity.Result;
import com.duan.entity.StatusCode;
import com.duan.search.service.SkuEsService;
import com.duan.search.pojo.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SkuController
 * @Author DuanJinFei
 * @Date 2021/4/3 16:45
 * @Version 1.0
 */
@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuEsController {

    @Autowired
    private SkuEsService skuEsService;

    @GetMapping("/import")
    public Result importData(){
        skuEsService.importData();
        return new Result(true, StatusCode.OK,"数据导入成功");
    }

    @GetMapping
    public Result<SearchEntity> searchByKeywords(@RequestBody(required = false) SearchEntity searchEntity) {
        System.out.println("searchEntity:"+searchEntity);
        SearchEntity result = skuEsService.searchByKeywords(searchEntity);
        return new Result<>(true,StatusCode.OK,"根据关键词搜索成功",result);
    }
}

package com.duan.searchWeb.controller;

import com.duan.search.feign.SkuEsFeign;
import com.duan.search.pojo.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName SkuSearchWebController
 * @Author DuanJinFei
 * @Date 2021/4/5 22:20
 * @Version 1.0
 */
@Controller
@RequestMapping("search_web")
public class SkuSearchWebController {

    @Autowired
    private SkuEsFeign skuEsFeign;

    @GetMapping("/list")
    public String searchByKeywords(SearchEntity searchEntity, Model model, HttpServletRequest request){
        /*if (searchEntity == null || StringUtils.isEmpty(searchEntity.getKeywords())){
            // 若前端输入的数据为空或者无效时  返回默认搜索数据 使其不会报错 正常运行
            searchEntity = new SearchEntity("小米");
        }
        if (searchEntity.getSearchSpec() == null) {
            searchEntity.setSearchSpec(new HashMap<>(8));
        }
        SearchEntity result = skuEsFeign.searchByKeywords(searchEntity).getData();
        model.addAttribute("result",result);*/
        SearchEntity result = skuEsFeign.searchByKeywords(searchEntity).getData();
        if (result == null){
            result = new SearchEntity();
        }
        result.setUrl(getUrl(request)[0]);
        result.setSortUrl(getUrl(request)[1]);
        model.addAttribute("result", result);
        return "search";
    }
    private String[] getUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder("/search/list");
        StringBuilder sortUrl = new StringBuilder("/search/list");
        Map<String, String[]> parameters = request.getParameterMap();
        if (parameters!=null&&parameters.size()>0){
            url.append("?");
            sortUrl.append("?");
            if (!parameters.containsKey("keywords")) {
                url.append("keywords=小米&");
                sortUrl.append("keywords=小米&");
            }
            for (String key:parameters.keySet()){
                if ("searchPage".equalsIgnoreCase(key)){
                    continue;
                }
                url.append(key).append("=").append(parameters.get(key)[0]).append("&");
                if (!("sortField".equalsIgnoreCase(key)||"sortRule".equalsIgnoreCase(key))){
                    sortUrl.append(key).append("=").append(parameters.get(key)[0]).append("&");
                }
            }
            url.deleteCharAt(url.length()-1);
            sortUrl.deleteCharAt(sortUrl.length()-1);
        }
        return new String[]{url.toString().replace(" ","+"),
                sortUrl.toString().replace(" ","+")};
    }
}

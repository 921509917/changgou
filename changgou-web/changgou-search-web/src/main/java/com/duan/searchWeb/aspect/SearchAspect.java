package com.duan.searchWeb.aspect;

import com.duan.search.pojo.SearchEntity;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SearchAspect
 * @Author DuanJinFei
 * @Date 2021/4/6 9:11
 * @Version 1.0
 * 利用AOP 实现提前对参数进行处理  从而不会影响业务层逻辑
 * 业务层只需负责相关业务的处理即可
 */
@Aspect
@Component
public class SearchAspect {


    // 切入点  当前台发送请求 开始执行该方法之前
    @Pointcut("execution(public * com.duan.searchWeb.controller.SkuSearchWebController.searchByKeywords(..)) " +
            "&& args(searchEntity,model,request))")
    public void searchAspect(SearchEntity searchEntity, Model model, HttpServletRequest request){
    }

    @Before(value = "searchAspect(searchEntity,model,request)",argNames = "searchEntity,model,request")
    public void doBeforeSearch(SearchEntity searchEntity,Model model, HttpServletRequest request)
            throws Throwable {
        if (StringUtils.isEmpty(searchEntity.getKeywords())) {
            searchEntity.setKeywords("小米");
        }
        Map<String,String> specs = searchEntity.getSearchSpec();
        if (specs == null) {
            searchEntity.setSearchSpec(new HashMap<>(8));
        } else {
            for (String key:specs.keySet()){
                String value = specs.get(key).replace(" ","+");
                specs.put(key,value);
            }
        }
    }
}

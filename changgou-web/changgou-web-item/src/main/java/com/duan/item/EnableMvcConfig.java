package com.duan.item;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName EnableMvcConfig
 * @Author DuanJinFei
 * @Date 2021/4/13 22:43
 * @Version 1.0
 */
@ControllerAdvice
@Configuration
public class EnableMvcConfig implements WebMvcConfigurer {

    /***
     * 静态资源放行
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/items/**").addResourceLocations("classpath:/templates/items/");
    }
}
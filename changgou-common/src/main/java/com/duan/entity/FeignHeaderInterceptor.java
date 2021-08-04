package com.duan.entity;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @ClassName FeignHeaderInterceptor
 * @Author DuanJinFei
 * @Date 2021/4/12 8:38
 * @Version 1.0
 */
public class FeignHeaderInterceptor implements RequestInterceptor {
    /**
     * Feign调用前调用
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            //所有请求头的名字集合
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String headerValue = request.getHeader(headerName);
                    template.header(headerName,headerValue);
                }
            }
        }
    }
}

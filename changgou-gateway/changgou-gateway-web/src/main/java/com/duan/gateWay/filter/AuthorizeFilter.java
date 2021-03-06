package com.duan.gateWay.filter;

import com.duan.entity.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName AuthorizeFilter
 * @Author DuanJinFei
 * @Date 2021/4/6 11:39
 * @Version 1.0
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 获取本地Token值 从Cookie中
    private static final String AUTHORIZE_TOKEN = "Authorization";

    /**
     *  客户端首次请求经过网关时进行请求过滤
     *      1.通过请求头中获取登录之后的token值
     *      2.若请求头中没有，就从参数中获取
     *      3.判断请求路径，如果不需要过滤，则放行
     *      4.若没成功放行，则进行响应码的设置
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        boolean hasTokenInHeader = true;
        // 如果是不需要token的请求就通过
        if(needlessToken(request.getURI().toString())){
            return chain.filter(exchange);
        }
        String token;
        // 从请求头中获取token
        token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        // 如果没有Token 就从参数中获取
        if(StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        // 参数中没有Token就从Cookie中获取
        if (StringUtils.isEmpty(token)){
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            // 若能从Cookie中拿到值
            if (cookie != null){
                token = cookie.getValue();
            }
        }
        if (StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
        }
        try {
            JwtUtil.parseJWT(token);
            request.mutate().header(AUTHORIZE_TOKEN,"Bearer"+token);
        } catch (Exception e) {
            //报异常说明Token是错误的，拦截
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //通过过滤器
        return chain.filter(exchange);
    }

    /**
     *  判断指定的uri是否不需要token就可以访问，true表示不需要
     * @param uri
     * @return
     */
    public boolean needlessToken(String uri) {
        String[] uris = new String[]{
                "/api/user/add",
                "/api/user/login"
        };
        for (String s : uris) {
            if (s.equals(uri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

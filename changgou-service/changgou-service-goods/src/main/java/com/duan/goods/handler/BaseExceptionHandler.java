package com.duan.goods.handler;

/**
 * @ClassName BaseExceptionHandler
 * @Author DuanJinFei
 * @Date 2021/3/31 16:49
 * @Version 1.0
 */

import com.duan.entity.Result;
import com.duan.entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }

}

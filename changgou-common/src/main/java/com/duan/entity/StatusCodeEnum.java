package com.duan.entity;

/**
 * @ClassName StatusCodeEnum
 * @Author DuanJinFei
 * @Date 2021/8/28 23:27
 * @Version 1.0
 */
public enum StatusCodeEnum {

    /**
     * 访问成功
     */
    OK("20001","发送成功",false);

    private String code;
    private String message;
    private boolean result;

    StatusCodeEnum(String code, String message, boolean result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}

package com.whz.blog.entity;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/10 13:17
 */
public class Result {

    //404 400 500
    private Integer code;

    private String message;

    private Object data;

    public Result(){
        this.code = 404;
        this.message = "发生未知的错误";
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

package com.whz.blog.entity;

import javafx.beans.binding.ObjectExpression;
import org.springframework.http.HttpStatus;

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
        this.code = 500;
        this.message = "catch unexpected errors";
    }
    public Result(int code, String message) {
        this(code,message,null);
    }
    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public Result(HttpStatus httpStatus) {
        this(httpStatus,null);
    }
    public Result(Object data,HttpStatus httpStatus) {
        this.message = httpStatus.getReasonPhrase();
        this.code = httpStatus.value();
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

package com.whz.blog.exception;

/*
    通用异常
 */
public enum CommonResponseEnum  {

    SERVER_ERROR(7002, "Server error."),
    SERVLET_ERROR(7005,"Servlet error");

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    CommonResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
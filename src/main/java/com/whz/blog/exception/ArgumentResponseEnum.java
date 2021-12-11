package com.whz.blog.exception;

/*
    参数异常
    参数绑定，参数验证等
 */
public enum ArgumentResponseEnum implements BusinessExceptionAssert {

    VALID_ERROR(7002, "Valid error.");

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    ArgumentResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
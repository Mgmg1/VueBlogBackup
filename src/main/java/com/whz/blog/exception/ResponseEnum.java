package com.whz.blog.exception;

/*
    枚举的业务异常
 */
public enum ResponseEnum implements BusinessExceptionAssert {

    /**
     * Bad licence type
     */
    BAD_LICENCE_TYPE(7001, "Bad licence type."),
    /**
     * Licence not found
     */
    LICENCE_NOT_FOUND(7002, "Licence not found."),

    /**
     * error empty result
     */
    ERROR_EMPTY_RESULT( 7003,"error empty result" );

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

    /*
        枚举类的构造器默认私有
     */
    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
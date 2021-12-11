package com.whz.blog.exception;

public enum ServletResponseEnum {

    /**
     * Licence not found
     */
    LICENCE_NOT_FOUND(7010, "Licence not found.")
    ;

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

    ServletResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
package com.whz.blog.exception;

/*
    以枚举异常信息为底的异常

    responseEnum 响应消息枚举对象
    args 消息格式化变量，用于填充占位符
 */
public class BaseException extends RuntimeException {

    private IResponseEnum responseEnum;
    private Object[] args;

    public BaseException(IResponseEnum responseEnum, Object[] args, String message) {
        super(message);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    /*
        cause 是指被 自定义异常类 包装的  异常类
     */
    public BaseException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BaseException(CommonResponseEnum serverError) {
        this( null,null,serverError.getMessage() );
    }

    public IResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public Object[] getArgs() {
        return args;
    }


}

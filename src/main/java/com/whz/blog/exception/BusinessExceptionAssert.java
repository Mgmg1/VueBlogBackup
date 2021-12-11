package com.whz.blog.exception;
import java.text.MessageFormat;

/*
    针对 BusinessException 的 断言器，用于生成 枚举 的业务异常

    通过IResponseEnum得到获取枚举异常信息的操作
    通过Assert得到生成异常和断言操作
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    /*
       取出枚举的错误反馈消息，并进行格式化
     */
    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, args, msg);
    }

    /*
        基于，Throwable t ，取出枚举的错误反馈消息，并进行格式化
    */
    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, args, msg, t);
    }

}
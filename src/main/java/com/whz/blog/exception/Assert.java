package com.whz.blog.exception;

/*
    断言接口
 */
public interface Assert {
    /**
     * 创建异常，格式化消息
     * @param args
     * @return
     */
    BaseException newException(Object... args);

    /**
     * 基于t创建异常，格式化消息
     * @param t
     * @param args
     * @return
     */
    BaseException newException(Throwable t, Object... args);

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj 待判断对象
     */
    default void assertNotNull(Object obj)  {
        if (obj == null) {
            throw newException();
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj 待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertNotNull(Object obj, Object... args)  {
        if (obj == null) {
            throw newException( args ) ;
        }
    }
}
package com.whz.blog.interceptor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface AccessLimit {
    int limitCount() default 5;
    int second() default 5;
    boolean needLogin() default false;
}

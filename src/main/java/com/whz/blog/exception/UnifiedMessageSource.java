package com.whz.blog.exception;

import org.springframework.stereotype.Component;

/*
    国际化消息处理
    先占位，暂不对消息进行处理
 */
@Component
public class UnifiedMessageSource {
    public String getMessage(String code, Object[] args) {
        return code;
    }
}

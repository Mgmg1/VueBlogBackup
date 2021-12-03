package com.whz.blog.config;

import com.whz.blog.interceptor.AccessOriginInterceptor;
import com.whz.blog.interceptor.AuthInterceptor;
import com.whz.blog.interceptor.SignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/10 15:45
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor getAccessOriginInterceptor(){
        return new AccessOriginInterceptor();
    }

    @Bean
    public HandlerInterceptor getAuthInterceptor(){
        return new AuthInterceptor();
    }

    @Bean
    public HandlerInterceptor getSignInterceptor() {return new SignInterceptor(); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAccessOriginInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(getSignInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(getAuthInterceptor())
                .addPathPatterns("/upload","/setheadimgurl","/setannon");

//                .excludePathPatterns("/login","/mail","/register","/autologin","/logout");
        // 拦截所有、排除 "/**"

    }
}

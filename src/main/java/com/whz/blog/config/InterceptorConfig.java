package com.whz.blog.config;

import com.whz.blog.interceptor.AccessLimitInterceptor;
import com.whz.blog.interceptor.AuthInterceptor;
import com.whz.blog.interceptor.SignInterceptor;
import com.whz.blog.interceptor.StaticResourceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerInterceptor;
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
    public HandlerInterceptor getStaticResourceInterceptor( ) { return new StaticResourceInterceptor();}

    @Bean
    public HandlerInterceptor getAccessLimitInterceptor(){ return new AccessLimitInterceptor();}

    @Bean
    public HandlerInterceptor getSignInterceptor() {return new SignInterceptor(); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( getStaticResourceInterceptor() )
                .addPathPatterns("/**");
        registry.addInterceptor( getAccessLimitInterceptor() )
                .addPathPatterns("/**");
        registry.addInterceptor(getSignInterceptor())
                .excludePathPatterns("/static/**","/favicon.ico")
                .addPathPatterns("/**").excludePathPatterns("/download");
//                .excludePathPatterns("/login","/mail","/register","/autologin","/logout");
        // 拦截所有、排除 "/**"
    }
}

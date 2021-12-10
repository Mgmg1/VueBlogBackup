package com.whz.blog.interceptor;

import com.auth0.jwt.JWTVerifier;
import com.whz.blog.controller.UserController;
import com.whz.blog.entity.User;
import com.whz.blog.mapper.UserMapper;
import com.whz.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    /*
        认证拦截器。
        只有登录了才能通过。
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler){
        return request.getSession().getAttribute(UserController.USER ) != null ;
    }
}

package com.whz.blog.interceptor;

import com.whz.blog.entity.User;
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

    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader("Authorization");
        String fingerPrintId = request.getHeader("fid");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (token == null || fingerPrintId == null){
            //token为空，进行业务处理
            return false;
        }

        if (!jwtUtil.verify(token,fingerPrintId)) {
            //token检验失败，如果在创建token时加上过期时间，时间过期了这里就是校验失败
            return false;
        }

        Integer userId = jwtUtil.getUserId(token);
        User user = (User) request.getSession().getAttribute("user");
        return user != null && userId.equals(user.getUserId());

    }
}

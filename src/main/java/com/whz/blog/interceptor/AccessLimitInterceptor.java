package com.whz.blog.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whz.blog.controller.UserController;
import com.whz.blog.entity.Result;
import com.whz.blog.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import sun.reflect.annotation.AnnotationType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    private static final String LIMIT_COUNT = "limitCount";
    private static final String SECOND = "second";
    private static final String NEED_LOGIN = "needLogin";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if( handler instanceof HandlerMethod ) {
            HandlerMethod hm = ( HandlerMethod ) handler;
            AccessLimit accessLimitOnClass = hm.getBeanType().getAnnotation(AccessLimit.class);
            AccessLimit accessLimitOnMethod = hm.getMethodAnnotation(AccessLimit.class);
            if( accessLimitOnClass != null || accessLimitOnMethod != null ) {
                Map<String, Object> defaultValMap = AnnotationType.getInstance(AccessLimit.class).memberDefaults();
                int limitCount = (int) defaultValMap.get(LIMIT_COUNT);
                int second = (int) defaultValMap.get(SECOND);
                boolean needLogin = (boolean) defaultValMap.get(NEED_LOGIN);

                if( accessLimitOnClass != null ) {
                    limitCount = accessLimitOnClass.limitCount();
                    second = accessLimitOnClass.second();
                    needLogin = accessLimitOnClass.needLogin();
                }
                if( accessLimitOnMethod != null ) {
                    limitCount = accessLimitOnMethod.limitCount();;
                    second = accessLimitOnMethod.second();
                    needLogin = accessLimitOnMethod.needLogin();;
                }
                if( needLogin ) {
                    Object user = request.getSession(true).getAttribute(UserController.USER );
                    if( user== null ) {
                        render(response,"需要登录");
                        return false;
                    }
                }
                String key = request.getRequestURI(); // uri 不包含协议和主机地址，url包含
                //接口uri 拼接sessionid， 用sessionId来 标识某个用户使用某个接口
                key = key +"#" + request.getSession(true).getId(); // uri#sessionId
                String count = redisUtils.get(key);
                if( count == null ) {
                    redisUtils.setEx(key,Integer.toString(1),second, TimeUnit.SECONDS);
                }else if( Integer.parseInt(count) < limitCount ){
                    redisUtils.incrBy(key,1);
                }else {
                    render(response,"请求过于频繁");
                    return false;
                }
            }
        }
        return true;
    }
    /*
        对原生response对象进行处理
     */
    private void render(HttpServletResponse response,String msg)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();

        Result result = new Result();
        result.setMessage((String) msg);
        result.setCode(400);

        String str  = new ObjectMapper().writeValueAsString(result);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}

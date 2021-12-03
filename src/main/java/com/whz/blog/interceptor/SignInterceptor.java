package com.whz.blog.interceptor;

import com.whz.blog.entity.Result;
import com.whz.blog.util.Md5Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class SignInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod) ){
            return  true;
        }

        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> param = new HashMap<>();
        for (String s : map.keySet()) {
            //不包含sign
            if("sign".equalsIgnoreCase(s) ){
                continue;
            }
            param.put(s, arrayToString(map.get(s)));
        }

        String signatures = Md5Utils.signatures(param);  //sign_server
        String sign = request.getParameter("sign");  //sign_client
        if(sign == null || !sign.equalsIgnoreCase(signatures)){
//            response.setContentType("text/json;charset=UTF-8");
//            response.getWriter().write(JSON.toJSONString(new JsonResult(501,"签名校验失败","不好意思咯")));
            return false;
        }
        return true;
    }


    private String arrayToString(String [] array){

        StringBuilder sb = new StringBuilder(10);
        for (String s : array) {
            sb.append(s);
        }
        return sb.toString();
    }
}
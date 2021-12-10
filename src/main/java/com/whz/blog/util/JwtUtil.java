package com.whz.blog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.whz.blog.controller.UserController;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    public static final String SEPERATION = "=";
    //默认有效时间，未24小时
    private static final int VALID_TIME = 24 * 3600 * 1000;

    //创建token
    //token是有三个部分分别用"."隔开
    //第一部分是声明用什么算法进行签名
    //第二部分是加密的数据，如下就是用userId
    //第三部分是密钥，如下就是"abc"，解密第二部分数据时需要用到

    //使用key加密，key 内容为 用户密码摘要拼接远程IP地址
    /*
        设置jwt的有效时间
        times : 毫秒作为单位
     */
    public static String create(Integer userId,String key,int times,Boolean isAutoLogin){
        return JWT.create().withClaim("userId", userId)
                .withClaim(UserController.AUTO_LOGIN,isAutoLogin)
                .withExpiresAt( new Date(System.currentTimeMillis() + times) )
                .sign(Algorithm.HMAC256( key ));
    }

    public static String create(Integer userId,String key,Boolean isAutoLogin){
        return JWT.create().withClaim("userId", userId)
                .withClaim(UserController.AUTO_LOGIN,isAutoLogin)
                .sign(Algorithm.HMAC256( key ));
    }

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 返回是否校验通过
     */
    public static boolean verify(String token,String key) {
        try {
            //abc——>创建token时的第三部分
            JWT.require(Algorithm.HMAC256( key )).build().verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 根据Token获取userId
     */
    public static Integer getUserId(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("userId").asInt();
    }

    public static Boolean getIsAutoLogin(String token) {
        return JWT.decode(token).getClaim(UserController.AUTO_LOGIN).asBoolean();
    }

    public static String appendKey( String[] signs ) {
        StringBuilder sb = new StringBuilder();
        if( signs != null ) {
            for (int i = 0; i < signs.length; i++) {
                sb.append( signs[i] );
                if( i < signs.length - 1 ) {
                    sb.append(JwtUtil.SEPERATION);
                }
            }
        }
        return sb.toString();
    }
}
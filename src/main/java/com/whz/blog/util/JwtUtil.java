package com.whz.blog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private String salt = "WangHZ";

    //创建token
    //token是有三个部分分别用"."隔开
    //第一部分是声明用什么算法进行签名
    //第二部分是加密的数据，如下就是用userId
    //第三部分是密钥，如下就是"abc"，解密第二部分数据时需要用到
    public String create(Integer userId,String key){
        return JWT.create().withClaim("userId", userId)
                .sign(Algorithm.HMAC256(this.salt + key));
    }


    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 返回是否校验通过
     */
    public boolean verify(String token,String key) {
        try {
            //abc——>创建token时的第三部分
            JWT.require(Algorithm.HMAC256(this.salt + key)).build().verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 根据Token获取userId
     */
    public Integer getUserId(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("userId").asInt();
    }

}
package com.vector.server.config.shiro;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:
 * @Title: JwtUtil
 * @Package com.vector.server.config.shiro
 * @Author 芝士汉堡
 * @Date 2022/12/4 11:29
 */
@Component
@Slf4j
public class JwtUtil {
    @Value("${applet.jwt.secret}")
    private String secret;
    @Value("${applet.jwt.expire}")
    private int expire;

    public String createToken(int userId) {
        Date data = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 5);  //令牌过期时间
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create();
        String token = builder.withClaim("userId", userId)
                .withExpiresAt(data)
                .sign(algorithm);// 生成签名
        return token;
    }

    /**
     * 获取用户id
     * @param token
     * @return
     */
    public int getUserId(String token) {
        try {
            return JWT.decode(token).getClaim("userId").asInt(); //获取用户id
        } catch (Exception e) {
            log.error("token解析失败");
            return 0;
        }
    }

    /**
     * 校验token是否正确
     * @param token
     */
    public void verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm).build().verify(token);
        } catch (Exception e) {
            log.error("token解析失败");
        }
    }

}

package com.zgnba.clos.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * token工具类
 */
@Component
@Slf4j
public class JwtUtil {

    @Value("${clos.jwt.secret}")
    private String secret;

    @Value("${clos.jwt.expire}")
    private Integer expire;

    /**
     * 创建token方法
     * @param userId
     * @return token
     */
    public String createToken(String userId) {
        Date date = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, expire);
        // 创建加密算法
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 生成token
        JWTCreator.Builder builder = JWT.create();
        return builder.withClaim("userId", userId).withExpiresAt(date).sign(algorithm);
    }

    /**
     * 根据token获取userId方法
     * @param token
     * @return userId
     */
    public String getUserId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userId").asString();
    }

    /**
     * 验证token方法
     * @param token
     */
    public void verifierToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
}

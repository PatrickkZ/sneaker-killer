package com.patrick.sneakerkillerservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.patrick.sneakerkillerservice.config.PropertiesConfig;

import java.util.Date;

public class JWTUtil {
    /**
     * 生成签名,15min后过期
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username, PropertiesConfig propertiesConfig) {
        Date date = new Date(System.currentTimeMillis() + propertiesConfig.getExpireTime());
        Algorithm algorithm = Algorithm.HMAC256(propertiesConfig.getSecret());
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, PropertiesConfig propertiesConfig){
        try {
            Algorithm algorithm = Algorithm.HMAC256(propertiesConfig.getSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的信息
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}

/**
 * 验证结果常量
 */
class JwtVerifyConst{
    public static String SUCCESS = "token验证成功";
    public static String EXPIRED = "token已过期";
    public static String SIGNATURE_VERIFICATION = "token签名失败";
    public static String DECODE_ERROR = "token解析失败，请重新登录获取token";
    public static String NOT_LOGIN = "未登录";
}

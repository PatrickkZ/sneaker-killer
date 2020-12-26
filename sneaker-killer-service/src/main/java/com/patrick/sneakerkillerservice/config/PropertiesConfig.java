package com.patrick.sneakerkillerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 属性配置类,从配置文件中获取属性的值
 */
@Component
public class PropertiesConfig {
    /**
     * 雪花算法workerId
     */
    @Value("${snowflake.workerId}")
    private Long workerId;
    /**
     * 雪花算法datacenterId
     */
    @Value("${snowflake.datacenterId}")
    private Long datacenterId;

    /**
     * jwt密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * jwt中token的过期时间
     */
    @Value("${jwt.expire-time}")
    private Long expireTime;

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(Long datacenterId) {
        this.datacenterId = datacenterId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}

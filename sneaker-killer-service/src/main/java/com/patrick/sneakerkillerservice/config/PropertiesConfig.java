package com.patrick.sneakerkillerservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 属性配置类,从配置文件中获取属性的值
 */
@Component
@ConfigurationProperties(prefix = "snowflake")
public class PropertiesConfig {
    /**
     * 雪花算法workerId
     */
    private long workerId;
    /**
     * 雪花算法datacenterId
     */
    private long datacenterId;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }
}

package com.patrick.sneakerkillerservice.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置
 */
@Configuration
public class RedissonConfig {

    /**
     * redis服务器的地址和端口号, 写在配置文件中
     */
    @Value("${redisson.address}")
    private String address;

    /**
     * 服务器redis的连接密码,写在配置文件中
     */
    @Value("${redisson.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password);
        return Redisson.create(config);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.patrick;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.patrick.sneakerkillermodel.mapper")
public class SneakerKillerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SneakerKillerWebApplication.class, args);
    }

}

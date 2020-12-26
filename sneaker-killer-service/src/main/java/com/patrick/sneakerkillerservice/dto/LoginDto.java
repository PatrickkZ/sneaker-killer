package com.patrick.sneakerkillerservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 登录时校验格式,但不提示是什么错误,统一为用户名或密码错误
 */
public class LoginDto {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]\\w{5,17}$", message = "用户名或密码错误")
    private String username;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{6,16}$", message = "用户名或密码错误")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

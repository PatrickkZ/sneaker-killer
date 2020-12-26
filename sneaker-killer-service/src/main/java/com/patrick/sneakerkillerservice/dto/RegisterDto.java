package com.patrick.sneakerkillerservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDto {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]\\w{5,17}$", message = "用户名不合法")
    private String username;
    @NotNull
    @Pattern(regexp = "^[0-9a-zA-Z]+\\w*@([0-9a-z]+\\.)+[0-9a-z]+$",message = "邮箱的格式不合法")
    private String email;
    /**
     * 暂时用一个简单的密码格式
     * 可包含字母数字,不包含特殊字符
     */
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{6,16}$", message = "密码格式不合法")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

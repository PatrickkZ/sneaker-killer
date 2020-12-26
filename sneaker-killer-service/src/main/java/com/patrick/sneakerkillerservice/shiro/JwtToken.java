package com.patrick.sneakerkillerservice.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自己实现的一个AuthenticationToken
 * 验证用户名和密码的过程都可以用jwt的token来代替
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

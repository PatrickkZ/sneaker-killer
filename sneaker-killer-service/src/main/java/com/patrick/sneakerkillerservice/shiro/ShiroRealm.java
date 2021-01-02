package com.patrick.sneakerkillerservice.shiro;

import com.patrick.sneakerkillermodel.entity.User;
import com.patrick.sneakerkillerservice.config.PropertiesConfig;
import com.patrick.sneakerkillerservice.service.UserService;
import com.patrick.sneakerkillerservice.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroRealm extends AuthorizingRealm {

    PropertiesConfig propertiesConfig;
    UserService userService;

    @Autowired
    public ShiroRealm(PropertiesConfig propertiesConfig, UserService userService){
        this.propertiesConfig = propertiesConfig;
        this.userService = userService;
    }

    /**
     * 这里必须重写该方法
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 检测用户权限的时候会调用该方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 身份的验证，这里用jwt的token
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String username = JWTUtil.getUsername(token);
        if(username == null){
            throw new AuthenticationException("token invalid");
        }
        User user = userService.getByName(username);
        if(user == null){
            throw new AuthenticationException("user not exist");
        }
        if (!JWTUtil.verify(token, propertiesConfig)) {
            throw new AuthenticationException("token verify error");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}

package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillermodel.entity.User;
import com.patrick.sneakerkillermodel.mapper.UserMapper;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    public final static String USERNAME_EXIST = "用户名已存在";
    public final static String EMAIL_EXIST = "该邮箱已被注册";
    public final static String SUCCESS = "注册成功";
    public final static String FAIL = "系统异常";
    UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public List<User> listAll(){
        return userMapper.listAll();
    }

    public User getByName(String username){
        return userMapper.getByUsername(username);
    }

    public String handleRegister(String username, String email, String password){
        User userByName = userMapper.getByUsername(username);
        if(userByName != null){
            return USERNAME_EXIST;
        }
        User userByEmail = userMapper.getByEmail(email);
        if(userByEmail != null){
            return EMAIL_EXIST;
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setSalt(salt);
        user.setRegisterDate(new Date());
        if(userMapper.add(user) > 0){
            return SUCCESS;
        } else {
            return FAIL;
        }
    }

    public boolean handleLogin(String username, String password){
        User user = userMapper.getByUsername(username);
        if(user == null){
            return false;
        }
        String salt = user.getSalt();
        int times = 2;
        String encodePassword = new SimpleHash("md5", password, salt, times).toString();
        return encodePassword.equals(user.getPassword());
    }
}

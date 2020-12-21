package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillermodel.entity.User;
import com.patrick.sneakerkillermodel.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public List<User> listAll(){
        return userMapper.listAll();
    }
}

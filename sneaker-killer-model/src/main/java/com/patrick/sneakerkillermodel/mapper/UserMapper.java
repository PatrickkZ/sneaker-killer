package com.patrick.sneakerkillermodel.mapper;

import com.patrick.sneakerkillermodel.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> listAll();
    User getByUsername(String username);
    User getByEmail(String email);
    int add(User user);
}

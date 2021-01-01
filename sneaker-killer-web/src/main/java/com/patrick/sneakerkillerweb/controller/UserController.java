package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillerservice.service.OrderService;
import com.patrick.sneakerkillerservice.service.UserService;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    UserService userService;
    OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService){
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/users")
    public Result getAllUser(){
        return ResultFactory.buildSuccessResult(userService.listAll());
    }

    @GetMapping(value = "/order")
    public Result getOrder(){
        // TODO 测试，用户先用2
        return ResultFactory.buildSuccessResult(orderService.getByUserId(2));
    }
}

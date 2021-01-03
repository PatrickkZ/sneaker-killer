package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillermodel.entity.Order;
import com.patrick.sneakerkillerservice.service.OrderService;
import com.patrick.sneakerkillerservice.service.UserService;
import com.patrick.sneakerkillerservice.util.JWTUtil;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @RequiresAuthentication
    public Result getOrder(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Integer userId = JWTUtil.getUserId(token);
        return ResultFactory.buildSuccessResult(orderService.getByUserId(userId));
    }

    @PostMapping(value = "/pay")
    @RequiresAuthentication
    public Result payOrder(@RequestBody Order order){
        if(orderService.payOrder(order.getId())){
            return ResultFactory.buildSuccessResult(null);
        }else {
            return ResultFactory.buildFailResult("订单已支付或已过期");
        }

    }
}

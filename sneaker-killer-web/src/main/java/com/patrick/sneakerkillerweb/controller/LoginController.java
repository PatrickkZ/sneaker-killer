package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillerservice.config.PropertiesConfig;
import com.patrick.sneakerkillerservice.dto.LoginDto;
import com.patrick.sneakerkillerservice.dto.RegisterDto;
import com.patrick.sneakerkillerservice.service.UserService;
import com.patrick.sneakerkillerservice.util.JWTUtil;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sk")
public class LoginController {

    UserService userService;
    PropertiesConfig propertiesConfig;

    @Autowired
    public LoginController(UserService userService, PropertiesConfig propertiesConfig){
        this.userService = userService;
        this.propertiesConfig = propertiesConfig;
    }


    @PostMapping(value = "/register")
    public Result register(@RequestBody @Validated RegisterDto registerDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultFactory.buildFailResult(bindingResult.getFieldError().getDefaultMessage());
        }
        String result = userService.handleRegister(registerDto.getUsername(), registerDto.getEmail(), registerDto.getPassword());
        if (UserService.SUCCESS.equals(result)){
            return ResultFactory.buildSuccessResult(result);
        } else {
            return ResultFactory.buildFailResult(result);
        }
    }

    @PostMapping(value = "/login")
    public Result login(@RequestBody @Validated LoginDto loginDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultFactory.buildFailResult(bindingResult.getFieldError().getDefaultMessage());
        }
        // 验证成功,生成token返回
        if(userService.handleLogin(loginDto.getUsername(), loginDto.getPassword())){
            return ResultFactory.buildSuccessResult(JWTUtil.sign(loginDto.getUsername(), propertiesConfig));
        }
        return ResultFactory.buildFailResult("用户名或密码错误");
    }
}

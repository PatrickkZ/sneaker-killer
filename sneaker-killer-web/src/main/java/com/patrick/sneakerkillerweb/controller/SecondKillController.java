package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillerservice.dto.SecondKillDto;
import com.patrick.sneakerkillerservice.service.SendMessageService;
import com.patrick.sneakerkillerservice.util.JWTUtil;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api")
public class SecondKillController {

    SendMessageService sendMessageService;

    public SecondKillController(SendMessageService sendMessageService){
        this.sendMessageService = sendMessageService;
    }

    @PostMapping(value = "/item/order")
    @RequiresAuthentication
    public Result executeKill(@RequestBody @Validated SecondKillDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return ResultFactory.buildFailResult("invalid parameter");
        }
        String token = request.getHeader("Authorization");
        Integer userId = JWTUtil.getUserId(token);
        dto.setUserId(userId);
        sendMessageService.sendMessageToMQ(dto);
        return ResultFactory.buildSuccessResult("订单已提交");
    }
}

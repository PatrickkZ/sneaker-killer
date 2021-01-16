package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillerservice.dto.SecondKillDto;
import com.patrick.sneakerkillerservice.service.SendMessageService;
import com.patrick.sneakerkillerservice.util.JWTUtil;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api")
public class SecondKillController {

    SendMessageService sendMessageService;
    StringRedisTemplate redisTemplate;

    public SecondKillController(SendMessageService sendMessageService, StringRedisTemplate redisTemplate){
        this.sendMessageService = sendMessageService;
        this.redisTemplate = redisTemplate;
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
        String redisKey = new StringBuffer().append(userId).append(dto.getItemId()).append("-redis").toString();
        // 先看缓存有没有信息，有说明用户已经提交过请求了，直接返回
        if(redisTemplate.opsForValue().get(redisKey) != null){
            return ResultFactory.buildFailResult("请勿重复提交");
        }
        // 没有就在缓存中创建一个kv，过期时间为30分钟
        redisTemplate.opsForValue().set(redisKey, String.valueOf(userId), 30, TimeUnit.MINUTES);
        sendMessageService.sendMessageToMQ(dto);
        return ResultFactory.buildSuccessResult("订单已提交");
    }
}

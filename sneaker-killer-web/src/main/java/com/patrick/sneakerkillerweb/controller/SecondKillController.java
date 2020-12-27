package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillerservice.dto.SecondKillDto;
import com.patrick.sneakerkillerservice.service.SendMessageService;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class SecondKillController {

    SendMessageService sendMessageService;

    public SecondKillController(SendMessageService sendMessageService){
        this.sendMessageService = sendMessageService;
    }

    @PostMapping(value = "/item/order")
    public Result executeKill(@RequestBody @Validated SecondKillDto dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultFactory.buildFailResult("invalid parameter");
        }
        sendMessageService.sendMessageToMQ(dto);
        return ResultFactory.buildSuccessResult("订单已提交");
    }
}

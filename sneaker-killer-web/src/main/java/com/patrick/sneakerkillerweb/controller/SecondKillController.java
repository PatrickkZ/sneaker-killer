package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillerservice.exception.AlreadyBoughtException;
import com.patrick.sneakerkillerservice.service.SecondKillService;
import com.patrick.sneakerkillerweb.dto.SecondKillDto;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondKillController {

    SecondKillService secondKillService;

    public SecondKillController(SecondKillService secondKillService){
        this.secondKillService = secondKillService;
    }

    @PostMapping(value = "/api/sk")
    public Result executeKill(@RequestBody @Validated SecondKillDto dto){
        try {
            boolean success = secondKillService.executeKill(dto.getItemId(), dto.getSize(), dto.getUserId());
            if (!success){
                return ResultFactory.buildFailResult("商品不在抢购时间段内或库存不足");
            }
        } catch (AlreadyBoughtException e){
            // 通知用户已经购买过
            return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildSuccessResult("订单提交成功");
    }
}

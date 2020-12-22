package com.patrick.sneakerkillerweb.controller;

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
        return ResultFactory.buildSuccessResult(null);
    }
}

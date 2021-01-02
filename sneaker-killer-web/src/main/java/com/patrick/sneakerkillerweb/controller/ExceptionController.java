package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultCode;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ShiroException.class)
    public Result handleUnAuthorize(){
        return ResultFactory.buildResult(ResultCode.UNAUTHORIZED, "请重新登录", null);
    }
}

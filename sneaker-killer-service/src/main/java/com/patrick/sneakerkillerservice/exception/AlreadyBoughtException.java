package com.patrick.sneakerkillerservice.exception;

/**
 * 已经购买过异常
 */
public class AlreadyBoughtException extends Exception{
    private String message;

    public AlreadyBoughtException(){

    }

    public AlreadyBoughtException(String message){
        super(message);
        this.message = message;
    }
}

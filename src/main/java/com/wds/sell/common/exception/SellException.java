package com.wds.sell.common.exception;

import com.wds.sell.common.enums.ExceptionEnum;

public class SellException extends RuntimeException {
    private Integer code;
    public SellException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
        this.code=exceptionEnum.getCode();
    }
    public SellException(Integer code,String message){
        super(message);
        this.code=code;
    }
}

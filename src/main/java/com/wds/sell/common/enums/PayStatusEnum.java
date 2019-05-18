package com.wds.sell.common.enums;

import lombok.Getter;

public enum PayStatusEnum implements CodeEnum {
    WAIT(0,"未支付"),
    SUCCESS(1,"支付成功")
    ;
    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

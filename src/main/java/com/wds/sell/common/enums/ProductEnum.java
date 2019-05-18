package com.wds.sell.common.enums;

import lombok.Getter;

@Getter
public enum ProductEnum implements CodeEnum {
    UP(0,"上架"),
    DOWN(1,"下架")
    ;
    private Integer code;
    private String msg;

    ProductEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

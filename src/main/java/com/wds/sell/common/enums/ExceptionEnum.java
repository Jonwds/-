package com.wds.sell.common.enums;

import lombok.Getter;

@Getter
public enum  ExceptionEnum {
    PARAM_ERROR(500,"参数错误"),
    CART_EMPTY(501,"购物车为空"),
    PRODUCT_NOT_FOUND(502,"未找到商品"),
    STOCK_NOT_ENOUGH(503,"库存不足"),
    ORDER_NOT_EXIST(504,"订单不存在"),
    ORDER_DETAIL_NOT_EXIST(505,"订单详情不存在"),
    ORDER_STATUS_ERROR(506,"订单状态不正确"),
    ORDER_PAY_STATUS_ERROR(507,"订单支付状态不正确"),
    ORDER_UPDATE_FAIL(508,"更新订单失败"),
    ORDER_DETAIL_EMPTY(509,"订单中无商品详情"),
    ORDER_OWNER_ERROR(510,"订单不属于该用户"),

    ;
    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

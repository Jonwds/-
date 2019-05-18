package com.wds.sell.dataobject.vo;

import lombok.Data;

//http请求返回的最外层对象
@Data
public class ResultVo<T> {

    private Integer code;

    private String msg;

    private T data;






}

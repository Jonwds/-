package com.wds.sell.dataobject.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductVo {
    //类目名字
    @JsonProperty("name")
    private String categoryName;

    //类目编号
    @JsonProperty("type")
    private Integer categoryType;

    //商品集合
    @JsonProperty("foods")
    private List<ProductInfoVo> productInfoList;

}

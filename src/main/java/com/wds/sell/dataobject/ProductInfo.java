package com.wds.sell.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wds.sell.common.enums.ProductEnum;
import com.wds.sell.common.util.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class ProductInfo {
    @Id
    private String productId;//为什么是String?因为是自己产的随机字符

    private String productName;//名字

    private BigDecimal productPrice;//价格

    private Integer productStock;//库存

    private String productDescription;//商品描述

    private String productIcon;//商品小图

    private Integer productStatus;//商品状态,0代表上架,1代表下架

    private Integer categoryType;//商品类目编号

    private Date createTime;

    private Date updateTime;

    @JsonIgnore
    public ProductEnum getProductEnum(){
        return EnumUtil.getByCode(productStatus,ProductEnum.class);
    }
}

package com.wds.sell.service;

import com.wds.sell.dataobject.ProductInfo;
import com.wds.sell.dataobject.dto.CartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductInfoService {

    ProductInfo findById(String productId);//商品详情

    List<ProductInfo> findBycategoryType(Integer categoryId);//类目详情

    List<ProductInfo> findAllUp();//查询所有上架商品

    Page<ProductInfo> findAll(Pageable pageable);//需要分页

    ProductInfo save(ProductInfo productInfo);//保存商品

    //上架
    ProductInfo onSale(String productId);
    //下架
    ProductInfo offSale(String productId);

    //加库存
    void increaseStock(List<CartDto> cartDtoList);
    //减库存
    void decreaseStock(List<CartDto> cartDtoList);
}

package com.wds.sell.repository;

import com.wds.sell.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {

    List<ProductInfo> findByProductStatus(Integer status);

    List<ProductInfo> findByCategoryType(Integer categoryType);

}

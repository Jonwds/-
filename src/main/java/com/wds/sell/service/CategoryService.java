package com.wds.sell.service;

import com.wds.sell.dataobject.ProductCategory;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

     ProductCategory findById(Integer categoryId);

     List<ProductCategory> findAll();

     List<ProductCategory> findByCategoryTypeList(List<Integer> list);

     ProductCategory save(ProductCategory productCategory);

}

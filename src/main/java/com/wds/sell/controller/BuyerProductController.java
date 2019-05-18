package com.wds.sell.controller;

import com.wds.sell.common.util.ResultVoUtil;
import com.wds.sell.dataobject.ProductCategory;
import com.wds.sell.dataobject.ProductInfo;
import com.wds.sell.service.impl.CategoryServiceImpl;
import com.wds.sell.service.impl.ProductInfoServiceImpl;
import com.wds.sell.dataobject.vo.ProductInfoVo;
import com.wds.sell.dataobject.vo.ProductVo;
import com.wds.sell.dataobject.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductInfoServiceImpl productInfoService;
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("list")
    public ResultVo list(){
        //1.查询所有商家的商品
        List<ProductInfo> productInfoList = productInfoService.findAllUp();
        //2.查询类目
        List<Integer> categoryTypeList = new ArrayList<>();
        for (ProductInfo info:productInfoList){
            categoryTypeList.add(info.getCategoryType());
        }
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeList(categoryTypeList);
        /*  Java8 Lambda 表达式
        List<Integer> categoryTypeList1=productInfoList.stream()
                .map(e->e.getCategoryType())
                .collect(Collectors.toList());
        */

        //3.拼装
        List<ProductVo> productVoList = ProductVoListUtil(productCategoryList,productInfoList);

        return ResultVoUtil.success(productVoList);

    }

    private List<ProductVo> ProductVoListUtil(List<ProductCategory> productCategoryList,List<ProductInfo> productInfoList){
        List<ProductVo> productVoList = new ArrayList<>();
        for (ProductCategory productCategory:productCategoryList){
            ProductVo productVo = new ProductVo();//productVo里有三个对象:类目名、类目编号、商品集合
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            //封装productVo里的商品集合
            for (ProductInfo productInfo:productInfoList){
                if (productInfo.getCategoryType()==productCategory.getCategoryType()){
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo,productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVo.setProductInfoList(productInfoVoList);
            productVoList.add(productVo);
        }
        return productVoList;
    }


}

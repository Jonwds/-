package com.wds.sell.service.impl;

import com.wds.sell.dataobject.ProductInfo;
import com.wds.sell.service.ProductInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoServiceImpl service;

    @Test
    public void findById() {
        ProductInfo productInfo = service.findById("123456");
        System.out.println(productInfo);
    }

    @Test
    public void findAllUp() {
        List<ProductInfo> list = service.findAllUp();
        System.out.println(list.size());
    }

    @Test
    public void findAll() {
        PageRequest pageRequest = new PageRequest(0,10);
        Page<ProductInfo> page = service.findAll(pageRequest);
        System.out.println(page.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("皮蛋瘦肉粥");
        productInfo.setProductPrice(new BigDecimal(18.5));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("便宜好喝");
        productInfo.setProductIcon("http://xxxx.jpg");
        productInfo.setCategoryType(2);
        productInfo.setProductStatus(0);
        ProductInfo result = service.save(productInfo);
        System.out.println(result);
    }
}
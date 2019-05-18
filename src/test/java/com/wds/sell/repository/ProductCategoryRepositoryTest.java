package com.wds.sell.repository;

import com.wds.sell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void find(){
        ProductCategory category = repository.findOne(1);
        System.out.println(category);
    }

    @Test
    public void save(){
        ProductCategory category = new ProductCategory();
        category.setCategoryName("年中大促");
        category.setCategoryType(2);
        repository.save(category);
    }

    @Test
    public void findByCategoryTypeIn(){
        List<Integer> list = Arrays.asList(1,2,3,4);
        List<ProductCategory> resultList = repository.findByCategoryTypeIn(list);
    }
}
package com.yongqi.sell.service.impl;

import com.yongqi.sell.dataobject.ProductCategory;
import com.yongqi.sell.service.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
    @Autowired
    CategoryService categoryService;
    @Test
    public void findOne() {
        ProductCategory productCategory = categoryService.findOne(2);
        Assert.assertEquals(new  Integer(2) ,productCategory.getCategoryId());
    }

    @Test
    public void findAll() {
        List<ProductCategory> result = categoryService.findAll();
        Assert.assertNotEquals(0,result.size());
    }

    @Test
    public void findByCategoryTypeIn() {
    }

    @Test
    public void save() {
    }
}
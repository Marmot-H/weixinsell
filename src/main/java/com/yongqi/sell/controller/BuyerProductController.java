package com.yongqi.sell.controller;

import com.yongqi.sell.dataobject.ProductCategory;
import com.yongqi.sell.dataobject.ProductInfo;
import com.yongqi.sell.service.CategoryService;
import com.yongqi.sell.service.ProductService;
import com.yongqi.sell.utils.ResultVoUtil;
import com.yongqi.sell.vo.ProductCategoryVo;
import com.yongqi.sell.vo.ProductInfoVo;
import com.yongqi.sell.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @GetMapping("/list")
    public ResultVo list(){
        //1. 查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //2. 查询类目(一次性查询)
//        List<Integer> categoryTypeList = new ArrayList<>();
        //传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
        //精简方法(java8, lambda)
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        //3. 数据拼装
        List<ProductCategoryVo> productVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategoryList) {
            ProductCategoryVo productVO = new ProductCategoryVo();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVo> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo: productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVo productInfoVO = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVoList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVoUtil.success(productVOList);
    }
}

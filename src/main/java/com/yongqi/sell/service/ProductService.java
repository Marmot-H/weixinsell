package com.yongqi.sell.service;

import com.yongqi.sell.dataobject.ProductInfo;
import com.yongqi.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductInfo findOne(String productId);
    /**
     * 查询所有在架商品列表
     * @return
     */
    List<ProductInfo> findUpAll();
//    分页查询所有商品
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);
//    加库存
    void addProductStock(List<CartDTO> cartDTO);
//    减库存
    void deleteProductStock(List<CartDTO> cartDTO);
}

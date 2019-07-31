package com.yongqi.sell.dao;

import com.yongqi.sell.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<ProductInfo,String> {
//    根据商品状态来差
    List<ProductInfo> findByProductStatus(Integer ProductStatus);
}

package com.yongqi.sell.service.impl;

import com.yongqi.sell.dao.ProductDao;
import com.yongqi.sell.dataobject.ProductInfo;
import com.yongqi.sell.dto.CartDTO;
import com.yongqi.sell.enums.ProductStatusEnum;
import com.yongqi.sell.enums.ResultEnum;
import com.yongqi.sell.exception.SellException;
import com.yongqi.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDao productDao;
    @Override
    public ProductInfo findOne(String productId) {
        return productDao.getOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productDao.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productDao.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productDao.save(productInfo);
    }

    @Override
    public void addProductStock(List<CartDTO> cartDTOs) {
        for (CartDTO cartDTO:cartDTOs){
            ProductInfo productInfo = productDao.getOne(cartDTO.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            Integer result = productInfo.getProductStock() + cartDTO.getProductStock();
            productInfo.setProductStock(result);
            productDao.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void deleteProductStock(List<CartDTO> cartDTOs) {
        for (CartDTO cartDTO1:cartDTOs){
            ProductInfo productInfo = productDao.getOne(cartDTO1.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            Integer result = productInfo.getProductStock() - cartDTO1.getProductStock();
            if (result<0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            productDao.save(productInfo);
        }
    }
}

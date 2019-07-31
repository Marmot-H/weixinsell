package com.yongqi.sell.dto;

import lombok.Data;

/**
 * 库存操作对象
 */
@Data
public class CartDTO {
    private String productId;
    private Integer productStock;

    public CartDTO(String productId, Integer productStock) {
        this.productId = productId;
        this.productStock = productStock;
    }
}

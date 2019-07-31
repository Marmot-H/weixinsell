package com.yongqi.sell.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    FORM_ERROR(1,"参数不正确"),

    PRODUCT_NOT_EXISTS(10,"商品不存在"),

    PRODUCT_STOCK_ERROR(11,"商品库存错误"),

    ORDER_NOT_EXISTS(12,"订单不存在"),

    ORDERDETAIL_NOT_EXIST(13, "订单详情不存在"),

    ORDER_STATUS_ERROR(14,"订单状态错误"),

    ORDER_UPDATE_FAILE(15,"订单更新失败"),

    ORDERDETAIL_EMPTY(16,"订单详情为空"),

    ORDER_PAY_ERROR(17,"订单支付状态不正确"),

    CART_EMPTY(18,"购物车为空"),

    ORDER_OWNER_ERROR(19,"订单不属于当前用户"),

    WXMP_ERROE(20,"微信授权错误")
    ;
    private Integer code;
    private String message;
    ResultEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}

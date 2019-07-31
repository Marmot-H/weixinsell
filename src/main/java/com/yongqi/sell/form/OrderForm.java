package com.yongqi.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderForm {
    /**
     * 买家姓名
     */
    @NotEmpty(message = "姓名不能为空")
    private String name;
    /**
     * 买家手机号
     */
    @NotEmpty(message = "手机号必填")
    private String phone;

    @NotEmpty(message = "地址必填")
    private String address;
    /**
     * 买家微信openid
     */
    @NotEmpty(message = "openid必填")
    private String openid;

    @NotEmpty(message = "购物车必填")
    private String items;
}

package com.yongqi.sell.service;

import com.yongqi.sell.dto.OrderDTo;

public interface BuyerService {
    //查询一个订单
    OrderDTo findOrderOne(String openid,String orderId);
    //取消订单
    OrderDTo cancelOrder(String openid,String orderId);
}

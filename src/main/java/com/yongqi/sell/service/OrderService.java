package com.yongqi.sell.service;

import com.yongqi.sell.dto.OrderDTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    //创建订单
    OrderDTo create(OrderDTo orderDTo);
    //查询单个订单
    OrderDTo findOne(String orderId);
    //查询订单列表
    Page<OrderDTo> findList(String buyerOpenid, Pageable pageable);
    //取消订单
    OrderDTo cancel(OrderDTo orderDTo);
    //完结订单
    OrderDTo finish(OrderDTo orderDTo);
    //支付订单
    OrderDTo paid(OrderDTo orderDTo);
    //查询订单列表
    Page<OrderDTo> findList(Pageable pageable);
}

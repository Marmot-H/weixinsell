package com.yongqi.sell.service.impl;

import com.yongqi.sell.dto.OrderDTo;
import com.yongqi.sell.enums.ResultEnum;
import com.yongqi.sell.exception.SellException;
import com.yongqi.sell.service.BuyerService;
import com.yongqi.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServerImpl implements BuyerService {
    @Autowired
    private OrderService orderService;
    @Override
    public OrderDTo findOrderOne(String openid, String orderId) {
        OrderDTo orderDTo = orderService.findOne(orderId);
        if (!orderDTo.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("查询订单 订单的openid不一致");
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTo;
    }

    @Override
    public OrderDTo cancelOrder(String openid, String orderId) {
        OrderDTo orderDTo = orderService.findOne(orderId);
        if (orderDTo == null) {
            log.error("查询订单 查不到订单");
            throw new SellException(ResultEnum.ORDER_NOT_EXISTS);
        }
        if (!orderDTo.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("查询订单 订单的openid不一致");
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTo;
    }
}

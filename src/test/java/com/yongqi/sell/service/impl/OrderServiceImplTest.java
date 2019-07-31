package com.yongqi.sell.service.impl;

import com.yongqi.sell.dataobject.OrderDetail;
import com.yongqi.sell.dto.OrderDTo;
import com.yongqi.sell.enums.OrderStatusEnum;
import com.yongqi.sell.enums.PayStatusEnum;
import com.yongqi.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
    @Autowired
    private OrderService orderService;
    private  final String buyerOpenid = "123645789";
    @Test
    public void create() {
        OrderDTo orderDTo = new OrderDTo();
        orderDTo.setBuyerName("王永启");
        orderDTo.setBuyerAddress("慕课网");
        orderDTo.setBuyerPhone("123456789");
        orderDTo.setBuyerOpenid(buyerOpenid);
        //购物车
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("132458");
        o1.setProductNumber(2);
        orderDetails.add(o1);
        OrderDetail o2 = new OrderDetail();
        o2.setProductId("123457");
        o2.setProductNumber(2);
        orderDetails.add(o2);
        orderDTo.setOrderDetailList(orderDetails);
        OrderDTo result = orderService.create(orderDTo);
        log.info("创建订单 result = {}",result);

    }

    @Test
    public void findOne() {
        OrderDTo orderDTo = orderService.findOne("1562290756846134982");
        log.info("查询单个订单 order={}",orderDTo);
    }

    @Test
    public void findList() {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDTo> orderDTOPage = orderService.findList(buyerOpenid, request);
        Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTo orderDTo = orderService.findOne("15622907568461349827");
        OrderDTo cancel = orderService.cancel(orderDTo);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), cancel.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTo orderDTo = orderService.findOne("15622907568461349827");
        OrderDTo cancel = orderService.finish(orderDTo);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), cancel.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTo orderDTo = orderService.findOne("15622907568461349827");
        OrderDTo cancel = orderService.paid(orderDTo);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), cancel.getPayStatus());
    }
}
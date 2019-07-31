package com.yongqi.sell.dao;

import com.yongqi.sell.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailDaoTest {
    @Autowired
    OrderDetailDao orderDetailDao;
    @Test
    public void findByOrderId() {
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderId("11111112");
        Assert.assertNotEquals(0, orderDetailList.size());
    }
    @Test
    public void save(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("1234567810");
        orderDetail.setOrderId("11111112");
        orderDetail.setProductIcon("http://xxxx.jpg");
        orderDetail.setProductId("11111112");
        orderDetail.setProductName("皮蛋粥");
        orderDetail.setProductPrice(new BigDecimal(2.2));
        orderDetail.setProductNumber(3);

        OrderDetail result = orderDetailDao.save(orderDetail);
        Assert.assertNotNull(result);
    }
}
package com.yongqi.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yongqi.sell.dataobject.OrderDetail;
import com.yongqi.sell.dto.OrderDTo;
import com.yongqi.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderFormToOrderDTO {
    public static OrderDTo converter(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTo orderDTo = new OrderDTo();
        orderDTo.setBuyerName(orderForm.getName());
        orderDTo.setBuyerPhone(orderForm.getPhone());
        orderDTo.setBuyerOpenid(orderForm.getOpenid());
        orderDTo.setBuyerAddress(orderForm.getAddress());
        List<OrderDetail> orderDetails = new ArrayList<>();
        try {
           orderDetails = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>(){}.getType());
        } catch (Exception e){
            log.error("对象转换出错");
        }
        orderDTo.setOrderDetailList(orderDetails);
        return orderDTo;

    }

}

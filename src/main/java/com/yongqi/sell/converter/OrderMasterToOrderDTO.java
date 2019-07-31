package com.yongqi.sell.converter;

import com.yongqi.sell.dataobject.OrderMaster;
import com.yongqi.sell.dto.OrderDTo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMasterToOrderDTO {

    public static OrderDTo convert(OrderMaster orderMaster){
        OrderDTo orderDTo = new OrderDTo();
        BeanUtils.copyProperties(orderMaster,orderDTo );
        return orderDTo;
    }
    public static List<OrderDTo> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }
}

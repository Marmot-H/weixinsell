package com.yongqi.sell.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yongqi.sell.dataobject.OrderDetail;
import com.yongqi.sell.enums.OrderStatusEnum;
import com.yongqi.sell.enums.PayStatusEnum;
import com.yongqi.sell.utils.EnumUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTo {
    //订单编号
    private String orderId;
    //    买家姓名
    private String buyerName;
    //    买家电话
    private String buyerPhone;
    //            地址
    private String buyerAddress;
    //    买家微信openId
    private String buyerOpenid;
    //                    订单总金额
    private BigDecimal orderAmount;
    //    订单状态默认为新下单
    private Integer orderStatus;
    //    支付状态默认为0未支付
    private Integer payStatus;
    //订单详情
    private List<OrderDetail> orderDetailList;
    //创建时间
    private String createTime;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class );
    }
    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus,PayStatusEnum.class );
    }
}

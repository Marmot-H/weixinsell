package com.yongqi.sell.dataobject;

import com.yongqi.sell.enums.OrderStatusEnum;
import com.yongqi.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@Proxy(lazy = false)
public class OrderMaster {
    //订单编号
    @Id
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
    private Integer orderStatus= OrderStatusEnum.NEW.getCode();
//    支付状态默认为0未支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    private String createTime;
}

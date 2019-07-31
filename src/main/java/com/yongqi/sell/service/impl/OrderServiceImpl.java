package com.yongqi.sell.service.impl;

import com.yongqi.sell.converter.OrderMasterToOrderDTO;
import com.yongqi.sell.dao.OrderDetailDao;
import com.yongqi.sell.dao.OrderMasterDao;
import com.yongqi.sell.dataobject.OrderDetail;
import com.yongqi.sell.dataobject.OrderMaster;
import com.yongqi.sell.dataobject.ProductInfo;
import com.yongqi.sell.dto.CartDTO;
import com.yongqi.sell.dto.OrderDTo;
import com.yongqi.sell.enums.OrderStatusEnum;
import com.yongqi.sell.enums.PayStatusEnum;
import com.yongqi.sell.enums.ResultEnum;
import com.yongqi.sell.exception.SellException;
import com.yongqi.sell.service.OrderService;
import com.yongqi.sell.service.ProductService;
import com.yongqi.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrderMasterDao orderMasterDao;
    @Override
    @Transactional
    public OrderDTo create(OrderDTo orderDTo) {
        String orderId = KeyUtil.getUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        List<CartDTO> cartDTOList = new ArrayList<>();//扣库存
        //1. 查询商品(数量,价格)
        for (OrderDetail orderDetail:orderDTo.getOrderDetailList()){
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            //2. 计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductNumber()))
                    .add(orderAmount);
            //3 订单详情入库
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail );
            orderDetailDao.save(orderDetail);
            //4. 扣库存
            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductNumber());
            cartDTOList.add(cartDTO);
            productService.deleteProductStock(cartDTOList);
        }
        //3. 写入订单数据库(orderMaster和orderDetail)
        OrderMaster orderMaster = new OrderMaster();
        orderDTo.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTo,orderMaster );
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterDao.save(orderMaster);
        //4. 扣库存
        return orderDTo;
    }

    @Override
    public OrderDTo findOne(String orderId) {
        OrderDTo orderDTo = new OrderDTo();
        OrderMaster orderMaster = orderMasterDao.getOne(orderId);
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXISTS);
        }
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        BeanUtils.copyProperties(orderMaster,orderDTo );
        orderDTo.setOrderDetailList(orderDetailList);
        return orderDTo;
    }

    @Override
    public Page<OrderDTo> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterDao.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTo> orderDToPage = OrderMasterToOrderDTO.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDToPage,pageable ,orderMasterPage.getTotalElements() );
    }

    @Override
    public OrderDTo cancel(OrderDTo orderDTo) {
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if (!orderDTo.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.info("取消订单，订单状态不正确");
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
//        修改订单状态
        orderDTo.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTo,orderMaster );
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if (updateResult == null){
            log.error("取消订单，更新失败 orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILE);
        }
        //返还库存
        if (CollectionUtils.isEmpty(orderDTo.getOrderDetailList())) {
            log.error("取消订单，订单中无商品详情 orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDERDETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = new ArrayList<>();
        List<OrderDetail> orderDetailList = orderDTo.getOrderDetailList();
        for (OrderDetail orderDetail:orderDetailList) {
            CartDTO cartDTO = new CartDTO(orderDetail.getProductId()
                    ,orderDetail.getProductNumber() );
            cartDTOList.add(cartDTO);
        }
        productService.addProductStock(cartDTOList);
        //如果已支付，需要退款
        if (orderDTo.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //todo
        }
        return orderDTo;
    }

    @Override
    @Transactional
    public OrderDTo finish(OrderDTo orderDTo) {
        //判断订单状态
        if (!orderDTo.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.info("完结订单，订单状态不正确");
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTo.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTo,orderMaster );
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if (updateResult == null){
            log.error("完结订单，更新失败 orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILE);
        }
        return orderDTo;
    }

    @Override
    @Transactional
    public OrderDTo paid(OrderDTo orderDTo) {
        //判断订单状态
        if (!orderDTo.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.info("支付订单，订单状态不正确");
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if (!orderDTo.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.info("支付订单，订单状态不正确");
            throw new SellException(ResultEnum.ORDER_PAY_ERROR);
        }
        //修改支付状态
        orderDTo.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTo,orderMaster );
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if (updateResult == null){
            log.error("支付订单，更新失败 orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILE);
        }
        return orderDTo;
    }
    //查询所有订单
    public Page<OrderDTo> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterDao.findAll(pageable);
        List<OrderDTo> orderDToPage = OrderMasterToOrderDTO.convert(orderMasterPage.getContent());
        return new PageImpl(orderDToPage,pageable ,orderMasterPage.getTotalElements() );
    }
}

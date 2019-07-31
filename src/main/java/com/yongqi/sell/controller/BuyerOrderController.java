package com.yongqi.sell.controller;

import com.yongqi.sell.converter.OrderFormToOrderDTO;
import com.yongqi.sell.dto.OrderDTo;
import com.yongqi.sell.enums.ResultEnum;
import com.yongqi.sell.exception.SellException;
import com.yongqi.sell.form.OrderForm;
import com.yongqi.sell.service.BuyerService;
import com.yongqi.sell.service.OrderService;
import com.yongqi.sell.utils.ResultVoUtil;
import com.yongqi.sell.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    /**
     * 创建订单
     */
    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerService buyerService;
    @PostMapping("/create")
    public ResultVo<Map<String,String>> create(@Valid OrderForm orderForm , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("创建订单 参数不正确 orderForm={}",orderForm);
            throw new SellException(ResultEnum.FORM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTo orderDTo = OrderFormToOrderDTO.converter(orderForm);
        if (CollectionUtils.isEmpty(orderDTo.getOrderDetailList())) {
            log.error("创建订单 购物车不能为空 ");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTo orderDTo1 = orderService.create(orderDTo);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",orderDTo1.getOrderId() );
        return ResultVoUtil.success(map);
    }
    /**
     * 订单列表
     */
    @GetMapping("/list")
    public ResultVo<List<OrderDTo>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page",defaultValue = "0") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size){
        if (StringUtils.isEmpty(openid)) {
            log.error("openid为空");
            throw new SellException(ResultEnum.FORM_ERROR);
        }
        PageRequest request = PageRequest.of(page,size );
        Page<OrderDTo> orderDToPage = orderService.findList(openid, request);
        return ResultVoUtil.success(orderDToPage.getContent());
    }
    /**
     * 订单详情
     */
    @GetMapping("/detail")
    public ResultVo detail(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        OrderDTo orderDTo = buyerService.findOrderOne(openid, orderId);
        return ResultVoUtil.success(orderDTo);
    }
    /**
     * 取消订单
     */
    @GetMapping("/cancel")
    public ResultVo cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
       buyerService.cancelOrder(openid,orderId);
        return ResultVoUtil.success();
    }

}

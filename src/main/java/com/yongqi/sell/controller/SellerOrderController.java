package com.yongqi.sell.controller;

import com.yongqi.sell.dto.OrderDTo;
import com.yongqi.sell.exception.SellException;
import com.yongqi.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {
    @Autowired
    private OrderService orderService;
    //查询所有订单
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "size",defaultValue = "10") Integer size,
                             Map<String,Object> map){
        PageRequest pageRequest =PageRequest.of(page - 1,size );
        Page<OrderDTo> orderDToPage = orderService.findList(pageRequest);
        map.put("orderDToPage" , orderDToPage );
        map.put("currentPage",page );
        map.put("size",size );
        return new ModelAndView("order/list",map);
    }
    //取消订单
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                               Map<String,Object> map) {
        try {
            OrderDTo orderDTo = orderService.findOne(orderId);
            orderService.cancel(orderDTo);
        }catch (SellException e){
            log.info("取消订单出错",e.getMessage());
            map.put("msg",e.getMessage() );
            map.put("url","/sell/seller/order/list" );
            return new ModelAndView("common/",map);
        }
        map.put("msg", "取消订单成功");
        map.put("url","/sell/seller/order/list" );
        return new ModelAndView("common/success",map);
    }
}

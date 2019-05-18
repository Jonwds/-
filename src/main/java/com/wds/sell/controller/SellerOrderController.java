package com.wds.sell.controller;

import com.wds.sell.common.enums.ExceptionEnum;
import com.wds.sell.dataobject.dto.OrderDto;
import com.wds.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单列表
     * @param page 第几页开始,0代表第一页
     * @param size 一页有多少条数据
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "size",defaultValue = "10")  Integer size,
                             Map<String,Object> map){
        PageRequest request = new PageRequest(page-1,size);
        Page<OrderDto> orderDtoPage = orderService.findList(request);
        map.put("orderDtoPage",orderDtoPage);
        map.put("currentPage",page);
        map.put("size",size);
        return new ModelAndView("/order/list",map);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                                Map<String,Object> map){
        try{
            OrderDto orderDto = orderService.findOne(orderId);
            orderService.cancel(orderDto);
        }catch (Exception e){
                log.error("[卖家取消订单]查询不到订单,异常:{}",e);
                map.put("msg", e.getMessage());
                map.put("url","/sell/seller/order/list");
                return new ModelAndView("common/error",map);
        }
        map.put("msg","订单取消成功.");
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("/common/success",map);
    }
    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId,
                               Map<String,Object> map){
        try{
            OrderDto orderDto = orderService.findOne(orderId);

            map.put("orderDto",orderDto);

        }catch (Exception e){
            log.error("[卖家查询订单详情]查询不到订单,异常:{}",e);
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }

        return new ModelAndView("/order/detail",map);
    }
    /**
     * 完结订单
     * @param orderId
     * @return
     */
    @GetMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId,
                               Map<String,Object> map){
        try{
            OrderDto orderDto = orderService.findOne(orderId);
            orderService.finish(orderDto);
        }catch (Exception e){
            log.error("[卖家完结订单]完结订单,异常:{}",e);
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }
        map.put("msg","订单完结成功.");
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("/common/success",map);
    }
}

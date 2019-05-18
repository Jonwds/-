package com.wds.sell.controller;

import com.wds.sell.common.enums.ExceptionEnum;
import com.wds.sell.common.exception.SellException;
import com.wds.sell.common.util.ResultVoUtil;
import com.wds.sell.dataobject.converter.OrderForm2OrderDtoConverter;
import com.wds.sell.dataobject.dto.CartDto;
import com.wds.sell.dataobject.dto.OrderDto;
import com.wds.sell.dataobject.form.OrderForm;
import com.wds.sell.dataobject.vo.ResultVo;
import com.wds.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/open")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    //创建订单
    @PostMapping("/create")
    public ResultVo<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("[创建订单]参数不正确,orderForm={}",orderForm);
            throw new SellException(ExceptionEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDto orderDto = OrderForm2OrderDtoConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDto.getOrderDetailList())){
            log.error("[创建订单]购物车为空,orderForm={}",orderForm);
            throw new SellException(ExceptionEnum.PARAM_ERROR);
        }
        OrderDto result = orderService.create(orderDto);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",result.getOrderId());
        return ResultVoUtil.success(map);
    }
    //查询订单列表
    @PostMapping("/list")
    public ResultVo<List<OrderDto>> list(@RequestParam(value = "openId",required = true) String openId,
                                             @RequestParam(value = "page",defaultValue = "0")int page,
                                             @RequestParam(value = "size",defaultValue = "10")int size){
        PageRequest request = new PageRequest(page, size);
        Page<OrderDto> orderDtoPage = orderService.findList(openId, request);

        return ResultVoUtil.success(orderDtoPage.getContent());
    }
    //查询单个订单详情
    @PostMapping("/detail")
    public ResultVo<OrderDto> findList(@RequestParam(value = "openId",required = true)String openId,
                                       @RequestParam(value = "orderId",required = true)String orderId){
        //todo  openid不安全

        OrderDto orderDto = orderService.findOne(orderId);
        if(!orderDto.getBuyerOpenid().equals(openId)){
            log.error("[查询订单]订单用户的openId与传入的不一致,openId={},orderDto={}",openId,orderDto);
            throw new SellException(ExceptionEnum.ORDER_OWNER_ERROR);
        }

        return ResultVoUtil.success(orderDto);
    }
    //取消订单
    @PostMapping("/cancel")
    public ResultVo cancel(@RequestParam(value = "openId",required = true)String openId,
                                    @RequestParam(value = "orderId",required = true)String orderId){

        OrderDto orderDto = orderService.findOne(orderId);
        //判断订单是不是属于该用户
        if(!orderDto.getBuyerOpenid().equals(openId)){
            log.error("[查询订单]订单用户的openId与传入的不一致,openId={},orderDto={}",openId,orderDto);
            throw new SellException(ExceptionEnum.ORDER_OWNER_ERROR);
        }
        orderService.cancel(orderDto);
        return ResultVoUtil.success();
    }
    //完结订单
    //OrderDto finish(OrderDto orderDto);
    //支付订单
    //OrderDto paid(OrderDto orderDto);
}

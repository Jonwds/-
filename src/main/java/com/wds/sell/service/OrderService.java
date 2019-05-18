package com.wds.sell.service;

import com.wds.sell.dataobject.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    //创建订单
    OrderDto create(OrderDto orderDto);
    //查询单个订单
    OrderDto findOne(String orderId);
    //买家端查询订单列表
    Page<OrderDto> findList(String buyerId, Pageable pageable);
    //取消订单
    OrderDto cancel(OrderDto orderDto);
    //完结订单
    OrderDto finish(OrderDto orderDto);
    //支付订单
    OrderDto paid(OrderDto orderDto);
    //卖家端查询订单列表
    Page<OrderDto> findList(Pageable pageable);
}

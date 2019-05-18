package com.wds.sell.dataobject.converter;

import com.wds.sell.dataobject.OrderDetail;
import com.wds.sell.dataobject.OrderMaster;
import com.wds.sell.dataobject.dto.OrderDto;
import org.hibernate.criterion.Order;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMaster2OrderDTOConverter {
    public static OrderDto convert(OrderMaster orderMaster){
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        return orderDto;
    }
    public static List<OrderDto> convert(List<OrderMaster> orderMasterList){
        return orderMasterList.stream().map(e->convert(e))
                .collect(Collectors.toList());
    }
}

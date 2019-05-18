package com.wds.sell.dataobject.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wds.sell.common.enums.ExceptionEnum;
import com.wds.sell.common.exception.SellException;
import com.wds.sell.dataobject.OrderDetail;
import com.wds.sell.dataobject.dto.CartDto;
import com.wds.sell.dataobject.dto.OrderDto;
import com.wds.sell.dataobject.form.OrderForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderForm2OrderDtoConverter{
    public static OrderDto convert(OrderForm orderForm){
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenId());
        Gson gson=new Gson();
        List<OrderDetail> orderDetailList=new ArrayList<>();
        try{
            orderDetailList=gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            log.error("[对象转换]错误,string={}",orderForm.getItems());
            throw new SellException(ExceptionEnum.PARAM_ERROR);
        }
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }
}

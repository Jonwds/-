package com.wds.sell.service.impl;

import com.wds.sell.common.enums.OrderStatusEnum;
import com.wds.sell.common.enums.PayStatusEnum;
import com.wds.sell.dataobject.OrderDetail;
import com.wds.sell.dataobject.dto.CartDto;
import com.wds.sell.dataobject.dto.OrderDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    private  OrderServiceImpl orderService;

    @Test
    public void create() {
        for (int i=0;i<25;i++){
            OrderDto orderDto = new OrderDto();
            orderDto.setBuyerAddress("慕课网总部");
            orderDto.setBuyerPhone("18868822111");
            orderDto.setBuyerName("张三");
            orderDto.setBuyerOpenid("ew3euwhd7sjw9diwkq");
            List<OrderDetail> list = new ArrayList<>();
            OrderDetail o1=new OrderDetail();
            o1.setProductId("123");o1.setProductQuantity(2);
            OrderDetail o2=new OrderDetail();
            o2.setProductId("234");o2.setProductQuantity(3);
            list.add(o1);list.add(o2);
            orderDto.setOrderDetailList(list);
            orderService.create(orderDto);
        }
    }

    @Test
    public void findOne() {
        OrderDto orderDto = orderService.findOne("1553581524985183805");
        System.out.println(orderDto);
    }

    @Test
    public void findList() {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDto> orderDtoPage = orderService.findList("ew3euwhd7sjw9diwkq", request);
        Assert.assertNotEquals(0,orderDtoPage.getTotalElements());
    }
    @Test
    public void findList2() {
        PageRequest request = new PageRequest(0,3);
        Page<OrderDto> orderDtoPage = orderService.findList(request);
        Assert.assertNotEquals(0,orderDtoPage.getTotalElements());
    }
    @Test
    public void cancal() {
        OrderDto orderDto = orderService.findOne("1553581524985183805");
        OrderDto result = orderService.cancel(orderDto);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDto orderDto = orderService.findOne("1553581524985183805");
        OrderDto result = orderService.finish(orderDto);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDto orderDto = orderService.findOne("1553581524985183805");
        OrderDto result = orderService.paid(orderDto);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getPayStatus());
    }
}
package com.wds.sell.service.impl;

import com.wds.sell.common.enums.ExceptionEnum;
import com.wds.sell.common.enums.OrderStatusEnum;
import com.wds.sell.common.enums.PayStatusEnum;
import com.wds.sell.common.exception.SellException;
import com.wds.sell.common.util.KeyUtil;
import com.wds.sell.dataobject.OrderDetail;
import com.wds.sell.dataobject.OrderMaster;
import com.wds.sell.dataobject.ProductInfo;
import com.wds.sell.dataobject.converter.OrderMaster2OrderDTOConverter;
import com.wds.sell.dataobject.dto.CartDto;
import com.wds.sell.dataobject.dto.OrderDto;
import com.wds.sell.repository.OrderDetailRepository;
import com.wds.sell.repository.OrderMasterRepository;
import com.wds.sell.service.OrderService;
import com.wds.sell.service.ProductInfoService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMasterRepository masterRepository;
    @Autowired
    private OrderDetailRepository detailRepository;
    @Autowired
    private ProductInfoService productService;

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        String orderId = KeyUtil.genUniqueKey();//随机ID
        BigDecimal orderAmout = new BigDecimal(0);
        List<CartDto> cartDtoList = new ArrayList<>();
        //商品的单价和库存 必须由我们自己从数据库取出来

        for (OrderDetail orderDetail:orderDto.getOrderDetailList()){
            //1.查询商品（数量、价格）
            ProductInfo productInfo = productService.findById(orderDetail.getProductId());
            if(productInfo==null){
                throw new SellException(ExceptionEnum.PRODUCT_NOT_FOUND );
            }
            //2.计算总价
            orderAmout=productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmout) ;
            //写入数据库OrderDetail
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            BeanUtils.copyProperties(productInfo,orderDetail);
            detailRepository.save(orderDetail);

            CartDto cartDto = new CartDto(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDtoList.add(cartDto);
        }
        //3.订单写入数据库（OrderMaster）
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderId(orderId);
        BeanUtils.copyProperties(orderDto,orderMaster);//先拷贝再赋值,因为Null也会被copy过去
        orderMaster.setOrderAmount(orderAmout);

        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        masterRepository.save(orderMaster);

        //4.扣库存  判断库存是否充足
        productService.decreaseStock(cartDtoList);
        return orderDto;
    }

    @Override
    public OrderDto findOne(String orderId) {
        OrderMaster orderMaster = masterRepository.findOne(orderId);
        if (orderMaster==null){
            throw new SellException(ExceptionEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = detailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ExceptionEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }

    @Override
    public Page<OrderDto> findList(String buyerId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = masterRepository.findByBuyerOpenid(buyerId, pageable);
        List<OrderDto> orderDtoList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return  new PageImpl<OrderDto>(orderDtoList, pageable, orderMasterPage.getTotalPages());
    }

    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {
        OrderMaster orderMaster=new OrderMaster();

        //1.判断订单状态
        if(!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("[取消订单]订单状态不正确,orderId={},orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
        }
        //2.修改订单状态
        orderDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster result = masterRepository.save(orderMaster);
        if(result==null){
            log.error("[取消订单]更新失败,orderMaster={}",orderMaster);
            throw new SellException(ExceptionEnum.ORDER_UPDATE_FAIL);
        }
        //3.返回库存
        if(CollectionUtils.isEmpty(orderDto.getOrderDetailList())){
                log.error("[取消订单]订单中无商品详情,oderDto={}",orderDto);
            throw new SellException(ExceptionEnum.ORDER_UPDATE_FAIL);
        }
        List<CartDto> cartDtoList = orderDto.getOrderDetailList().stream()
                .map(e -> new CartDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDtoList);

        //4.如果已经支付,需要退款
        if(orderDto.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //todo 退款


        }

        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto finish(OrderDto orderDto) {
        //判断订单状态
        if(!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("[完结订单]订单状态不正确,orderId={},orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
        }

        //更新订单状态
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster result = masterRepository.save(orderMaster);
        if(result==null){
            log.error("[完结订单]更新订单失败,orderMaster={}",orderMaster);
            throw new SellException(ExceptionEnum.ORDER_UPDATE_FAIL);
        }
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto paid(OrderDto orderDto) {
        //判断订单状态
        if(!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("[支付订单]订单状态不正确,orderId={},orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if(!orderDto.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("[支付订单]订单支付状态不正确,orderId={},payStatus={}",orderDto.getOrderId(),orderDto.getPayStatus());
            throw new SellException(ExceptionEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster result = masterRepository.save(orderMaster);
        if(result==null){
            log.error("[支付订单]支付订单失败,orderMaster={}",orderMaster);
            throw new SellException(ExceptionEnum.ORDER_UPDATE_FAIL);
        }
        return orderDto;
    }
    @Override
    public Page<OrderDto> findList(Pageable pageable){
        Page<OrderMaster> orderMasterPage = masterRepository.findAll(pageable);
        List<OrderDto> orderDtoList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDtoList,pageable,orderMasterPage.getTotalElements());
    }
}

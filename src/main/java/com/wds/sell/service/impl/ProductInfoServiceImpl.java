package com.wds.sell.service.impl;

import com.wds.sell.common.enums.ExceptionEnum;
import com.wds.sell.common.exception.SellException;
import com.wds.sell.dataobject.ProductInfo;
import com.wds.sell.common.enums.ProductEnum;
import com.wds.sell.dataobject.dto.CartDto;
import com.wds.sell.repository.ProductInfoRepository;
import com.wds.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findById(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findBycategoryType(Integer categoryType) {
        return repository.findByCategoryType(categoryType);
    }

    @Override
    public List<ProductInfo> findAllUp() {
        return repository.findByProductStatus(ProductEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }
    @Override
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = repository.findOne(productId);
        if (productInfo == null){
            throw new SellException(ExceptionEnum.PRODUCT_NOT_FOUND);
        }
        if (productInfo.getProductStatus().equals(ProductEnum.DOWN.getCode())){
            productInfo.setProductStatus(ProductEnum.UP.getCode());
            return repository.save(productInfo);
        }else{
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
        }
    }
    @Override
    public ProductInfo offSale( String productId) {
        ProductInfo productInfo = repository.findOne(productId);
        if (productInfo == null){
            throw new SellException(ExceptionEnum.PRODUCT_NOT_FOUND);
        }

        if (productInfo.getProductStatus().equals(ProductEnum.UP.getCode())){
            productInfo.setProductStatus(ProductEnum.DOWN.getCode());
            return repository.save(productInfo);
        }else
            throw new SellException(ExceptionEnum.ORDER_STATUS_ERROR);
    }

    @Override
    public void increaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto:cartDtoList){
            ProductInfo productInfo = repository.findOne(cartDto.getProductId());
            if (productInfo==null){
                throw new SellException(ExceptionEnum.PRODUCT_NOT_FOUND);
            }
            //库存+购物车里的数量
            Integer result=productInfo.getProductStock()+cartDto.getProductQuantity();
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList){
            ProductInfo productInfo = repository.findOne(cartDto.getProductId());
            if (productInfo==null){
                throw new SellException(ExceptionEnum.PRODUCT_NOT_FOUND);
            }
            //库存-购物车里的数量
            Integer result=productInfo.getProductStock()-cartDto.getProductQuantity();
            if (result<0){
                throw new SellException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }
}

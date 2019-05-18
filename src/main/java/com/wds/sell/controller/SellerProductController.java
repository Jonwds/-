package com.wds.sell.controller;

import com.wds.sell.dataobject.ProductCategory;
import com.wds.sell.dataobject.ProductInfo;
import com.wds.sell.dataobject.dto.OrderDto;
import com.wds.sell.service.CategoryService;
import com.wds.sell.service.OrderService;
import com.wds.sell.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;
    
    @Autowired
    private CategoryService categoryService;

    /**
     * 商品详情列表
     * @param page 第几页开始,0代表第一页
     * @param size 一页有多少条数据
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "size",defaultValue = "10")  Integer size,
                             Map<String,Object> map){
        PageRequest request = new PageRequest(page-1,size);
        Page<ProductInfo> productInfoPage = productInfoService.findAll(request);
        map.put("productInfoPage",productInfoPage);
        map.put("currentPage",page);
        map.put("size",size);
        return new ModelAndView("/product/list",map);
    }

    /**
     * 新增或者修改订单
     * @param productId
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId",required = false) String productId,
                                Map<String,Object> map){
        if (StringUtils.isNotBlank(productId)){
            ProductInfo productInfo = productInfoService.findById(productId);
            map.put("productInfo",productInfo);
        }
        //所有类目
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList",categoryList);

        return new ModelAndView("/product/index",map);
    }

    /**
     * 上架
     * @param productId
     * @return
     */
    @GetMapping("/on_sale")
    public ModelAndView onSale(@RequestParam("productId") String productId,
                               Map<String,Object> map){
        try{
            productInfoService.onSale(productId);
        }catch (Exception e){
            log.error("[卖家修改订单]上架订单,异常:{}",e);
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("msg","订单上架成功.");
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("/common/success",map);
    }
    /**
     * 下架
     * @param productId
     * @return
     */
    @GetMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                               Map<String,Object> map){
        try{
            productInfoService.offSale(productId);
        }catch (Exception e){
            log.error("[卖家修改订单]下架订单,异常:{}",e);
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("msg","订单下架成功.");
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("/common/success",map);
    }
}

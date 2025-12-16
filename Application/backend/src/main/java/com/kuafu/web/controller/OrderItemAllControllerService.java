package com.kuafu.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import java.io.Serializable;
import com.kuafu.common.login.IControllerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.kuafu.login.annotation.*;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.OrderItemAll;
import com.kuafu.web.service.IOrderItemAllService;
import com.kuafu.web.vo.OrderItemAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IOrderInfoService;
import com.kuafu.web.entity.OrderInfo;
import com.kuafu.web.service.IProductInfoService;
import com.kuafu.web.entity.ProductInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 订单商品明细 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("OrderItemAllControllerService")
@RequiredArgsConstructor
public class OrderItemAllControllerService implements IControllerService<OrderItemAll> {

    private final IOrderItemAllService orderItemAllService;





    public BaseResponse page( OrderItemAllPageVO pageVO){
        IPage<OrderItemAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<OrderItemAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("oi.order_item_id");
        LambdaQueryWrapper<OrderItemAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getOrderNo())) {
                queryWrapper.like(OrderItemAll::getOrderNo, pageVO.getOrderNo());
            }
            if(pageVO.getOrderStatusEnumOrderStatusEnumId1() != null){
                queryWrapper.eq(OrderItemAll::getOrderStatusEnumOrderStatusEnumId1, pageVO.getOrderStatusEnumOrderStatusEnumId1());
            }
            if(pageVO.getShelfLife() != null){
                queryWrapper.eq(OrderItemAll::getShelfLife, pageVO.getShelfLife());
            }
            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(OrderItemAll::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }
            if(pageVO.getCommission() != null){
                queryWrapper.eq(OrderItemAll::getCommission, pageVO.getCommission());
            }
        if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
                queryWrapper.like(OrderItemAll::getBarcode, pageVO.getBarcode());
            }
        if(StringUtils.isNotEmpty(pageVO.getBrand())) {
                queryWrapper.like(OrderItemAll::getBrand, pageVO.getBrand());
            }
            if(pageVO.getQuantity() != null){
                queryWrapper.eq(OrderItemAll::getQuantity, pageVO.getQuantity());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(OrderItemAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
            if(pageVO.getTotalPrice() != null){
                queryWrapper.eq(OrderItemAll::getTotalPrice, pageVO.getTotalPrice());
            }
            if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
                queryWrapper.eq(OrderItemAll::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
                queryWrapper.like(OrderItemAll::getSpecification, pageVO.getSpecification());
            }
        if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                queryWrapper.like(OrderItemAll::getSerialNumber, pageVO.getSerialNumber());
            }
            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(OrderItemAll::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }
            if(pageVO.getUnitPrice() != null){
                queryWrapper.eq(OrderItemAll::getUnitPrice, pageVO.getUnitPrice());
            }
            if(pageVO.getProfitAmount() != null){
                queryWrapper.eq(OrderItemAll::getProfitAmount, pageVO.getProfitAmount());
            }
        if(StringUtils.isNotEmpty(pageVO.getProductName())) {

                        String s_string = pageVO.getProductName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(OrderItemAll::getProductName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getOrderInfoOrderInfoId1() != null){
                queryWrapper.eq(OrderItemAll::getOrderInfoOrderInfoId1, pageVO.getOrderInfoOrderInfoId1());
            }
            if(pageVO.getOrderItemId() != null){
                queryWrapper.eq(OrderItemAll::getOrderItemId, pageVO.getOrderItemId());
            }
            if(pageVO.getShippingFee() != null){
                queryWrapper.eq(OrderItemAll::getShippingFee, pageVO.getShippingFee());
            }
            if(pageVO.getOrderDate() != null){
                queryWrapper.eq(OrderItemAll::getOrderDate, pageVO.getOrderDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getGeneralOrders())) {
                queryWrapper.like(OrderItemAll::getGeneralOrders, pageVO.getGeneralOrders());
            }
        if(StringUtils.isNotEmpty(pageVO.getUnit())) {
                queryWrapper.like(OrderItemAll::getUnit, pageVO.getUnit());
            }
            if(pageVO.getTotalAmount() != null){
                queryWrapper.eq(OrderItemAll::getTotalAmount, pageVO.getTotalAmount());
            }
            if(pageVO.getMinStock() != null){
                queryWrapper.eq(OrderItemAll::getMinStock, pageVO.getMinStock());
            }
        if(StringUtils.isNotEmpty(pageVO.getCategory())) {
                queryWrapper.like(OrderItemAll::getCategory, pageVO.getCategory());
            }

                    orderItemAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(OrderItemAllPageVO vo){
            LambdaQueryWrapper<OrderItemAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                    queryWrapper.eq(OrderItemAll::getOrderNo, vo.getOrderNo());
                }
                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
                    queryWrapper.eq(OrderItemAll::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
                }
                if(vo.getShelfLife() != null){
                    queryWrapper.eq(OrderItemAll::getShelfLife, vo.getShelfLife());
                }
                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(OrderItemAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
                if(vo.getCommission() != null){
                    queryWrapper.eq(OrderItemAll::getCommission, vo.getCommission());
                }
            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                    queryWrapper.eq(OrderItemAll::getBarcode, vo.getBarcode());
                }
            if(StringUtils.isNotEmpty(vo.getBrand())) {
                    queryWrapper.eq(OrderItemAll::getBrand, vo.getBrand());
                }
                if(vo.getQuantity() != null){
                    queryWrapper.eq(OrderItemAll::getQuantity, vo.getQuantity());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(OrderItemAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getTotalPrice() != null){
                    queryWrapper.eq(OrderItemAll::getTotalPrice, vo.getTotalPrice());
                }
                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                    queryWrapper.eq(OrderItemAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                    queryWrapper.eq(OrderItemAll::getSpecification, vo.getSpecification());
                }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                    queryWrapper.eq(OrderItemAll::getSerialNumber, vo.getSerialNumber());
                }
                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(OrderItemAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
                if(vo.getUnitPrice() != null){
                    queryWrapper.eq(OrderItemAll::getUnitPrice, vo.getUnitPrice());
                }
                if(vo.getProfitAmount() != null){
                    queryWrapper.eq(OrderItemAll::getProfitAmount, vo.getProfitAmount());
                }
            if(StringUtils.isNotEmpty(vo.getProductName())) {
                            queryWrapper.like(OrderItemAll::getProductName, vo.getProductName());
                }
                if(vo.getOrderInfoOrderInfoId1() != null){
                    queryWrapper.eq(OrderItemAll::getOrderInfoOrderInfoId1, vo.getOrderInfoOrderInfoId1());
                }
                if(vo.getShippingFee() != null){
                    queryWrapper.eq(OrderItemAll::getShippingFee, vo.getShippingFee());
                }
                if(vo.getOrderDate() != null){
                    queryWrapper.eq(OrderItemAll::getOrderDate, vo.getOrderDate());
                }
            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
                    queryWrapper.eq(OrderItemAll::getGeneralOrders, vo.getGeneralOrders());
                }
            if(StringUtils.isNotEmpty(vo.getUnit())) {
                    queryWrapper.eq(OrderItemAll::getUnit, vo.getUnit());
                }
                if(vo.getTotalAmount() != null){
                    queryWrapper.eq(OrderItemAll::getTotalAmount, vo.getTotalAmount());
                }
                if(vo.getMinStock() != null){
                    queryWrapper.eq(OrderItemAll::getMinStock, vo.getMinStock());
                }
            if(StringUtils.isNotEmpty(vo.getCategory())) {
                    queryWrapper.eq(OrderItemAll::getCategory, vo.getCategory());
                }
                return ResultUtils.success(orderItemAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(OrderItemAllPageVO vo) {


                LambdaQueryWrapper<OrderItemAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                                    queryWrapper.eq(OrderItemAll::getOrderNo, vo.getOrderNo());
                                }


                                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
                                    queryWrapper.eq(OrderItemAll::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
                                }


                                if(vo.getShelfLife() != null){
                                    queryWrapper.eq(OrderItemAll::getShelfLife, vo.getShelfLife());
                                }


                                if(vo.getCustomerInfoCustomerInfoId1() != null){
                                    queryWrapper.eq(OrderItemAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                                }


                                if(vo.getCommission() != null){
                                    queryWrapper.eq(OrderItemAll::getCommission, vo.getCommission());
                                }


                            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                                    queryWrapper.eq(OrderItemAll::getBarcode, vo.getBarcode());
                                }


                            if(StringUtils.isNotEmpty(vo.getBrand())) {
                                    queryWrapper.eq(OrderItemAll::getBrand, vo.getBrand());
                                }


                                if(vo.getQuantity() != null){
                                    queryWrapper.eq(OrderItemAll::getQuantity, vo.getQuantity());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(OrderItemAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                                if(vo.getTotalPrice() != null){
                                    queryWrapper.eq(OrderItemAll::getTotalPrice, vo.getTotalPrice());
                                }


                                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                                    queryWrapper.eq(OrderItemAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                                    queryWrapper.eq(OrderItemAll::getSpecification, vo.getSpecification());
                                }


                            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                                    queryWrapper.eq(OrderItemAll::getSerialNumber, vo.getSerialNumber());
                                }


                                if(vo.getProductInfoProductInfoId1() != null){
                                    queryWrapper.eq(OrderItemAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                                }


                                if(vo.getUnitPrice() != null){
                                    queryWrapper.eq(OrderItemAll::getUnitPrice, vo.getUnitPrice());
                                }


                                if(vo.getProfitAmount() != null){
                                    queryWrapper.eq(OrderItemAll::getProfitAmount, vo.getProfitAmount());
                                }


                            if(StringUtils.isNotEmpty(vo.getProductName())) {
                                            queryWrapper.like(OrderItemAll::getProductName, vo.getProductName());
                                }


                                if(vo.getOrderInfoOrderInfoId1() != null){
                                    queryWrapper.eq(OrderItemAll::getOrderInfoOrderInfoId1, vo.getOrderInfoOrderInfoId1());
                                }


                                if(vo.getOrderItemId() != null){
                                    queryWrapper.eq(OrderItemAll::getOrderItemId, vo.getOrderItemId());
                                }


                                if(vo.getShippingFee() != null){
                                    queryWrapper.eq(OrderItemAll::getShippingFee, vo.getShippingFee());
                                }


                                if(vo.getOrderDate() != null){
                                    queryWrapper.eq(OrderItemAll::getOrderDate, vo.getOrderDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
                                    queryWrapper.eq(OrderItemAll::getGeneralOrders, vo.getGeneralOrders());
                                }


                            if(StringUtils.isNotEmpty(vo.getUnit())) {
                                    queryWrapper.eq(OrderItemAll::getUnit, vo.getUnit());
                                }


                                if(vo.getTotalAmount() != null){
                                    queryWrapper.eq(OrderItemAll::getTotalAmount, vo.getTotalAmount());
                                }


                                if(vo.getMinStock() != null){
                                    queryWrapper.eq(OrderItemAll::getMinStock, vo.getMinStock());
                                }


                            if(StringUtils.isNotEmpty(vo.getCategory())) {
                                    queryWrapper.eq(OrderItemAll::getCategory, vo.getCategory());
                                }

                // OrderItemAll entity = this.orderItemAllService.getById(orderItemId);
                    final Page<OrderItemAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    orderItemAllService.pageNew(page, vo, queryWrapper);
                    final List<OrderItemAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public OrderItemAll getById(Serializable orderItemId) {

                    LambdaQueryWrapper<OrderItemAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(OrderItemAll::getOrderItemId, orderItemId);

                    final Page<OrderItemAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    orderItemAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<OrderItemAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

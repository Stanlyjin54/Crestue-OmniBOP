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
import com.kuafu.web.entity.OrderInfoAll;
import com.kuafu.web.service.IOrderInfoAllService;
import com.kuafu.web.vo.OrderInfoAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.ICustomerInfoService;
import com.kuafu.web.entity.CustomerInfo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IOrderStatusEnumService;
import com.kuafu.web.entity.OrderStatusEnum;
import com.kuafu.web.service.IGeneralOrdersService;
import com.kuafu.web.entity.GeneralOrders;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 订单信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("OrderInfoAllControllerService")
@RequiredArgsConstructor
public class OrderInfoAllControllerService implements IControllerService<OrderInfoAll> {

    private final IOrderInfoAllService orderInfoAllService;





    public BaseResponse page( OrderInfoAllPageVO pageVO){
        IPage<OrderInfoAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<OrderInfoAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("oi.order_info_id");
        LambdaQueryWrapper<OrderInfoAll> queryWrapper = objectQueryWrapper.lambda();


            if(pageVO.getDiscountAmount() != null){
                queryWrapper.eq(OrderInfoAll::getDiscountAmount, pageVO.getDiscountAmount());
            }
            if(pageVO.getPaymentTime() != null){
                queryWrapper.eq(OrderInfoAll::getPaymentTime, pageVO.getPaymentTime());
            }
        if(StringUtils.isNotEmpty(pageVO.getRefundNo())) {
                queryWrapper.like(OrderInfoAll::getRefundNo, pageVO.getRefundNo());
            }
            if(pageVO.getActualAmount() != null){
                queryWrapper.eq(OrderInfoAll::getActualAmount, pageVO.getActualAmount());
            }
        if(StringUtils.isNotEmpty(pageVO.getSource())) {
                queryWrapper.like(OrderInfoAll::getSource, pageVO.getSource());
            }
        if(StringUtils.isNotEmpty(pageVO.getTableName())) {

                        String s_string = pageVO.getTableName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(OrderInfoAll::getTableName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getOrderStatusEnumOrderStatusEnumId1() != null){
                queryWrapper.eq(OrderInfoAll::getOrderStatusEnumOrderStatusEnumId1, pageVO.getOrderStatusEnumOrderStatusEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getOrderStatus())) {
                queryWrapper.like(OrderInfoAll::getOrderStatus, pageVO.getOrderStatus());
            }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(OrderInfoAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getCompleteTime() != null){
                queryWrapper.eq(OrderInfoAll::getCompleteTime, pageVO.getCompleteTime());
            }
        if(StringUtils.isNotEmpty(pageVO.getShippingMethod())) {
                queryWrapper.like(OrderInfoAll::getShippingMethod, pageVO.getShippingMethod());
            }
        if(StringUtils.isNotEmpty(pageVO.getTel())) {
                queryWrapper.like(OrderInfoAll::getTel, pageVO.getTel());
            }
        if(StringUtils.isNotEmpty(pageVO.getOrderType())) {
                queryWrapper.like(OrderInfoAll::getOrderType, pageVO.getOrderType());
            }
            if(pageVO.getCancelTime() != null){
                queryWrapper.eq(OrderInfoAll::getCancelTime, pageVO.getCancelTime());
            }
        if(StringUtils.isNotEmpty(pageVO.getRefundType())) {
                queryWrapper.like(OrderInfoAll::getRefundType, pageVO.getRefundType());
            }
        if(StringUtils.isNotEmpty(pageVO.getRefundStatus())) {
                queryWrapper.like(OrderInfoAll::getRefundStatus, pageVO.getRefundStatus());
            }
        if(StringUtils.isNotEmpty(pageVO.getShippingCompany())) {
                queryWrapper.like(OrderInfoAll::getShippingCompany, pageVO.getShippingCompany());
            }
            if(pageVO.getCreateTime() != null){
                queryWrapper.eq(OrderInfoAll::getCreateTime, pageVO.getCreateTime());
            }
            if(pageVO.getPaymentAmount() != null){
                queryWrapper.eq(OrderInfoAll::getPaymentAmount, pageVO.getPaymentAmount());
            }
            if(pageVO.getProfitAmount() != null){
                queryWrapper.eq(OrderInfoAll::getProfitAmount, pageVO.getProfitAmount());
            }
        if(StringUtils.isNotEmpty(pageVO.getRefundId())) {
                queryWrapper.like(OrderInfoAll::getRefundId, pageVO.getRefundId());
            }
            if(pageVO.getQuality() != null){
                queryWrapper.eq(OrderInfoAll::getQuality, pageVO.getQuality());
            }
            if(pageVO.getShippingFee() != null){
                queryWrapper.eq(OrderInfoAll::getShippingFee, pageVO.getShippingFee());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(OrderInfoAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
            if(pageVO.getTotalAmount() != null){
                queryWrapper.eq(OrderInfoAll::getTotalAmount, pageVO.getTotalAmount());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhone())) {
                queryWrapper.like(OrderInfoAll::getPhone, pageVO.getPhone());
            }
            if(pageVO.getUserId() != null){
                queryWrapper.eq(OrderInfoAll::getUserId, pageVO.getUserId());
            }
        if(StringUtils.isNotEmpty(pageVO.getPaymentChannel())) {
                queryWrapper.like(OrderInfoAll::getPaymentChannel, pageVO.getPaymentChannel());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(OrderInfoAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
        if(StringUtils.isNotEmpty(pageVO.getOrderNo())) {
                queryWrapper.like(OrderInfoAll::getOrderNo, pageVO.getOrderNo());
            }
        if(StringUtils.isNotEmpty(pageVO.getRemark())) {
                queryWrapper.like(OrderInfoAll::getRemark, pageVO.getRemark());
            }
        if(StringUtils.isNotEmpty(pageVO.getPayStatus())) {
                queryWrapper.like(OrderInfoAll::getPayStatus, pageVO.getPayStatus());
            }
            if(pageVO.getUpdateTime() != null){
                queryWrapper.eq(OrderInfoAll::getUpdateTime, pageVO.getUpdateTime());
            }
            if(pageVO.getIsDeleted() != null){
                queryWrapper.eq(OrderInfoAll::getIsDeleted, pageVO.getIsDeleted());
            }
            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(OrderInfoAll::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }
            if(pageVO.getRfmLevelEnumRfmLevelEnumId1() != null){
                queryWrapper.eq(OrderInfoAll::getRfmLevelEnumRfmLevelEnumId1, pageVO.getRfmLevelEnumRfmLevelEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getTrackingNumber())) {
                queryWrapper.like(OrderInfoAll::getTrackingNumber, pageVO.getTrackingNumber());
            }
            if(pageVO.getRefundAmount() != null){
                queryWrapper.eq(OrderInfoAll::getRefundAmount, pageVO.getRefundAmount());
            }
            if(pageVO.getCommission() != null){
                queryWrapper.eq(OrderInfoAll::getCommission, pageVO.getCommission());
            }
        if(StringUtils.isNotEmpty(pageVO.getRefundReason())) {
                queryWrapper.like(OrderInfoAll::getRefundReason, pageVO.getRefundReason());
            }
        if(StringUtils.isNotEmpty(pageVO.getShippingAddress())) {
                queryWrapper.like(OrderInfoAll::getShippingAddress, pageVO.getShippingAddress());
            }
        if(StringUtils.isNotEmpty(pageVO.getEmail())) {
                queryWrapper.like(OrderInfoAll::getEmail, pageVO.getEmail());
            }
        if(StringUtils.isNotEmpty(pageVO.getWechatId())) {
                queryWrapper.like(OrderInfoAll::getWechatId, pageVO.getWechatId());
            }
            if(pageVO.getDeliverTime() != null){
                queryWrapper.eq(OrderInfoAll::getDeliverTime, pageVO.getDeliverTime());
            }
            if(pageVO.getOrderInfoId() != null){
                queryWrapper.eq(OrderInfoAll::getOrderInfoId, pageVO.getOrderInfoId());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(OrderInfoAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getAddress())) {
                queryWrapper.like(OrderInfoAll::getAddress, pageVO.getAddress());
            }
        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {

                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(OrderInfoAll::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getOpId())) {
                queryWrapper.like(OrderInfoAll::getOpId, pageVO.getOpId());
            }
        if(StringUtils.isNotEmpty(pageVO.getPaymentOrderId())) {
                queryWrapper.like(OrderInfoAll::getPaymentOrderId, pageVO.getPaymentOrderId());
            }
        if(StringUtils.isNotEmpty(pageVO.getFieldName())) {

                        String s_string = pageVO.getFieldName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(OrderInfoAll::getFieldName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getOrderDate() != null){
                queryWrapper.eq(OrderInfoAll::getOrderDate, pageVO.getOrderDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getGeneralOrders())) {
                queryWrapper.like(OrderInfoAll::getGeneralOrders, pageVO.getGeneralOrders());
            }
            if(pageVO.getProductsId() != null){
                queryWrapper.eq(OrderInfoAll::getProductsId, pageVO.getProductsId());
            }
        if(StringUtils.isNotEmpty(pageVO.getWechatDataEncrypted())) {
                queryWrapper.like(OrderInfoAll::getWechatDataEncrypted, pageVO.getWechatDataEncrypted());
            }
        if(StringUtils.isNotEmpty(pageVO.getRecipient())) {
                queryWrapper.like(OrderInfoAll::getRecipient, pageVO.getRecipient());
            }
            if(pageVO.getCustomerTagEnumCustomerTagEnumId1() != null){
                queryWrapper.eq(OrderInfoAll::getCustomerTagEnumCustomerTagEnumId1, pageVO.getCustomerTagEnumCustomerTagEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getCustomerName())) {

                        String s_string = pageVO.getCustomerName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(OrderInfoAll::getCustomerName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getDeliveryStatus())) {
                queryWrapper.like(OrderInfoAll::getDeliveryStatus, pageVO.getDeliveryStatus());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(OrderInfoAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    orderInfoAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(OrderInfoAllPageVO vo){
            LambdaQueryWrapper<OrderInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getDiscountAmount() != null){
                    queryWrapper.eq(OrderInfoAll::getDiscountAmount, vo.getDiscountAmount());
                }
                if(vo.getPaymentTime() != null){
                    queryWrapper.eq(OrderInfoAll::getPaymentTime, vo.getPaymentTime());
                }
            if(StringUtils.isNotEmpty(vo.getRefundNo())) {
                    queryWrapper.eq(OrderInfoAll::getRefundNo, vo.getRefundNo());
                }
                if(vo.getActualAmount() != null){
                    queryWrapper.eq(OrderInfoAll::getActualAmount, vo.getActualAmount());
                }
            if(StringUtils.isNotEmpty(vo.getSource())) {
                    queryWrapper.eq(OrderInfoAll::getSource, vo.getSource());
                }
            if(StringUtils.isNotEmpty(vo.getTableName())) {
                            queryWrapper.like(OrderInfoAll::getTableName, vo.getTableName());
                }
                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
                    queryWrapper.eq(OrderInfoAll::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getOrderStatus())) {
                    queryWrapper.eq(OrderInfoAll::getOrderStatus, vo.getOrderStatus());
                }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(OrderInfoAll::getPassword, vo.getPassword());
                }
                if(vo.getCompleteTime() != null){
                    queryWrapper.eq(OrderInfoAll::getCompleteTime, vo.getCompleteTime());
                }
            if(StringUtils.isNotEmpty(vo.getShippingMethod())) {
                    queryWrapper.eq(OrderInfoAll::getShippingMethod, vo.getShippingMethod());
                }
            if(StringUtils.isNotEmpty(vo.getTel())) {
                    queryWrapper.eq(OrderInfoAll::getTel, vo.getTel());
                }
            if(StringUtils.isNotEmpty(vo.getOrderType())) {
                    queryWrapper.eq(OrderInfoAll::getOrderType, vo.getOrderType());
                }
                if(vo.getCancelTime() != null){
                    queryWrapper.eq(OrderInfoAll::getCancelTime, vo.getCancelTime());
                }
            if(StringUtils.isNotEmpty(vo.getRefundType())) {
                    queryWrapper.eq(OrderInfoAll::getRefundType, vo.getRefundType());
                }
            if(StringUtils.isNotEmpty(vo.getRefundStatus())) {
                    queryWrapper.eq(OrderInfoAll::getRefundStatus, vo.getRefundStatus());
                }
            if(StringUtils.isNotEmpty(vo.getShippingCompany())) {
                    queryWrapper.eq(OrderInfoAll::getShippingCompany, vo.getShippingCompany());
                }
                if(vo.getCreateTime() != null){
                    queryWrapper.eq(OrderInfoAll::getCreateTime, vo.getCreateTime());
                }
                if(vo.getPaymentAmount() != null){
                    queryWrapper.eq(OrderInfoAll::getPaymentAmount, vo.getPaymentAmount());
                }
                if(vo.getProfitAmount() != null){
                    queryWrapper.eq(OrderInfoAll::getProfitAmount, vo.getProfitAmount());
                }
            if(StringUtils.isNotEmpty(vo.getRefundId())) {
                    queryWrapper.eq(OrderInfoAll::getRefundId, vo.getRefundId());
                }
                if(vo.getQuality() != null){
                    queryWrapper.eq(OrderInfoAll::getQuality, vo.getQuality());
                }
                if(vo.getShippingFee() != null){
                    queryWrapper.eq(OrderInfoAll::getShippingFee, vo.getShippingFee());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(OrderInfoAll::getRegistrationDate, vo.getRegistrationDate());
                }
                if(vo.getTotalAmount() != null){
                    queryWrapper.eq(OrderInfoAll::getTotalAmount, vo.getTotalAmount());
                }
            if(StringUtils.isNotEmpty(vo.getPhone())) {
                    queryWrapper.eq(OrderInfoAll::getPhone, vo.getPhone());
                }
                if(vo.getUserId() != null){
                    queryWrapper.eq(OrderInfoAll::getUserId, vo.getUserId());
                }
            if(StringUtils.isNotEmpty(vo.getPaymentChannel())) {
                    queryWrapper.eq(OrderInfoAll::getPaymentChannel, vo.getPaymentChannel());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(OrderInfoAll::getPhoneNumber, vo.getPhoneNumber());
                }
            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                    queryWrapper.eq(OrderInfoAll::getOrderNo, vo.getOrderNo());
                }
            if(StringUtils.isNotEmpty(vo.getRemark())) {
                    queryWrapper.eq(OrderInfoAll::getRemark, vo.getRemark());
                }
            if(StringUtils.isNotEmpty(vo.getPayStatus())) {
                    queryWrapper.eq(OrderInfoAll::getPayStatus, vo.getPayStatus());
                }
                if(vo.getUpdateTime() != null){
                    queryWrapper.eq(OrderInfoAll::getUpdateTime, vo.getUpdateTime());
                }
                if(vo.getIsDeleted() != null){
                    queryWrapper.eq(OrderInfoAll::getIsDeleted, vo.getIsDeleted());
                }
                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(OrderInfoAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                    queryWrapper.eq(OrderInfoAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getTrackingNumber())) {
                    queryWrapper.eq(OrderInfoAll::getTrackingNumber, vo.getTrackingNumber());
                }
                if(vo.getRefundAmount() != null){
                    queryWrapper.eq(OrderInfoAll::getRefundAmount, vo.getRefundAmount());
                }
                if(vo.getCommission() != null){
                    queryWrapper.eq(OrderInfoAll::getCommission, vo.getCommission());
                }
            if(StringUtils.isNotEmpty(vo.getRefundReason())) {
                    queryWrapper.eq(OrderInfoAll::getRefundReason, vo.getRefundReason());
                }
            if(StringUtils.isNotEmpty(vo.getShippingAddress())) {
                    queryWrapper.eq(OrderInfoAll::getShippingAddress, vo.getShippingAddress());
                }
            if(StringUtils.isNotEmpty(vo.getEmail())) {
                    queryWrapper.eq(OrderInfoAll::getEmail, vo.getEmail());
                }
            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                    queryWrapper.eq(OrderInfoAll::getWechatId, vo.getWechatId());
                }
                if(vo.getDeliverTime() != null){
                    queryWrapper.eq(OrderInfoAll::getDeliverTime, vo.getDeliverTime());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(OrderInfoAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getAddress())) {
                    queryWrapper.eq(OrderInfoAll::getAddress, vo.getAddress());
                }
            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(OrderInfoAll::getStatusName, vo.getStatusName());
                }
            if(StringUtils.isNotEmpty(vo.getOpId())) {
                    queryWrapper.eq(OrderInfoAll::getOpId, vo.getOpId());
                }
            if(StringUtils.isNotEmpty(vo.getPaymentOrderId())) {
                    queryWrapper.eq(OrderInfoAll::getPaymentOrderId, vo.getPaymentOrderId());
                }
            if(StringUtils.isNotEmpty(vo.getFieldName())) {
                            queryWrapper.like(OrderInfoAll::getFieldName, vo.getFieldName());
                }
                if(vo.getOrderDate() != null){
                    queryWrapper.eq(OrderInfoAll::getOrderDate, vo.getOrderDate());
                }
            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
                    queryWrapper.eq(OrderInfoAll::getGeneralOrders, vo.getGeneralOrders());
                }
                if(vo.getProductsId() != null){
                    queryWrapper.eq(OrderInfoAll::getProductsId, vo.getProductsId());
                }
            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                    queryWrapper.eq(OrderInfoAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                }
            if(StringUtils.isNotEmpty(vo.getRecipient())) {
                    queryWrapper.eq(OrderInfoAll::getRecipient, vo.getRecipient());
                }
                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                    queryWrapper.eq(OrderInfoAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                            queryWrapper.like(OrderInfoAll::getCustomerName, vo.getCustomerName());
                }
            if(StringUtils.isNotEmpty(vo.getDeliveryStatus())) {
                    queryWrapper.eq(OrderInfoAll::getDeliveryStatus, vo.getDeliveryStatus());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(OrderInfoAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(orderInfoAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(OrderInfoAllPageVO vo) {


                LambdaQueryWrapper<OrderInfoAll> queryWrapper = new LambdaQueryWrapper<>();


                                if(vo.getDiscountAmount() != null){
                                    queryWrapper.eq(OrderInfoAll::getDiscountAmount, vo.getDiscountAmount());
                                }


                                if(vo.getPaymentTime() != null){
                                    queryWrapper.eq(OrderInfoAll::getPaymentTime, vo.getPaymentTime());
                                }


                            if(StringUtils.isNotEmpty(vo.getRefundNo())) {
                                    queryWrapper.eq(OrderInfoAll::getRefundNo, vo.getRefundNo());
                                }


                                if(vo.getActualAmount() != null){
                                    queryWrapper.eq(OrderInfoAll::getActualAmount, vo.getActualAmount());
                                }


                            if(StringUtils.isNotEmpty(vo.getSource())) {
                                    queryWrapper.eq(OrderInfoAll::getSource, vo.getSource());
                                }


                            if(StringUtils.isNotEmpty(vo.getTableName())) {
                                            queryWrapper.like(OrderInfoAll::getTableName, vo.getTableName());
                                }


                                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
                                    queryWrapper.eq(OrderInfoAll::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getOrderStatus())) {
                                    queryWrapper.eq(OrderInfoAll::getOrderStatus, vo.getOrderStatus());
                                }


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(OrderInfoAll::getPassword, vo.getPassword());
                                }


                                if(vo.getCompleteTime() != null){
                                    queryWrapper.eq(OrderInfoAll::getCompleteTime, vo.getCompleteTime());
                                }


                            if(StringUtils.isNotEmpty(vo.getShippingMethod())) {
                                    queryWrapper.eq(OrderInfoAll::getShippingMethod, vo.getShippingMethod());
                                }


                            if(StringUtils.isNotEmpty(vo.getTel())) {
                                    queryWrapper.eq(OrderInfoAll::getTel, vo.getTel());
                                }


                            if(StringUtils.isNotEmpty(vo.getOrderType())) {
                                    queryWrapper.eq(OrderInfoAll::getOrderType, vo.getOrderType());
                                }


                                if(vo.getCancelTime() != null){
                                    queryWrapper.eq(OrderInfoAll::getCancelTime, vo.getCancelTime());
                                }


                            if(StringUtils.isNotEmpty(vo.getRefundType())) {
                                    queryWrapper.eq(OrderInfoAll::getRefundType, vo.getRefundType());
                                }


                            if(StringUtils.isNotEmpty(vo.getRefundStatus())) {
                                    queryWrapper.eq(OrderInfoAll::getRefundStatus, vo.getRefundStatus());
                                }


                            if(StringUtils.isNotEmpty(vo.getShippingCompany())) {
                                    queryWrapper.eq(OrderInfoAll::getShippingCompany, vo.getShippingCompany());
                                }


                                if(vo.getCreateTime() != null){
                                    queryWrapper.eq(OrderInfoAll::getCreateTime, vo.getCreateTime());
                                }


                                if(vo.getPaymentAmount() != null){
                                    queryWrapper.eq(OrderInfoAll::getPaymentAmount, vo.getPaymentAmount());
                                }


                                if(vo.getProfitAmount() != null){
                                    queryWrapper.eq(OrderInfoAll::getProfitAmount, vo.getProfitAmount());
                                }


                            if(StringUtils.isNotEmpty(vo.getRefundId())) {
                                    queryWrapper.eq(OrderInfoAll::getRefundId, vo.getRefundId());
                                }


                                if(vo.getQuality() != null){
                                    queryWrapper.eq(OrderInfoAll::getQuality, vo.getQuality());
                                }


                                if(vo.getShippingFee() != null){
                                    queryWrapper.eq(OrderInfoAll::getShippingFee, vo.getShippingFee());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(OrderInfoAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                                if(vo.getTotalAmount() != null){
                                    queryWrapper.eq(OrderInfoAll::getTotalAmount, vo.getTotalAmount());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhone())) {
                                    queryWrapper.eq(OrderInfoAll::getPhone, vo.getPhone());
                                }


                                if(vo.getUserId() != null){
                                    queryWrapper.eq(OrderInfoAll::getUserId, vo.getUserId());
                                }


                            if(StringUtils.isNotEmpty(vo.getPaymentChannel())) {
                                    queryWrapper.eq(OrderInfoAll::getPaymentChannel, vo.getPaymentChannel());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(OrderInfoAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                                    queryWrapper.eq(OrderInfoAll::getOrderNo, vo.getOrderNo());
                                }


                            if(StringUtils.isNotEmpty(vo.getRemark())) {
                                    queryWrapper.eq(OrderInfoAll::getRemark, vo.getRemark());
                                }


                            if(StringUtils.isNotEmpty(vo.getPayStatus())) {
                                    queryWrapper.eq(OrderInfoAll::getPayStatus, vo.getPayStatus());
                                }


                                if(vo.getUpdateTime() != null){
                                    queryWrapper.eq(OrderInfoAll::getUpdateTime, vo.getUpdateTime());
                                }


                                if(vo.getIsDeleted() != null){
                                    queryWrapper.eq(OrderInfoAll::getIsDeleted, vo.getIsDeleted());
                                }


                                if(vo.getCustomerInfoCustomerInfoId1() != null){
                                    queryWrapper.eq(OrderInfoAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                                }


                                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                                    queryWrapper.eq(OrderInfoAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getTrackingNumber())) {
                                    queryWrapper.eq(OrderInfoAll::getTrackingNumber, vo.getTrackingNumber());
                                }


                                if(vo.getRefundAmount() != null){
                                    queryWrapper.eq(OrderInfoAll::getRefundAmount, vo.getRefundAmount());
                                }


                                if(vo.getCommission() != null){
                                    queryWrapper.eq(OrderInfoAll::getCommission, vo.getCommission());
                                }


                            if(StringUtils.isNotEmpty(vo.getRefundReason())) {
                                    queryWrapper.eq(OrderInfoAll::getRefundReason, vo.getRefundReason());
                                }


                            if(StringUtils.isNotEmpty(vo.getShippingAddress())) {
                                    queryWrapper.eq(OrderInfoAll::getShippingAddress, vo.getShippingAddress());
                                }


                            if(StringUtils.isNotEmpty(vo.getEmail())) {
                                    queryWrapper.eq(OrderInfoAll::getEmail, vo.getEmail());
                                }


                            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                                    queryWrapper.eq(OrderInfoAll::getWechatId, vo.getWechatId());
                                }


                                if(vo.getDeliverTime() != null){
                                    queryWrapper.eq(OrderInfoAll::getDeliverTime, vo.getDeliverTime());
                                }


                                if(vo.getOrderInfoId() != null){
                                    queryWrapper.eq(OrderInfoAll::getOrderInfoId, vo.getOrderInfoId());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(OrderInfoAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getAddress())) {
                                    queryWrapper.eq(OrderInfoAll::getAddress, vo.getAddress());
                                }


                            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                                            queryWrapper.like(OrderInfoAll::getStatusName, vo.getStatusName());
                                }


                            if(StringUtils.isNotEmpty(vo.getOpId())) {
                                    queryWrapper.eq(OrderInfoAll::getOpId, vo.getOpId());
                                }


                            if(StringUtils.isNotEmpty(vo.getPaymentOrderId())) {
                                    queryWrapper.eq(OrderInfoAll::getPaymentOrderId, vo.getPaymentOrderId());
                                }


                            if(StringUtils.isNotEmpty(vo.getFieldName())) {
                                            queryWrapper.like(OrderInfoAll::getFieldName, vo.getFieldName());
                                }


                                if(vo.getOrderDate() != null){
                                    queryWrapper.eq(OrderInfoAll::getOrderDate, vo.getOrderDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
                                    queryWrapper.eq(OrderInfoAll::getGeneralOrders, vo.getGeneralOrders());
                                }


                                if(vo.getProductsId() != null){
                                    queryWrapper.eq(OrderInfoAll::getProductsId, vo.getProductsId());
                                }


                            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                                    queryWrapper.eq(OrderInfoAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                                }


                            if(StringUtils.isNotEmpty(vo.getRecipient())) {
                                    queryWrapper.eq(OrderInfoAll::getRecipient, vo.getRecipient());
                                }


                                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                                    queryWrapper.eq(OrderInfoAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                                            queryWrapper.like(OrderInfoAll::getCustomerName, vo.getCustomerName());
                                }


                            if(StringUtils.isNotEmpty(vo.getDeliveryStatus())) {
                                    queryWrapper.eq(OrderInfoAll::getDeliveryStatus, vo.getDeliveryStatus());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(OrderInfoAll::getUsername, vo.getUsername());
                                }

                // OrderInfoAll entity = this.orderInfoAllService.getById(orderInfoId);
                    final Page<OrderInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    orderInfoAllService.pageNew(page, vo, queryWrapper);
                    final List<OrderInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public OrderInfoAll getById(Serializable orderInfoId) {

                    LambdaQueryWrapper<OrderInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(OrderInfoAll::getOrderInfoId, orderInfoId);

                    final Page<OrderInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    orderInfoAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<OrderInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

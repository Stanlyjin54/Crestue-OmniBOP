package com.kuafu.web.controller;

import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.resource.service.IStaticResourceService;
import com.kuafu.common.event.EventVo;
import com.kuafu.common.event.MyEventService;
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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.support.TransactionTemplate;
import com.kuafu.common.sensitive.SensitiveFilter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.OrderInfo;
import com.kuafu.web.service.IOrderInfoService;
import com.kuafu.web.vo.OrderInfoPageVO;
import com.kuafu.web.vo.OrderInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 订单信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("OrderInfoControllerService")
@RequiredArgsConstructor
public class OrderInfoControllerService  {

    private final IOrderInfoService orderInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( OrderInfoPageVO pageVO){
        IPage<OrderInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<OrderInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<OrderInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(OrderInfo::getOrderInfoId);

            if(pageVO.getOrderInfoId() != null){
                queryWrapper.eq(OrderInfo::getOrderInfoId, pageVO.getOrderInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getOrderNo())) {
                queryWrapper.eq(OrderInfo::getOrderNo, pageVO.getOrderNo());
            }


            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(OrderInfo::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }


            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(OrderInfo::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }


            if(pageVO.getOrderStatusEnumOrderStatusEnumId1() != null){
                queryWrapper.eq(OrderInfo::getOrderStatusEnumOrderStatusEnumId1, pageVO.getOrderStatusEnumOrderStatusEnumId1());
            }


            if(pageVO.getTotalAmount() != null){
                queryWrapper.eq(OrderInfo::getTotalAmount, pageVO.getTotalAmount());
            }


            if(pageVO.getProfitAmount() != null){
                queryWrapper.eq(OrderInfo::getProfitAmount, pageVO.getProfitAmount());
            }


            if(pageVO.getCommission() != null){
                queryWrapper.eq(OrderInfo::getCommission, pageVO.getCommission());
            }


            if(pageVO.getShippingFee() != null){
                queryWrapper.eq(OrderInfo::getShippingFee, pageVO.getShippingFee());
            }


            if(pageVO.getOrderDate() != null){
                queryWrapper.eq(OrderInfo::getOrderDate, pageVO.getOrderDate());
            }


        if(StringUtils.isNotEmpty(pageVO.getGeneralOrders())) {
                queryWrapper.eq(OrderInfo::getGeneralOrders, pageVO.getGeneralOrders());
            }

            return ResultUtils.success(orderInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  OrderInfoVO vo){
            LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                    queryWrapper.eq(OrderInfo::getOrderNo, vo.getOrderNo());
                }
                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(OrderInfo::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(OrderInfo::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
                    queryWrapper.eq(OrderInfo::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
                }
                if(vo.getTotalAmount() != null){
                    queryWrapper.eq(OrderInfo::getTotalAmount, vo.getTotalAmount());
                }
                if(vo.getProfitAmount() != null){
                    queryWrapper.eq(OrderInfo::getProfitAmount, vo.getProfitAmount());
                }
                if(vo.getCommission() != null){
                    queryWrapper.eq(OrderInfo::getCommission, vo.getCommission());
                }
                if(vo.getShippingFee() != null){
                    queryWrapper.eq(OrderInfo::getShippingFee, vo.getShippingFee());
                }
                if(vo.getOrderDate() != null){
                    queryWrapper.eq(OrderInfo::getOrderDate, vo.getOrderDate());
                }
            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
                    queryWrapper.eq(OrderInfo::getGeneralOrders, vo.getGeneralOrders());
                }
                return ResultUtils.success(orderInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  OrderInfoVO vo) {
                OrderInfo entity =  OrderInfo.builder()
                    .orderNo(vo.getOrderNo())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .orderStatusEnumOrderStatusEnumId1(vo.getOrderStatusEnumOrderStatusEnumId1())
                    .totalAmount(vo.getTotalAmount())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .orderDate(vo.getOrderDate())
                    .generalOrders(vo.getGeneralOrders())
                .build();








                boolean flag =this.orderInfoService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("order_info").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getOrderInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( OrderInfoVO vo) {
                OrderInfo entity =  OrderInfo.builder()
                    .orderInfoId(vo.getOrderInfoId())
                    .orderNo(vo.getOrderNo())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .orderStatusEnumOrderStatusEnumId1(vo.getOrderStatusEnumOrderStatusEnumId1())
                    .totalAmount(vo.getTotalAmount())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .orderDate(vo.getOrderDate())
                    .generalOrders(vo.getGeneralOrders())
                .build();



                        boolean flag = this.orderInfoService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getOrderInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( OrderInfoVO vo ) {
                OrderInfo entity = this.orderInfoService.getById(vo.getOrderInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( OrderInfoVO vo) {
                boolean flag = this.orderInfoService.removeById(vo.getOrderInfoId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

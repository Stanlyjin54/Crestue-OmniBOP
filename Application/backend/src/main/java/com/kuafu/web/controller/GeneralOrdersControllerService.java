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
import com.kuafu.web.entity.GeneralOrders;
import com.kuafu.web.service.IGeneralOrdersService;
import com.kuafu.web.vo.GeneralOrdersPageVO;
import com.kuafu.web.vo.GeneralOrdersVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 系统订单信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("GeneralOrdersControllerService")
@RequiredArgsConstructor
public class GeneralOrdersControllerService  {

    private final IGeneralOrdersService generalOrdersService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( GeneralOrdersPageVO pageVO){
        IPage<GeneralOrders> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<GeneralOrders> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<GeneralOrders> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(GeneralOrders::getId);

            if(pageVO.getId() != null){
                queryWrapper.eq(GeneralOrders::getId, pageVO.getId());
            }


        if(StringUtils.isNotEmpty(pageVO.getOrderNo())) {
                queryWrapper.eq(GeneralOrders::getOrderNo, pageVO.getOrderNo());
            }


            if(pageVO.getUserId() != null){
                queryWrapper.eq(GeneralOrders::getUserId, pageVO.getUserId());
            }


        if(StringUtils.isNotEmpty(pageVO.getOpId())) {
                queryWrapper.eq(GeneralOrders::getOpId, pageVO.getOpId());
            }


        if(StringUtils.isNotEmpty(pageVO.getOrderType())) {
                queryWrapper.eq(GeneralOrders::getOrderType, pageVO.getOrderType());
            }


        if(StringUtils.isNotEmpty(pageVO.getOrderStatus())) {
                queryWrapper.eq(GeneralOrders::getOrderStatus, pageVO.getOrderStatus());
            }


        if(StringUtils.isNotEmpty(pageVO.getPayStatus())) {
                queryWrapper.eq(GeneralOrders::getPayStatus, pageVO.getPayStatus());
            }


        if(StringUtils.isNotEmpty(pageVO.getDeliveryStatus())) {
                queryWrapper.eq(GeneralOrders::getDeliveryStatus, pageVO.getDeliveryStatus());
            }


            if(pageVO.getProductsId() != null){
                queryWrapper.eq(GeneralOrders::getProductsId, pageVO.getProductsId());
            }


            if(pageVO.getQuality() != null){
                queryWrapper.eq(GeneralOrders::getQuality, pageVO.getQuality());
            }


            if(pageVO.getTotalAmount() != null){
                queryWrapper.eq(GeneralOrders::getTotalAmount, pageVO.getTotalAmount());
            }


            if(pageVO.getDiscountAmount() != null){
                queryWrapper.eq(GeneralOrders::getDiscountAmount, pageVO.getDiscountAmount());
            }


            if(pageVO.getShippingFee() != null){
                queryWrapper.eq(GeneralOrders::getShippingFee, pageVO.getShippingFee());
            }


            if(pageVO.getActualAmount() != null){
                queryWrapper.eq(GeneralOrders::getActualAmount, pageVO.getActualAmount());
            }


        if(StringUtils.isNotEmpty(pageVO.getPaymentChannel())) {
                queryWrapper.eq(GeneralOrders::getPaymentChannel, pageVO.getPaymentChannel());
            }


        if(StringUtils.isNotEmpty(pageVO.getPaymentOrderId())) {
                queryWrapper.eq(GeneralOrders::getPaymentOrderId, pageVO.getPaymentOrderId());
            }


            if(pageVO.getPaymentTime() != null){
                queryWrapper.eq(GeneralOrders::getPaymentTime, pageVO.getPaymentTime());
            }


            if(pageVO.getPaymentAmount() != null){
                queryWrapper.eq(GeneralOrders::getPaymentAmount, pageVO.getPaymentAmount());
            }


        if(StringUtils.isNotEmpty(pageVO.getShippingMethod())) {
                queryWrapper.eq(GeneralOrders::getShippingMethod, pageVO.getShippingMethod());
            }


        if(StringUtils.isNotEmpty(pageVO.getShippingAddress())) {
                queryWrapper.eq(GeneralOrders::getShippingAddress, pageVO.getShippingAddress());
            }


        if(StringUtils.isNotEmpty(pageVO.getShippingCompany())) {
                queryWrapper.eq(GeneralOrders::getShippingCompany, pageVO.getShippingCompany());
            }


        if(StringUtils.isNotEmpty(pageVO.getTrackingNumber())) {
                queryWrapper.eq(GeneralOrders::getTrackingNumber, pageVO.getTrackingNumber());
            }


        if(StringUtils.isNotEmpty(pageVO.getTel())) {
                queryWrapper.eq(GeneralOrders::getTel, pageVO.getTel());
            }


        if(StringUtils.isNotEmpty(pageVO.getRecipient())) {
                queryWrapper.eq(GeneralOrders::getRecipient, pageVO.getRecipient());
            }


            if(pageVO.getCreateTime() != null){
                queryWrapper.eq(GeneralOrders::getCreateTime, pageVO.getCreateTime());
            }


            if(pageVO.getUpdateTime() != null){
                queryWrapper.eq(GeneralOrders::getUpdateTime, pageVO.getUpdateTime());
            }


            if(pageVO.getCancelTime() != null){
                queryWrapper.eq(GeneralOrders::getCancelTime, pageVO.getCancelTime());
            }


            if(pageVO.getCompleteTime() != null){
                queryWrapper.eq(GeneralOrders::getCompleteTime, pageVO.getCompleteTime());
            }


            if(pageVO.getDeliverTime() != null){
                queryWrapper.eq(GeneralOrders::getDeliverTime, pageVO.getDeliverTime());
            }


        if(StringUtils.isNotEmpty(pageVO.getRefundId())) {
                queryWrapper.eq(GeneralOrders::getRefundId, pageVO.getRefundId());
            }


        if(StringUtils.isNotEmpty(pageVO.getRefundNo())) {
                queryWrapper.eq(GeneralOrders::getRefundNo, pageVO.getRefundNo());
            }


        if(StringUtils.isNotEmpty(pageVO.getRefundType())) {
                queryWrapper.eq(GeneralOrders::getRefundType, pageVO.getRefundType());
            }


            if(pageVO.getRefundAmount() != null){
                queryWrapper.eq(GeneralOrders::getRefundAmount, pageVO.getRefundAmount());
            }


        if(StringUtils.isNotEmpty(pageVO.getRefundReason())) {
                queryWrapper.eq(GeneralOrders::getRefundReason, pageVO.getRefundReason());
            }


        if(StringUtils.isNotEmpty(pageVO.getRefundStatus())) {
                queryWrapper.eq(GeneralOrders::getRefundStatus, pageVO.getRefundStatus());
            }


        if(StringUtils.isNotEmpty(pageVO.getRemark())) {
                queryWrapper.eq(GeneralOrders::getRemark, pageVO.getRemark());
            }


            if(pageVO.getIsDeleted() != null){
                queryWrapper.eq(GeneralOrders::getIsDeleted, pageVO.getIsDeleted());
            }


        if(StringUtils.isNotEmpty(pageVO.getTableName())) {
//                        queryWrapper.like(GeneralOrders::getTableName, pageVO.getTableName());
                        String s_string = pageVO.getTableName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(GeneralOrders::getTableName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getFieldName())) {
//                        queryWrapper.like(GeneralOrders::getFieldName, pageVO.getFieldName());
                        String s_string = pageVO.getFieldName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(GeneralOrders::getFieldName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

            return ResultUtils.success(generalOrdersService.page(page, queryWrapper));
        }


        public BaseResponse list(  GeneralOrdersVO vo){
            LambdaQueryWrapper<GeneralOrders> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                    queryWrapper.eq(GeneralOrders::getOrderNo, vo.getOrderNo());
                }
                if(vo.getUserId() != null){
                    queryWrapper.eq(GeneralOrders::getUserId, vo.getUserId());
                }
            if(StringUtils.isNotEmpty(vo.getOpId())) {
                    queryWrapper.eq(GeneralOrders::getOpId, vo.getOpId());
                }
            if(StringUtils.isNotEmpty(vo.getOrderType())) {
                    queryWrapper.eq(GeneralOrders::getOrderType, vo.getOrderType());
                }
            if(StringUtils.isNotEmpty(vo.getOrderStatus())) {
                    queryWrapper.eq(GeneralOrders::getOrderStatus, vo.getOrderStatus());
                }
            if(StringUtils.isNotEmpty(vo.getPayStatus())) {
                    queryWrapper.eq(GeneralOrders::getPayStatus, vo.getPayStatus());
                }
            if(StringUtils.isNotEmpty(vo.getDeliveryStatus())) {
                    queryWrapper.eq(GeneralOrders::getDeliveryStatus, vo.getDeliveryStatus());
                }
                if(vo.getProductsId() != null){
                    queryWrapper.eq(GeneralOrders::getProductsId, vo.getProductsId());
                }
                if(vo.getQuality() != null){
                    queryWrapper.eq(GeneralOrders::getQuality, vo.getQuality());
                }
                if(vo.getTotalAmount() != null){
                    queryWrapper.eq(GeneralOrders::getTotalAmount, vo.getTotalAmount());
                }
                if(vo.getDiscountAmount() != null){
                    queryWrapper.eq(GeneralOrders::getDiscountAmount, vo.getDiscountAmount());
                }
                if(vo.getShippingFee() != null){
                    queryWrapper.eq(GeneralOrders::getShippingFee, vo.getShippingFee());
                }
                if(vo.getActualAmount() != null){
                    queryWrapper.eq(GeneralOrders::getActualAmount, vo.getActualAmount());
                }
            if(StringUtils.isNotEmpty(vo.getPaymentChannel())) {
                    queryWrapper.eq(GeneralOrders::getPaymentChannel, vo.getPaymentChannel());
                }
            if(StringUtils.isNotEmpty(vo.getPaymentOrderId())) {
                    queryWrapper.eq(GeneralOrders::getPaymentOrderId, vo.getPaymentOrderId());
                }
                if(vo.getPaymentTime() != null){
                    queryWrapper.eq(GeneralOrders::getPaymentTime, vo.getPaymentTime());
                }
                if(vo.getPaymentAmount() != null){
                    queryWrapper.eq(GeneralOrders::getPaymentAmount, vo.getPaymentAmount());
                }
            if(StringUtils.isNotEmpty(vo.getShippingMethod())) {
                    queryWrapper.eq(GeneralOrders::getShippingMethod, vo.getShippingMethod());
                }
            if(StringUtils.isNotEmpty(vo.getShippingAddress())) {
                    queryWrapper.eq(GeneralOrders::getShippingAddress, vo.getShippingAddress());
                }
            if(StringUtils.isNotEmpty(vo.getShippingCompany())) {
                    queryWrapper.eq(GeneralOrders::getShippingCompany, vo.getShippingCompany());
                }
            if(StringUtils.isNotEmpty(vo.getTrackingNumber())) {
                    queryWrapper.eq(GeneralOrders::getTrackingNumber, vo.getTrackingNumber());
                }
            if(StringUtils.isNotEmpty(vo.getTel())) {
                    queryWrapper.eq(GeneralOrders::getTel, vo.getTel());
                }
            if(StringUtils.isNotEmpty(vo.getRecipient())) {
                    queryWrapper.eq(GeneralOrders::getRecipient, vo.getRecipient());
                }
                if(vo.getCreateTime() != null){
                    queryWrapper.eq(GeneralOrders::getCreateTime, vo.getCreateTime());
                }
                if(vo.getUpdateTime() != null){
                    queryWrapper.eq(GeneralOrders::getUpdateTime, vo.getUpdateTime());
                }
                if(vo.getCancelTime() != null){
                    queryWrapper.eq(GeneralOrders::getCancelTime, vo.getCancelTime());
                }
                if(vo.getCompleteTime() != null){
                    queryWrapper.eq(GeneralOrders::getCompleteTime, vo.getCompleteTime());
                }
                if(vo.getDeliverTime() != null){
                    queryWrapper.eq(GeneralOrders::getDeliverTime, vo.getDeliverTime());
                }
            if(StringUtils.isNotEmpty(vo.getRefundId())) {
                    queryWrapper.eq(GeneralOrders::getRefundId, vo.getRefundId());
                }
            if(StringUtils.isNotEmpty(vo.getRefundNo())) {
                    queryWrapper.eq(GeneralOrders::getRefundNo, vo.getRefundNo());
                }
            if(StringUtils.isNotEmpty(vo.getRefundType())) {
                    queryWrapper.eq(GeneralOrders::getRefundType, vo.getRefundType());
                }
                if(vo.getRefundAmount() != null){
                    queryWrapper.eq(GeneralOrders::getRefundAmount, vo.getRefundAmount());
                }
            if(StringUtils.isNotEmpty(vo.getRefundReason())) {
                    queryWrapper.eq(GeneralOrders::getRefundReason, vo.getRefundReason());
                }
            if(StringUtils.isNotEmpty(vo.getRefundStatus())) {
                    queryWrapper.eq(GeneralOrders::getRefundStatus, vo.getRefundStatus());
                }
            if(StringUtils.isNotEmpty(vo.getRemark())) {
                    queryWrapper.eq(GeneralOrders::getRemark, vo.getRemark());
                }
                if(vo.getIsDeleted() != null){
                    queryWrapper.eq(GeneralOrders::getIsDeleted, vo.getIsDeleted());
                }
            if(StringUtils.isNotEmpty(vo.getTableName())) {
                            queryWrapper.like(GeneralOrders::getTableName, vo.getTableName());
                }
            if(StringUtils.isNotEmpty(vo.getFieldName())) {
                            queryWrapper.like(GeneralOrders::getFieldName, vo.getFieldName());
                }
                return ResultUtils.success(generalOrdersService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  GeneralOrdersVO vo) {
                GeneralOrders entity =  GeneralOrders.builder()
                    .orderNo(vo.getOrderNo())
                    .userId(vo.getUserId())
                    .opId(vo.getOpId())
                    .orderType(vo.getOrderType())
                    .orderStatus(vo.getOrderStatus())
                    .payStatus(vo.getPayStatus())
                    .deliveryStatus(vo.getDeliveryStatus())
                    .productsId(vo.getProductsId())
                    .quality(vo.getQuality())
                    .totalAmount(vo.getTotalAmount())
                    .discountAmount(vo.getDiscountAmount())
                    .shippingFee(vo.getShippingFee())
                    .actualAmount(vo.getActualAmount())
                    .paymentChannel(vo.getPaymentChannel())
                    .paymentOrderId(vo.getPaymentOrderId())
                    .paymentTime(vo.getPaymentTime())
                    .paymentAmount(vo.getPaymentAmount())
                    .shippingMethod(vo.getShippingMethod())
                    .shippingAddress(vo.getShippingAddress())
                    .shippingCompany(vo.getShippingCompany())
                    .trackingNumber(vo.getTrackingNumber())
                    .tel(vo.getTel())
                    .recipient(vo.getRecipient())
                    .cancelTime(vo.getCancelTime())
                    .completeTime(vo.getCompleteTime())
                    .deliverTime(vo.getDeliverTime())
                    .refundId(vo.getRefundId())
                    .refundNo(vo.getRefundNo())
                    .refundType(vo.getRefundType())
                    .refundAmount(vo.getRefundAmount())
                    .refundReason(vo.getRefundReason())
                    .refundStatus(vo.getRefundStatus())
                    .remark(vo.getRemark())
                    .tableName(vo.getTableName())
                    .fieldName(vo.getFieldName())
                .build();








                boolean flag =this.generalOrdersService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("general_orders").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( GeneralOrdersVO vo) {
                GeneralOrders entity =  GeneralOrders.builder()
                    .id(vo.getId())
                    .orderNo(vo.getOrderNo())
                    .userId(vo.getUserId())
                    .opId(vo.getOpId())
                    .orderType(vo.getOrderType())
                    .orderStatus(vo.getOrderStatus())
                    .payStatus(vo.getPayStatus())
                    .deliveryStatus(vo.getDeliveryStatus())
                    .productsId(vo.getProductsId())
                    .quality(vo.getQuality())
                    .totalAmount(vo.getTotalAmount())
                    .discountAmount(vo.getDiscountAmount())
                    .shippingFee(vo.getShippingFee())
                    .actualAmount(vo.getActualAmount())
                    .paymentChannel(vo.getPaymentChannel())
                    .paymentOrderId(vo.getPaymentOrderId())
                    .paymentTime(vo.getPaymentTime())
                    .paymentAmount(vo.getPaymentAmount())
                    .shippingMethod(vo.getShippingMethod())
                    .shippingAddress(vo.getShippingAddress())
                    .shippingCompany(vo.getShippingCompany())
                    .trackingNumber(vo.getTrackingNumber())
                    .tel(vo.getTel())
                    .recipient(vo.getRecipient())
                    .cancelTime(vo.getCancelTime())
                    .completeTime(vo.getCompleteTime())
                    .deliverTime(vo.getDeliverTime())
                    .refundId(vo.getRefundId())
                    .refundNo(vo.getRefundNo())
                    .refundType(vo.getRefundType())
                    .refundAmount(vo.getRefundAmount())
                    .refundReason(vo.getRefundReason())
                    .refundStatus(vo.getRefundStatus())
                    .remark(vo.getRemark())
                    .tableName(vo.getTableName())
                    .fieldName(vo.getFieldName())
                .build();



                        boolean flag = this.generalOrdersService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( GeneralOrdersVO vo ) {
                GeneralOrders entity = this.generalOrdersService.getById(vo.getId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( GeneralOrdersVO vo) {
                boolean flag = this.generalOrdersService.removeById(vo.getId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

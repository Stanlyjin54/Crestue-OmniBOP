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
import com.kuafu.web.entity.OrderItem;
import com.kuafu.web.service.IOrderItemService;
import com.kuafu.web.vo.OrderItemPageVO;
import com.kuafu.web.vo.OrderItemVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 订单商品明细 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("OrderItemControllerService")
@RequiredArgsConstructor
public class OrderItemControllerService  {

    private final IOrderItemService orderItemService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( OrderItemPageVO pageVO){
        IPage<OrderItem> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<OrderItem> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<OrderItem> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(OrderItem::getOrderItemId);

            if(pageVO.getOrderItemId() != null){
                queryWrapper.eq(OrderItem::getOrderItemId, pageVO.getOrderItemId());
            }


            if(pageVO.getOrderInfoOrderInfoId1() != null){
                queryWrapper.eq(OrderItem::getOrderInfoOrderInfoId1, pageVO.getOrderInfoOrderInfoId1());
            }


            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(OrderItem::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }


            if(pageVO.getQuantity() != null){
                queryWrapper.eq(OrderItem::getQuantity, pageVO.getQuantity());
            }


            if(pageVO.getUnitPrice() != null){
                queryWrapper.eq(OrderItem::getUnitPrice, pageVO.getUnitPrice());
            }


            if(pageVO.getTotalPrice() != null){
                queryWrapper.eq(OrderItem::getTotalPrice, pageVO.getTotalPrice());
            }

            return ResultUtils.success(orderItemService.page(page, queryWrapper));
        }


        public BaseResponse list(  OrderItemVO vo){
            LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getOrderInfoOrderInfoId1() != null){
                    queryWrapper.eq(OrderItem::getOrderInfoOrderInfoId1, vo.getOrderInfoOrderInfoId1());
                }
                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(OrderItem::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
                if(vo.getQuantity() != null){
                    queryWrapper.eq(OrderItem::getQuantity, vo.getQuantity());
                }
                if(vo.getUnitPrice() != null){
                    queryWrapper.eq(OrderItem::getUnitPrice, vo.getUnitPrice());
                }
                if(vo.getTotalPrice() != null){
                    queryWrapper.eq(OrderItem::getTotalPrice, vo.getTotalPrice());
                }
                return ResultUtils.success(orderItemService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  OrderItemVO vo) {
                OrderItem entity =  OrderItem.builder()
                    .orderInfoOrderInfoId1(vo.getOrderInfoOrderInfoId1())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .quantity(vo.getQuantity())
                    .unitPrice(vo.getUnitPrice())
                    .totalPrice(vo.getTotalPrice())
                .build();








                boolean flag =this.orderItemService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("order_item").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getOrderItemId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( OrderItemVO vo) {
                OrderItem entity =  OrderItem.builder()
                    .orderItemId(vo.getOrderItemId())
                    .orderInfoOrderInfoId1(vo.getOrderInfoOrderInfoId1())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .quantity(vo.getQuantity())
                    .unitPrice(vo.getUnitPrice())
                    .totalPrice(vo.getTotalPrice())
                .build();



                        boolean flag = this.orderItemService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getOrderItemId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( OrderItemVO vo ) {
                OrderItem entity = this.orderItemService.getById(vo.getOrderItemId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( OrderItemVO vo) {
                boolean flag = this.orderItemService.removeById(vo.getOrderItemId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

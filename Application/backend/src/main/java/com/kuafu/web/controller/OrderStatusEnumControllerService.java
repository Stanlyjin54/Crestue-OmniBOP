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
import com.kuafu.web.entity.OrderStatusEnum;
import com.kuafu.web.service.IOrderStatusEnumService;
import com.kuafu.web.vo.OrderStatusEnumPageVO;
import com.kuafu.web.vo.OrderStatusEnumVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 订单状态枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("OrderStatusEnumControllerService")
@RequiredArgsConstructor
public class OrderStatusEnumControllerService  {

    private final IOrderStatusEnumService orderStatusEnumService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( OrderStatusEnumPageVO pageVO){
        IPage<OrderStatusEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<OrderStatusEnum> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<OrderStatusEnum> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(OrderStatusEnum::getOrderStatusEnumId);

            if(pageVO.getOrderStatusEnumId() != null){
                queryWrapper.eq(OrderStatusEnum::getOrderStatusEnumId, pageVO.getOrderStatusEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {
//                        queryWrapper.like(OrderStatusEnum::getStatusName, pageVO.getStatusName());
                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(OrderStatusEnum::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

            return ResultUtils.success(orderStatusEnumService.page(page, queryWrapper));
        }


        public BaseResponse list(  OrderStatusEnumVO vo){
            LambdaQueryWrapper<OrderStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(OrderStatusEnum::getStatusName, vo.getStatusName());
                }
                return ResultUtils.success(orderStatusEnumService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  OrderStatusEnumVO vo) {
                OrderStatusEnum entity =  OrderStatusEnum.builder()
                    .statusName(vo.getStatusName())
                .build();








                boolean flag =this.orderStatusEnumService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("order_status_enum").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getOrderStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( OrderStatusEnumVO vo) {
                OrderStatusEnum entity =  OrderStatusEnum.builder()
                    .orderStatusEnumId(vo.getOrderStatusEnumId())
                    .statusName(vo.getStatusName())
                .build();



                        boolean flag = this.orderStatusEnumService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getOrderStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( OrderStatusEnumVO vo ) {
                OrderStatusEnum entity = this.orderStatusEnumService.getById(vo.getOrderStatusEnumId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( OrderStatusEnumVO vo) {
                boolean flag = this.orderStatusEnumService.removeById(vo.getOrderStatusEnumId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

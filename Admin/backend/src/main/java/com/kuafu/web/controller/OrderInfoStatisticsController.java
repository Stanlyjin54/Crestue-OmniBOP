package com.kuafuweb.web.controller;

import java.util.List;
import java.util.stream.Collectors;
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


import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafuweb.common.domin.BaseResponse;
import com.kuafuweb.common.domin.ErrorCode;
import com.kuafuweb.common.domin.ResultUtils;
import com.kuafuweb.common.util.StringUtils;
import com.kuafuweb.web.entity.OrderInfo;
import com.kuafuweb.web.service.IOrderInfoStatisticsService;
import com.kuafuweb.web.entity.SelectVO;

import com.kuafuweb.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;





/**

 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orderInfoStatistics")

public class OrderInfoStatisticsController  {

    private final IOrderInfoStatisticsService orderInfoStatisticsService;


    @PostMapping("order_info_order_status_enum_order_status_enum_id_1_status_statistic_ad7a7387_count")
    public BaseResponse order_info_order_status_enum_order_status_enum_id_1_status_statistic_ad7a7387_count(@RequestBody OrderInfo statisticVo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderInfoStatisticsService.order_info_order_status_enum_order_status_enum_id_1_status_statistic_ad7a7387_count(queryWrapper));
    }
    @PostMapping("order_info_order_status_enum_order_status_enum_id_1_status_statistic_b94f62d5_count")
    public BaseResponse order_info_order_status_enum_order_status_enum_id_1_status_statistic_b94f62d5_count(@RequestBody OrderInfo statisticVo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderInfoStatisticsService.order_info_order_status_enum_order_status_enum_id_1_status_statistic_b94f62d5_count(queryWrapper));
    }
    @PostMapping("order_info_order_status_enum_order_status_enum_id_1_status_statistic_b8881331_count")
    public BaseResponse order_info_order_status_enum_order_status_enum_id_1_status_statistic_b8881331_count(@RequestBody OrderInfo statisticVo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderInfoStatisticsService.order_info_order_status_enum_order_status_enum_id_1_status_statistic_b8881331_count(queryWrapper));
    }
    @PostMapping("order_info_order_date_datetime_statistic_4a683c7d_count")
    public BaseResponse order_info_order_date_datetime_statistic_4a683c7d_count(@RequestBody OrderInfo statisticVo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderInfoStatisticsService.order_info_order_date_datetime_statistic_4a683c7d_count(queryWrapper));
    }
    @PostMapping("order_info_order_date_datetime_statistic_d9005f45_count")
    public BaseResponse order_info_order_date_datetime_statistic_d9005f45_count(@RequestBody OrderInfo statisticVo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderInfoStatisticsService.order_info_order_date_datetime_statistic_d9005f45_count(queryWrapper));
    }
    @PostMapping("order_info_order_date_datetime_statistic_8d28825b_count")
    public BaseResponse order_info_order_date_datetime_statistic_8d28825b_count(@RequestBody OrderInfo statisticVo) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderInfoStatisticsService.order_info_order_date_datetime_statistic_8d28825b_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<OrderInfo> queryWrapper, OrderInfo statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getOrderNo())) {
                queryWrapper.eq(OrderInfo::getOrderNo, statisticVo.getOrderNo());
            }
            if(statisticVo.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(OrderInfo::getCustomerInfoCustomerInfoId1, statisticVo.getCustomerInfoCustomerInfoId1());
            }
            if(statisticVo.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(OrderInfo::getUserInfoUserInfoId1, statisticVo.getUserInfoUserInfoId1());
            }
            if(statisticVo.getOrderStatusEnumOrderStatusEnumId1() != null){
                queryWrapper.eq(OrderInfo::getOrderStatusEnumOrderStatusEnumId1, statisticVo.getOrderStatusEnumOrderStatusEnumId1());
            }
            if(statisticVo.getTotalAmount() != null){
                queryWrapper.eq(OrderInfo::getTotalAmount, statisticVo.getTotalAmount());
            }
            if(statisticVo.getProfitAmount() != null){
                queryWrapper.eq(OrderInfo::getProfitAmount, statisticVo.getProfitAmount());
            }
            if(statisticVo.getCommission() != null){
                queryWrapper.eq(OrderInfo::getCommission, statisticVo.getCommission());
            }
            if(statisticVo.getShippingFee() != null){
                queryWrapper.eq(OrderInfo::getShippingFee, statisticVo.getShippingFee());
            }
            if(statisticVo.getOrderDate() != null){
                queryWrapper.eq(OrderInfo::getOrderDate, statisticVo.getOrderDate());
            }
        if(StringUtils.isNotEmpty(statisticVo.getGeneralOrders())) {
                queryWrapper.eq(OrderInfo::getGeneralOrders, statisticVo.getGeneralOrders());
            }
    }



}






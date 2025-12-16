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
import com.kuafuweb.web.entity.OrderStatusEnum;
import com.kuafuweb.web.service.IOrderStatusEnumStatisticsService;
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
@RequestMapping("/orderStatusEnumStatistics")

public class OrderStatusEnumStatisticsController  {

    private final IOrderStatusEnumStatisticsService orderStatusEnumStatisticsService;


    @PostMapping("order_status_enum_order_status_enum_id_status_statistic_ed6cd955_count")
    public BaseResponse order_status_enum_order_status_enum_id_status_statistic_ed6cd955_count(@RequestBody OrderStatusEnum statisticVo) {
        LambdaQueryWrapper<OrderStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderStatusEnumStatisticsService.order_status_enum_order_status_enum_id_status_statistic_ed6cd955_count(queryWrapper));
    }
    @PostMapping("order_status_enum_order_status_enum_id_status_statistic_dcd3b45b_count")
    public BaseResponse order_status_enum_order_status_enum_id_status_statistic_dcd3b45b_count(@RequestBody OrderStatusEnum statisticVo) {
        LambdaQueryWrapper<OrderStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderStatusEnumStatisticsService.order_status_enum_order_status_enum_id_status_statistic_dcd3b45b_count(queryWrapper));
    }
    @PostMapping("order_status_enum_order_status_enum_id_status_statistic_d8e4b58d_count")
    public BaseResponse order_status_enum_order_status_enum_id_status_statistic_d8e4b58d_count(@RequestBody OrderStatusEnum statisticVo) {
        LambdaQueryWrapper<OrderStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderStatusEnumStatisticsService.order_status_enum_order_status_enum_id_status_statistic_d8e4b58d_count(queryWrapper));
    }
    @PostMapping("order_status_enum_status_name_status_statistic_cd290941_count")
    public BaseResponse order_status_enum_status_name_status_statistic_cd290941_count(@RequestBody OrderStatusEnum statisticVo) {
        LambdaQueryWrapper<OrderStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderStatusEnumStatisticsService.order_status_enum_status_name_status_statistic_cd290941_count(queryWrapper));
    }
    @PostMapping("order_status_enum_status_name_status_statistic_962848ae_count")
    public BaseResponse order_status_enum_status_name_status_statistic_962848ae_count(@RequestBody OrderStatusEnum statisticVo) {
        LambdaQueryWrapper<OrderStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderStatusEnumStatisticsService.order_status_enum_status_name_status_statistic_962848ae_count(queryWrapper));
    }
    @PostMapping("order_status_enum_status_name_status_statistic_dcb4f308_count")
    public BaseResponse order_status_enum_status_name_status_statistic_dcb4f308_count(@RequestBody OrderStatusEnum statisticVo) {
        LambdaQueryWrapper<OrderStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( orderStatusEnumStatisticsService.order_status_enum_status_name_status_statistic_dcb4f308_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<OrderStatusEnum> queryWrapper, OrderStatusEnum statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getStatusName())) {
                        queryWrapper.like(OrderStatusEnum::getStatusName, statisticVo.getStatusName());
            }
    }



}






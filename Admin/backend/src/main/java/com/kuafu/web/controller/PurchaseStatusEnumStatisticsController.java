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
import com.kuafuweb.web.entity.PurchaseStatusEnum;
import com.kuafuweb.web.service.IPurchaseStatusEnumStatisticsService;
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
@RequestMapping("/purchaseStatusEnumStatistics")

public class PurchaseStatusEnumStatisticsController  {

    private final IPurchaseStatusEnumStatisticsService purchaseStatusEnumStatisticsService;


    @PostMapping("purchase_status_enum_purchase_status_enum_id_status_statistic_cac5b56b_count")
    public BaseResponse purchase_status_enum_purchase_status_enum_id_status_statistic_cac5b56b_count(@RequestBody PurchaseStatusEnum statisticVo) {
        LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseStatusEnumStatisticsService.purchase_status_enum_purchase_status_enum_id_status_statistic_cac5b56b_count(queryWrapper));
    }
    @PostMapping("purchase_status_enum_purchase_status_enum_id_status_statistic_4879421d_count")
    public BaseResponse purchase_status_enum_purchase_status_enum_id_status_statistic_4879421d_count(@RequestBody PurchaseStatusEnum statisticVo) {
        LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseStatusEnumStatisticsService.purchase_status_enum_purchase_status_enum_id_status_statistic_4879421d_count(queryWrapper));
    }
    @PostMapping("purchase_status_enum_purchase_status_enum_id_status_statistic_d51abb7f_count")
    public BaseResponse purchase_status_enum_purchase_status_enum_id_status_statistic_d51abb7f_count(@RequestBody PurchaseStatusEnum statisticVo) {
        LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseStatusEnumStatisticsService.purchase_status_enum_purchase_status_enum_id_status_statistic_d51abb7f_count(queryWrapper));
    }
    @PostMapping("purchase_status_enum_status_name_status_statistic_ae1dfb7a_count")
    public BaseResponse purchase_status_enum_status_name_status_statistic_ae1dfb7a_count(@RequestBody PurchaseStatusEnum statisticVo) {
        LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseStatusEnumStatisticsService.purchase_status_enum_status_name_status_statistic_ae1dfb7a_count(queryWrapper));
    }
    @PostMapping("purchase_status_enum_status_name_status_statistic_b11589b5_count")
    public BaseResponse purchase_status_enum_status_name_status_statistic_b11589b5_count(@RequestBody PurchaseStatusEnum statisticVo) {
        LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseStatusEnumStatisticsService.purchase_status_enum_status_name_status_statistic_b11589b5_count(queryWrapper));
    }
    @PostMapping("purchase_status_enum_status_name_status_statistic_4fce27d5_count")
    public BaseResponse purchase_status_enum_status_name_status_statistic_4fce27d5_count(@RequestBody PurchaseStatusEnum statisticVo) {
        LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseStatusEnumStatisticsService.purchase_status_enum_status_name_status_statistic_4fce27d5_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper, PurchaseStatusEnum statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getStatusName())) {
                        queryWrapper.like(PurchaseStatusEnum::getStatusName, statisticVo.getStatusName());
            }
    }



}






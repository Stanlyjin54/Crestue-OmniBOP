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
import com.kuafuweb.web.entity.PurchaseInfo;
import com.kuafuweb.web.service.IPurchaseInfoStatisticsService;
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
@RequestMapping("/purchaseInfoStatistics")

public class PurchaseInfoStatisticsController  {

    private final IPurchaseInfoStatisticsService purchaseInfoStatisticsService;


    @PostMapping("purchase_info_purchase_date_datetime_statistic_2bd78ba1_count")
    public BaseResponse purchase_info_purchase_date_datetime_statistic_2bd78ba1_count(@RequestBody PurchaseInfo statisticVo) {
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseInfoStatisticsService.purchase_info_purchase_date_datetime_statistic_2bd78ba1_count(queryWrapper));
    }
    @PostMapping("purchase_info_purchase_date_datetime_statistic_11ee5598_count")
    public BaseResponse purchase_info_purchase_date_datetime_statistic_11ee5598_count(@RequestBody PurchaseInfo statisticVo) {
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseInfoStatisticsService.purchase_info_purchase_date_datetime_statistic_11ee5598_count(queryWrapper));
    }
    @PostMapping("purchase_info_purchase_date_datetime_statistic_8e593cc4_count")
    public BaseResponse purchase_info_purchase_date_datetime_statistic_8e593cc4_count(@RequestBody PurchaseInfo statisticVo) {
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseInfoStatisticsService.purchase_info_purchase_date_datetime_statistic_8e593cc4_count(queryWrapper));
    }
    @PostMapping("purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_c751e7be_count")
    public BaseResponse purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_c751e7be_count(@RequestBody PurchaseInfo statisticVo) {
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseInfoStatisticsService.purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_c751e7be_count(queryWrapper));
    }
    @PostMapping("purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_4d195a98_count")
    public BaseResponse purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_4d195a98_count(@RequestBody PurchaseInfo statisticVo) {
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseInfoStatisticsService.purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_4d195a98_count(queryWrapper));
    }
    @PostMapping("purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_f7e55886_count")
    public BaseResponse purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_f7e55886_count(@RequestBody PurchaseInfo statisticVo) {
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( purchaseInfoStatisticsService.purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_f7e55886_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<PurchaseInfo> queryWrapper, PurchaseInfo statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getPurchaseNo())) {
                queryWrapper.eq(PurchaseInfo::getPurchaseNo, statisticVo.getPurchaseNo());
            }
            if(statisticVo.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(PurchaseInfo::getUserInfoUserInfoId1, statisticVo.getUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(statisticVo.getSupplier())) {
                queryWrapper.eq(PurchaseInfo::getSupplier, statisticVo.getSupplier());
            }
            if(statisticVo.getTotalAmount() != null){
                queryWrapper.eq(PurchaseInfo::getTotalAmount, statisticVo.getTotalAmount());
            }
            if(statisticVo.getPurchaseDate() != null){
                queryWrapper.eq(PurchaseInfo::getPurchaseDate, statisticVo.getPurchaseDate());
            }
            if(statisticVo.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
                queryWrapper.eq(PurchaseInfo::getPurchaseStatusEnumPurchaseStatusEnumId1, statisticVo.getPurchaseStatusEnumPurchaseStatusEnumId1());
            }
    }



}






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
import com.kuafuweb.web.entity.TransferInfo;
import com.kuafuweb.web.service.ITransferInfoStatisticsService;
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
@RequestMapping("/transferInfoStatistics")

public class TransferInfoStatisticsController  {

    private final ITransferInfoStatisticsService transferInfoStatisticsService;


    @PostMapping("transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_0b0d5ec1_count")
    public BaseResponse transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_0b0d5ec1_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_0b0d5ec1_count(queryWrapper));
    }
    @PostMapping("transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_3a04bd30_count")
    public BaseResponse transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_3a04bd30_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_3a04bd30_count(queryWrapper));
    }
    @PostMapping("transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_92a0f49d_count")
    public BaseResponse transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_92a0f49d_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_92a0f49d_count(queryWrapper));
    }
    @PostMapping("transfer_info_apply_date_datetime_statistic_1ce09557_count")
    public BaseResponse transfer_info_apply_date_datetime_statistic_1ce09557_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_apply_date_datetime_statistic_1ce09557_count(queryWrapper));
    }
    @PostMapping("transfer_info_apply_date_datetime_statistic_747d5b62_count")
    public BaseResponse transfer_info_apply_date_datetime_statistic_747d5b62_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_apply_date_datetime_statistic_747d5b62_count(queryWrapper));
    }
    @PostMapping("transfer_info_apply_date_datetime_statistic_7f4dfcee_count")
    public BaseResponse transfer_info_apply_date_datetime_statistic_7f4dfcee_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_apply_date_datetime_statistic_7f4dfcee_count(queryWrapper));
    }
    @PostMapping("transfer_info_confirm_date_datetime_statistic_843e5a5b_count")
    public BaseResponse transfer_info_confirm_date_datetime_statistic_843e5a5b_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_confirm_date_datetime_statistic_843e5a5b_count(queryWrapper));
    }
    @PostMapping("transfer_info_confirm_date_datetime_statistic_c99d1d40_count")
    public BaseResponse transfer_info_confirm_date_datetime_statistic_c99d1d40_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_confirm_date_datetime_statistic_c99d1d40_count(queryWrapper));
    }
    @PostMapping("transfer_info_confirm_date_datetime_statistic_9e5bc169_count")
    public BaseResponse transfer_info_confirm_date_datetime_statistic_9e5bc169_count(@RequestBody TransferInfo statisticVo) {
        LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferInfoStatisticsService.transfer_info_confirm_date_datetime_statistic_9e5bc169_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<TransferInfo> queryWrapper, TransferInfo statisticVo){
            if(statisticVo.getFromWarehouseInfoWarehouseInfoId1() != null){
                queryWrapper.eq(TransferInfo::getFromWarehouseInfoWarehouseInfoId1, statisticVo.getFromWarehouseInfoWarehouseInfoId1());
            }
            if(statisticVo.getToWarehouseInfoWarehouseInfoId2() != null){
                queryWrapper.eq(TransferInfo::getToWarehouseInfoWarehouseInfoId2, statisticVo.getToWarehouseInfoWarehouseInfoId2());
            }
            if(statisticVo.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(TransferInfo::getProductInfoProductInfoId1, statisticVo.getProductInfoProductInfoId1());
            }
            if(statisticVo.getQuantity() != null){
                queryWrapper.eq(TransferInfo::getQuantity, statisticVo.getQuantity());
            }
            if(statisticVo.getTransferStatusEnumTransferStatusEnumId1() != null){
                queryWrapper.eq(TransferInfo::getTransferStatusEnumTransferStatusEnumId1, statisticVo.getTransferStatusEnumTransferStatusEnumId1());
            }
            if(statisticVo.getApplyUserInfoUserInfoId1() != null){
                queryWrapper.eq(TransferInfo::getApplyUserInfoUserInfoId1, statisticVo.getApplyUserInfoUserInfoId1());
            }
            if(statisticVo.getConfirmUserInfoUserInfoId2() != null){
                queryWrapper.eq(TransferInfo::getConfirmUserInfoUserInfoId2, statisticVo.getConfirmUserInfoUserInfoId2());
            }
            if(statisticVo.getApplyDate() != null){
                queryWrapper.eq(TransferInfo::getApplyDate, statisticVo.getApplyDate());
            }
            if(statisticVo.getConfirmDate() != null){
                queryWrapper.eq(TransferInfo::getConfirmDate, statisticVo.getConfirmDate());
            }
    }



}






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
import com.kuafuweb.web.entity.TransferStatusEnum;
import com.kuafuweb.web.service.ITransferStatusEnumStatisticsService;
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
@RequestMapping("/transferStatusEnumStatistics")

public class TransferStatusEnumStatisticsController  {

    private final ITransferStatusEnumStatisticsService transferStatusEnumStatisticsService;


    @PostMapping("transfer_status_enum_transfer_status_enum_id_status_statistic_5bfcb35a_count")
    public BaseResponse transfer_status_enum_transfer_status_enum_id_status_statistic_5bfcb35a_count(@RequestBody TransferStatusEnum statisticVo) {
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferStatusEnumStatisticsService.transfer_status_enum_transfer_status_enum_id_status_statistic_5bfcb35a_count(queryWrapper));
    }
    @PostMapping("transfer_status_enum_transfer_status_enum_id_status_statistic_35b1149a_count")
    public BaseResponse transfer_status_enum_transfer_status_enum_id_status_statistic_35b1149a_count(@RequestBody TransferStatusEnum statisticVo) {
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferStatusEnumStatisticsService.transfer_status_enum_transfer_status_enum_id_status_statistic_35b1149a_count(queryWrapper));
    }
    @PostMapping("transfer_status_enum_transfer_status_enum_id_status_statistic_26443357_count")
    public BaseResponse transfer_status_enum_transfer_status_enum_id_status_statistic_26443357_count(@RequestBody TransferStatusEnum statisticVo) {
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferStatusEnumStatisticsService.transfer_status_enum_transfer_status_enum_id_status_statistic_26443357_count(queryWrapper));
    }
    @PostMapping("transfer_status_enum_status_name_status_statistic_29f93e61_count")
    public BaseResponse transfer_status_enum_status_name_status_statistic_29f93e61_count(@RequestBody TransferStatusEnum statisticVo) {
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferStatusEnumStatisticsService.transfer_status_enum_status_name_status_statistic_29f93e61_count(queryWrapper));
    }
    @PostMapping("transfer_status_enum_status_name_status_statistic_a78fbbbf_count")
    public BaseResponse transfer_status_enum_status_name_status_statistic_a78fbbbf_count(@RequestBody TransferStatusEnum statisticVo) {
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferStatusEnumStatisticsService.transfer_status_enum_status_name_status_statistic_a78fbbbf_count(queryWrapper));
    }
    @PostMapping("transfer_status_enum_status_name_status_statistic_58b07edd_count")
    public BaseResponse transfer_status_enum_status_name_status_statistic_58b07edd_count(@RequestBody TransferStatusEnum statisticVo) {
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( transferStatusEnumStatisticsService.transfer_status_enum_status_name_status_statistic_58b07edd_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<TransferStatusEnum> queryWrapper, TransferStatusEnum statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getStatusName())) {
                        queryWrapper.like(TransferStatusEnum::getStatusName, statisticVo.getStatusName());
            }
    }



}






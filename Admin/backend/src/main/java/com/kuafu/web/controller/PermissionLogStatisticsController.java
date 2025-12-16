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
import com.kuafuweb.web.entity.PermissionLog;
import com.kuafuweb.web.service.IPermissionLogStatisticsService;
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
@RequestMapping("/permissionLogStatistics")

public class PermissionLogStatisticsController  {

    private final IPermissionLogStatisticsService permissionLogStatisticsService;


    @PostMapping("permission_log_action_time_datetime_statistic_84170e10_count")
    public BaseResponse permission_log_action_time_datetime_statistic_84170e10_count(@RequestBody PermissionLog statisticVo) {
        LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( permissionLogStatisticsService.permission_log_action_time_datetime_statistic_84170e10_count(queryWrapper));
    }
    @PostMapping("permission_log_action_time_datetime_statistic_975db700_count")
    public BaseResponse permission_log_action_time_datetime_statistic_975db700_count(@RequestBody PermissionLog statisticVo) {
        LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( permissionLogStatisticsService.permission_log_action_time_datetime_statistic_975db700_count(queryWrapper));
    }
    @PostMapping("permission_log_action_time_datetime_statistic_8cf06955_count")
    public BaseResponse permission_log_action_time_datetime_statistic_8cf06955_count(@RequestBody PermissionLog statisticVo) {
        LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( permissionLogStatisticsService.permission_log_action_time_datetime_statistic_8cf06955_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<PermissionLog> queryWrapper, PermissionLog statisticVo){
            if(statisticVo.getUserInfoId() != null){
                queryWrapper.eq(PermissionLog::getUserInfoId, statisticVo.getUserInfoId());
            }
        if(StringUtils.isNotEmpty(statisticVo.getActionType())) {
                queryWrapper.eq(PermissionLog::getActionType, statisticVo.getActionType());
            }
        if(StringUtils.isNotEmpty(statisticVo.getTargetObject())) {
                queryWrapper.eq(PermissionLog::getTargetObject, statisticVo.getTargetObject());
            }
        if(StringUtils.isNotEmpty(statisticVo.getChangeContent())) {
                queryWrapper.eq(PermissionLog::getChangeContent, statisticVo.getChangeContent());
            }
            if(statisticVo.getActionTime() != null){
                queryWrapper.eq(PermissionLog::getActionTime, statisticVo.getActionTime());
            }
    }



}






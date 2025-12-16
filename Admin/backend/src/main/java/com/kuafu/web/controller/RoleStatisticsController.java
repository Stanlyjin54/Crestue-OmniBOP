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
import com.kuafuweb.web.entity.Role;
import com.kuafuweb.web.service.IRoleStatisticsService;
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
@RequestMapping("/roleStatistics")

public class RoleStatisticsController  {

    private final IRoleStatisticsService roleStatisticsService;


    @PostMapping("role_create_time_datetime_statistic_f1dec0e3_count")
    public BaseResponse role_create_time_datetime_statistic_f1dec0e3_count(@RequestBody Role statisticVo) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( roleStatisticsService.role_create_time_datetime_statistic_f1dec0e3_count(queryWrapper));
    }
    @PostMapping("role_create_time_datetime_statistic_30529d9f_count")
    public BaseResponse role_create_time_datetime_statistic_30529d9f_count(@RequestBody Role statisticVo) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( roleStatisticsService.role_create_time_datetime_statistic_30529d9f_count(queryWrapper));
    }
    @PostMapping("role_create_time_datetime_statistic_99f55dc9_count")
    public BaseResponse role_create_time_datetime_statistic_99f55dc9_count(@RequestBody Role statisticVo) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( roleStatisticsService.role_create_time_datetime_statistic_99f55dc9_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<Role> queryWrapper, Role statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getRoleName())) {
                        queryWrapper.like(Role::getRoleName, statisticVo.getRoleName());
            }
        if(StringUtils.isNotEmpty(statisticVo.getRoleDesc())) {
                queryWrapper.eq(Role::getRoleDesc, statisticVo.getRoleDesc());
            }
            if(statisticVo.getCreateTime() != null){
                queryWrapper.eq(Role::getCreateTime, statisticVo.getCreateTime());
            }
    }



}






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
import com.kuafuweb.web.entity.FissionTool;
import com.kuafuweb.web.service.IFissionToolStatisticsService;
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
@RequestMapping("/fissionToolStatistics")

public class FissionToolStatisticsController  {

    private final IFissionToolStatisticsService fissionToolStatisticsService;


    @PostMapping("fission_tool_start_date_datetime_statistic_79c789ea_count")
    public BaseResponse fission_tool_start_date_datetime_statistic_79c789ea_count(@RequestBody FissionTool statisticVo) {
        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( fissionToolStatisticsService.fission_tool_start_date_datetime_statistic_79c789ea_count(queryWrapper));
    }
    @PostMapping("fission_tool_start_date_datetime_statistic_a2b0c036_count")
    public BaseResponse fission_tool_start_date_datetime_statistic_a2b0c036_count(@RequestBody FissionTool statisticVo) {
        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( fissionToolStatisticsService.fission_tool_start_date_datetime_statistic_a2b0c036_count(queryWrapper));
    }
    @PostMapping("fission_tool_start_date_datetime_statistic_a11aa4e6_count")
    public BaseResponse fission_tool_start_date_datetime_statistic_a11aa4e6_count(@RequestBody FissionTool statisticVo) {
        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( fissionToolStatisticsService.fission_tool_start_date_datetime_statistic_a11aa4e6_count(queryWrapper));
    }
    @PostMapping("fission_tool_end_date_datetime_statistic_308b2d65_count")
    public BaseResponse fission_tool_end_date_datetime_statistic_308b2d65_count(@RequestBody FissionTool statisticVo) {
        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( fissionToolStatisticsService.fission_tool_end_date_datetime_statistic_308b2d65_count(queryWrapper));
    }
    @PostMapping("fission_tool_end_date_datetime_statistic_955257d7_count")
    public BaseResponse fission_tool_end_date_datetime_statistic_955257d7_count(@RequestBody FissionTool statisticVo) {
        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( fissionToolStatisticsService.fission_tool_end_date_datetime_statistic_955257d7_count(queryWrapper));
    }
    @PostMapping("fission_tool_end_date_datetime_statistic_700394af_count")
    public BaseResponse fission_tool_end_date_datetime_statistic_700394af_count(@RequestBody FissionTool statisticVo) {
        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( fissionToolStatisticsService.fission_tool_end_date_datetime_statistic_700394af_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<FissionTool> queryWrapper, FissionTool statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getToolType())) {
                queryWrapper.eq(FissionTool::getToolType, statisticVo.getToolType());
            }
        if(StringUtils.isNotEmpty(statisticVo.getTitle())) {
                queryWrapper.eq(FissionTool::getTitle, statisticVo.getTitle());
            }
        if(StringUtils.isNotEmpty(statisticVo.getConfig())) {
                queryWrapper.eq(FissionTool::getConfig, statisticVo.getConfig());
            }
            if(statisticVo.getStartDate() != null){
                queryWrapper.eq(FissionTool::getStartDate, statisticVo.getStartDate());
            }
            if(statisticVo.getEndDate() != null){
                queryWrapper.eq(FissionTool::getEndDate, statisticVo.getEndDate());
            }
    }



}






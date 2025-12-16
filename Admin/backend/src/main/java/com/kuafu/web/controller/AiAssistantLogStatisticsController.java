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
import com.kuafuweb.web.entity.AiAssistantLog;
import com.kuafuweb.web.service.IAiAssistantLogStatisticsService;
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
@RequestMapping("/aiAssistantLogStatistics")

public class AiAssistantLogStatisticsController  {

    private final IAiAssistantLogStatisticsService aiAssistantLogStatisticsService;


    @PostMapping("ai_assistant_log_query_time_datetime_statistic_9b682c72_count")
    public BaseResponse ai_assistant_log_query_time_datetime_statistic_9b682c72_count(@RequestBody AiAssistantLog statisticVo) {
        LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( aiAssistantLogStatisticsService.ai_assistant_log_query_time_datetime_statistic_9b682c72_count(queryWrapper));
    }
    @PostMapping("ai_assistant_log_query_time_datetime_statistic_df5261a6_count")
    public BaseResponse ai_assistant_log_query_time_datetime_statistic_df5261a6_count(@RequestBody AiAssistantLog statisticVo) {
        LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( aiAssistantLogStatisticsService.ai_assistant_log_query_time_datetime_statistic_df5261a6_count(queryWrapper));
    }
    @PostMapping("ai_assistant_log_query_time_datetime_statistic_d691e2ab_count")
    public BaseResponse ai_assistant_log_query_time_datetime_statistic_d691e2ab_count(@RequestBody AiAssistantLog statisticVo) {
        LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( aiAssistantLogStatisticsService.ai_assistant_log_query_time_datetime_statistic_d691e2ab_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<AiAssistantLog> queryWrapper, AiAssistantLog statisticVo){
            if(statisticVo.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(AiAssistantLog::getUserInfoUserInfoId1, statisticVo.getUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(statisticVo.getQueryText())) {
                queryWrapper.eq(AiAssistantLog::getQueryText, statisticVo.getQueryText());
            }
        if(StringUtils.isNotEmpty(statisticVo.getResponseText())) {
                queryWrapper.eq(AiAssistantLog::getResponseText, statisticVo.getResponseText());
            }
            if(statisticVo.getQueryTime() != null){
                queryWrapper.eq(AiAssistantLog::getQueryTime, statisticVo.getQueryTime());
            }
    }



}






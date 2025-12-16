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
import com.kuafuweb.web.entity.BusinessOpportunity;
import com.kuafuweb.web.service.IBusinessOpportunityStatisticsService;
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
@RequestMapping("/businessOpportunityStatistics")

public class BusinessOpportunityStatisticsController  {

    private final IBusinessOpportunityStatisticsService businessOpportunityStatisticsService;


    @PostMapping("business_opportunity_status_status_statistic_7faf0bdd_count")
    public BaseResponse business_opportunity_status_status_statistic_7faf0bdd_count(@RequestBody BusinessOpportunity statisticVo) {
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( businessOpportunityStatisticsService.business_opportunity_status_status_statistic_7faf0bdd_count(queryWrapper));
    }
    @PostMapping("business_opportunity_status_status_statistic_5ba2a19d_count")
    public BaseResponse business_opportunity_status_status_statistic_5ba2a19d_count(@RequestBody BusinessOpportunity statisticVo) {
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( businessOpportunityStatisticsService.business_opportunity_status_status_statistic_5ba2a19d_count(queryWrapper));
    }
    @PostMapping("business_opportunity_status_status_statistic_3d13a485_count")
    public BaseResponse business_opportunity_status_status_statistic_3d13a485_count(@RequestBody BusinessOpportunity statisticVo) {
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( businessOpportunityStatisticsService.business_opportunity_status_status_statistic_3d13a485_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<BusinessOpportunity> queryWrapper, BusinessOpportunity statisticVo){
            if(statisticVo.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(BusinessOpportunity::getCustomerInfoCustomerInfoId1, statisticVo.getCustomerInfoCustomerInfoId1());
            }
        if(StringUtils.isNotEmpty(statisticVo.getOpportunityName())) {
                        queryWrapper.like(BusinessOpportunity::getOpportunityName, statisticVo.getOpportunityName());
            }
        if(StringUtils.isNotEmpty(statisticVo.getStatus())) {
                queryWrapper.eq(BusinessOpportunity::getStatus, statisticVo.getStatus());
            }
        if(StringUtils.isNotEmpty(statisticVo.getFollowUpSuggestion())) {
                queryWrapper.eq(BusinessOpportunity::getFollowUpSuggestion, statisticVo.getFollowUpSuggestion());
            }
    }



}






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
import com.kuafuweb.web.entity.ProcurementSuggestion;
import com.kuafuweb.web.service.IProcurementSuggestionStatisticsService;
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
@RequestMapping("/procurementSuggestionStatistics")

public class ProcurementSuggestionStatisticsController  {

    private final IProcurementSuggestionStatisticsService procurementSuggestionStatisticsService;


    @PostMapping("procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count")
    public BaseResponse procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count(@RequestBody ProcurementSuggestion statisticVo) {
        LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( procurementSuggestionStatisticsService.procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count(queryWrapper));
    }
    @PostMapping("procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count")
    public BaseResponse procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count(@RequestBody ProcurementSuggestion statisticVo) {
        LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( procurementSuggestionStatisticsService.procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count(queryWrapper));
    }
    @PostMapping("procurement_suggestion_suggest_date_datetime_statistic_00313900_count")
    public BaseResponse procurement_suggestion_suggest_date_datetime_statistic_00313900_count(@RequestBody ProcurementSuggestion statisticVo) {
        LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( procurementSuggestionStatisticsService.procurement_suggestion_suggest_date_datetime_statistic_00313900_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<ProcurementSuggestion> queryWrapper, ProcurementSuggestion statisticVo){
            if(statisticVo.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(ProcurementSuggestion::getProductInfoProductInfoId1, statisticVo.getProductInfoProductInfoId1());
            }
            if(statisticVo.getSuggestQuantity() != null){
                queryWrapper.eq(ProcurementSuggestion::getSuggestQuantity, statisticVo.getSuggestQuantity());
            }
        if(StringUtils.isNotEmpty(statisticVo.getReason())) {
                queryWrapper.eq(ProcurementSuggestion::getReason, statisticVo.getReason());
            }
            if(statisticVo.getSuggestDate() != null){
                queryWrapper.eq(ProcurementSuggestion::getSuggestDate, statisticVo.getSuggestDate());
            }
    }



}






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
import com.kuafuweb.web.entity.ProfitBoard;
import com.kuafuweb.web.service.IProfitBoardStatisticsService;
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
@RequestMapping("/profitBoardStatistics")

public class ProfitBoardStatisticsController  {

    private final IProfitBoardStatisticsService profitBoardStatisticsService;


    @PostMapping("profit_board_stat_date_datetime_statistic_17470edd_count")
    public BaseResponse profit_board_stat_date_datetime_statistic_17470edd_count(@RequestBody ProfitBoard statisticVo) {
        LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( profitBoardStatisticsService.profit_board_stat_date_datetime_statistic_17470edd_count(queryWrapper));
    }
    @PostMapping("profit_board_stat_date_datetime_statistic_edf98856_count")
    public BaseResponse profit_board_stat_date_datetime_statistic_edf98856_count(@RequestBody ProfitBoard statisticVo) {
        LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( profitBoardStatisticsService.profit_board_stat_date_datetime_statistic_edf98856_count(queryWrapper));
    }
    @PostMapping("profit_board_stat_date_datetime_statistic_f80cc477_count")
    public BaseResponse profit_board_stat_date_datetime_statistic_f80cc477_count(@RequestBody ProfitBoard statisticVo) {
        LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( profitBoardStatisticsService.profit_board_stat_date_datetime_statistic_f80cc477_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<ProfitBoard> queryWrapper, ProfitBoard statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getTargetType())) {
                queryWrapper.eq(ProfitBoard::getTargetType, statisticVo.getTargetType());
            }
            if(statisticVo.getTargetId() != null){
                queryWrapper.eq(ProfitBoard::getTargetId, statisticVo.getTargetId());
            }
            if(statisticVo.getProfitAmount() != null){
                queryWrapper.eq(ProfitBoard::getProfitAmount, statisticVo.getProfitAmount());
            }
            if(statisticVo.getCommission() != null){
                queryWrapper.eq(ProfitBoard::getCommission, statisticVo.getCommission());
            }
            if(statisticVo.getShippingFee() != null){
                queryWrapper.eq(ProfitBoard::getShippingFee, statisticVo.getShippingFee());
            }
            if(statisticVo.getStatDate() != null){
                queryWrapper.eq(ProfitBoard::getStatDate, statisticVo.getStatDate());
            }
    }



}






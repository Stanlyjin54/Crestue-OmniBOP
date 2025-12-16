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
import com.kuafuweb.web.entity.RfmLevelEnum;
import com.kuafuweb.web.service.IRfmLevelEnumStatisticsService;
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
@RequestMapping("/rfmLevelEnumStatistics")

public class RfmLevelEnumStatisticsController  {

    private final IRfmLevelEnumStatisticsService rfmLevelEnumStatisticsService;


    @PostMapping("rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count")
    public BaseResponse rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count(@RequestBody RfmLevelEnum statisticVo) {
        LambdaQueryWrapper<RfmLevelEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( rfmLevelEnumStatisticsService.rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count(queryWrapper));
    }
    @PostMapping("rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count")
    public BaseResponse rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count(@RequestBody RfmLevelEnum statisticVo) {
        LambdaQueryWrapper<RfmLevelEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( rfmLevelEnumStatisticsService.rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count(queryWrapper));
    }
    @PostMapping("rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count")
    public BaseResponse rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count(@RequestBody RfmLevelEnum statisticVo) {
        LambdaQueryWrapper<RfmLevelEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( rfmLevelEnumStatisticsService.rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<RfmLevelEnum> queryWrapper, RfmLevelEnum statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getLevelName())) {
                        queryWrapper.like(RfmLevelEnum::getLevelName, statisticVo.getLevelName());
            }
    }



}






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
import com.kuafuweb.web.entity.ProductStatusEnum;
import com.kuafuweb.web.service.IProductStatusEnumStatisticsService;
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
@RequestMapping("/productStatusEnumStatistics")

public class ProductStatusEnumStatisticsController  {

    private final IProductStatusEnumStatisticsService productStatusEnumStatisticsService;


    @PostMapping("product_status_enum_product_status_enum_id_status_statistic_d77ca5ce_count")
    public BaseResponse product_status_enum_product_status_enum_id_status_statistic_d77ca5ce_count(@RequestBody ProductStatusEnum statisticVo) {
        LambdaQueryWrapper<ProductStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productStatusEnumStatisticsService.product_status_enum_product_status_enum_id_status_statistic_d77ca5ce_count(queryWrapper));
    }
    @PostMapping("product_status_enum_product_status_enum_id_status_statistic_e288bf0e_count")
    public BaseResponse product_status_enum_product_status_enum_id_status_statistic_e288bf0e_count(@RequestBody ProductStatusEnum statisticVo) {
        LambdaQueryWrapper<ProductStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productStatusEnumStatisticsService.product_status_enum_product_status_enum_id_status_statistic_e288bf0e_count(queryWrapper));
    }
    @PostMapping("product_status_enum_product_status_enum_id_status_statistic_3e0cb3c6_count")
    public BaseResponse product_status_enum_product_status_enum_id_status_statistic_3e0cb3c6_count(@RequestBody ProductStatusEnum statisticVo) {
        LambdaQueryWrapper<ProductStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productStatusEnumStatisticsService.product_status_enum_product_status_enum_id_status_statistic_3e0cb3c6_count(queryWrapper));
    }
    @PostMapping("product_status_enum_status_name_status_statistic_fe3afe34_count")
    public BaseResponse product_status_enum_status_name_status_statistic_fe3afe34_count(@RequestBody ProductStatusEnum statisticVo) {
        LambdaQueryWrapper<ProductStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productStatusEnumStatisticsService.product_status_enum_status_name_status_statistic_fe3afe34_count(queryWrapper));
    }
    @PostMapping("product_status_enum_status_name_status_statistic_1a658de2_count")
    public BaseResponse product_status_enum_status_name_status_statistic_1a658de2_count(@RequestBody ProductStatusEnum statisticVo) {
        LambdaQueryWrapper<ProductStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productStatusEnumStatisticsService.product_status_enum_status_name_status_statistic_1a658de2_count(queryWrapper));
    }
    @PostMapping("product_status_enum_status_name_status_statistic_8130dea7_count")
    public BaseResponse product_status_enum_status_name_status_statistic_8130dea7_count(@RequestBody ProductStatusEnum statisticVo) {
        LambdaQueryWrapper<ProductStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productStatusEnumStatisticsService.product_status_enum_status_name_status_statistic_8130dea7_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<ProductStatusEnum> queryWrapper, ProductStatusEnum statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getStatusName())) {
                        queryWrapper.like(ProductStatusEnum::getStatusName, statisticVo.getStatusName());
            }
    }



}






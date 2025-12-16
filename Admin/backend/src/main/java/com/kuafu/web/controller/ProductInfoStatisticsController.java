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
import com.kuafuweb.web.entity.ProductInfo;
import com.kuafuweb.web.service.IProductInfoStatisticsService;
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
@RequestMapping("/productInfoStatistics")

public class ProductInfoStatisticsController  {

    private final IProductInfoStatisticsService productInfoStatisticsService;


    @PostMapping("product_info_shelf_life_datetime_statistic_13b09a91_count")
    public BaseResponse product_info_shelf_life_datetime_statistic_13b09a91_count(@RequestBody ProductInfo statisticVo) {
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productInfoStatisticsService.product_info_shelf_life_datetime_statistic_13b09a91_count(queryWrapper));
    }
    @PostMapping("product_info_shelf_life_datetime_statistic_fabe9b9f_count")
    public BaseResponse product_info_shelf_life_datetime_statistic_fabe9b9f_count(@RequestBody ProductInfo statisticVo) {
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productInfoStatisticsService.product_info_shelf_life_datetime_statistic_fabe9b9f_count(queryWrapper));
    }
    @PostMapping("product_info_shelf_life_datetime_statistic_b986a1ea_count")
    public BaseResponse product_info_shelf_life_datetime_statistic_b986a1ea_count(@RequestBody ProductInfo statisticVo) {
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productInfoStatisticsService.product_info_shelf_life_datetime_statistic_b986a1ea_count(queryWrapper));
    }
    @PostMapping("product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count")
    public BaseResponse product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count(@RequestBody ProductInfo statisticVo) {
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productInfoStatisticsService.product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count(queryWrapper));
    }
    @PostMapping("product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count")
    public BaseResponse product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count(@RequestBody ProductInfo statisticVo) {
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productInfoStatisticsService.product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count(queryWrapper));
    }
    @PostMapping("product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count")
    public BaseResponse product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count(@RequestBody ProductInfo statisticVo) {
        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( productInfoStatisticsService.product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<ProductInfo> queryWrapper, ProductInfo statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getProductName())) {
                        queryWrapper.like(ProductInfo::getProductName, statisticVo.getProductName());
            }
        if(StringUtils.isNotEmpty(statisticVo.getBarcode())) {
                queryWrapper.eq(ProductInfo::getBarcode, statisticVo.getBarcode());
            }
        if(StringUtils.isNotEmpty(statisticVo.getCategory())) {
                queryWrapper.eq(ProductInfo::getCategory, statisticVo.getCategory());
            }
        if(StringUtils.isNotEmpty(statisticVo.getBrand())) {
                queryWrapper.eq(ProductInfo::getBrand, statisticVo.getBrand());
            }
        if(StringUtils.isNotEmpty(statisticVo.getSpecification())) {
                queryWrapper.eq(ProductInfo::getSpecification, statisticVo.getSpecification());
            }
        if(StringUtils.isNotEmpty(statisticVo.getUnit())) {
                queryWrapper.eq(ProductInfo::getUnit, statisticVo.getUnit());
            }
        if(StringUtils.isNotEmpty(statisticVo.getImagesResourceKey())) {
                queryWrapper.eq(ProductInfo::getImagesResourceKey, statisticVo.getImagesResourceKey());
            }
        if(StringUtils.isNotEmpty(statisticVo.getSerialNumber())) {
                queryWrapper.eq(ProductInfo::getSerialNumber, statisticVo.getSerialNumber());
            }
            if(statisticVo.getShelfLife() != null){
                queryWrapper.eq(ProductInfo::getShelfLife, statisticVo.getShelfLife());
            }
            if(statisticVo.getMinStock() != null){
                queryWrapper.eq(ProductInfo::getMinStock, statisticVo.getMinStock());
            }
            if(statisticVo.getProductStatusEnumProductStatusEnumId1() != null){
                queryWrapper.eq(ProductInfo::getProductStatusEnumProductStatusEnumId1, statisticVo.getProductStatusEnumProductStatusEnumId1());
            }
    }



}






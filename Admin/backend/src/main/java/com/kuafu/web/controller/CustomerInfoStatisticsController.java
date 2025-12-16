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
import com.kuafuweb.web.entity.CustomerInfo;
import com.kuafuweb.web.service.ICustomerInfoStatisticsService;
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
@RequestMapping("/customerInfoStatistics")

public class CustomerInfoStatisticsController  {

    private final ICustomerInfoStatisticsService customerInfoStatisticsService;


    @PostMapping("customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_ef82b098_count")
    public BaseResponse customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_ef82b098_count(@RequestBody CustomerInfo statisticVo) {
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerInfoStatisticsService.customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_ef82b098_count(queryWrapper));
    }
    @PostMapping("customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_77cd48e4_count")
    public BaseResponse customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_77cd48e4_count(@RequestBody CustomerInfo statisticVo) {
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerInfoStatisticsService.customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_77cd48e4_count(queryWrapper));
    }
    @PostMapping("customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_b209e896_count")
    public BaseResponse customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_b209e896_count(@RequestBody CustomerInfo statisticVo) {
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerInfoStatisticsService.customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_b209e896_count(queryWrapper));
    }
    @PostMapping("customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_376f16a9_count")
    public BaseResponse customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_376f16a9_count(@RequestBody CustomerInfo statisticVo) {
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerInfoStatisticsService.customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_376f16a9_count(queryWrapper));
    }
    @PostMapping("customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_dc7b8cc0_count")
    public BaseResponse customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_dc7b8cc0_count(@RequestBody CustomerInfo statisticVo) {
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerInfoStatisticsService.customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_dc7b8cc0_count(queryWrapper));
    }
    @PostMapping("customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_c56bdc0c_count")
    public BaseResponse customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_c56bdc0c_count(@RequestBody CustomerInfo statisticVo) {
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerInfoStatisticsService.customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_c56bdc0c_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<CustomerInfo> queryWrapper, CustomerInfo statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getCustomerName())) {
                        queryWrapper.like(CustomerInfo::getCustomerName, statisticVo.getCustomerName());
            }
        if(StringUtils.isNotEmpty(statisticVo.getPhone())) {
                queryWrapper.eq(CustomerInfo::getPhone, statisticVo.getPhone());
            }
        if(StringUtils.isNotEmpty(statisticVo.getEmail())) {
                queryWrapper.eq(CustomerInfo::getEmail, statisticVo.getEmail());
            }
        if(StringUtils.isNotEmpty(statisticVo.getAddress())) {
                queryWrapper.eq(CustomerInfo::getAddress, statisticVo.getAddress());
            }
            if(statisticVo.getRfmLevelEnumRfmLevelEnumId1() != null){
                queryWrapper.eq(CustomerInfo::getRfmLevelEnumRfmLevelEnumId1, statisticVo.getRfmLevelEnumRfmLevelEnumId1());
            }
            if(statisticVo.getCustomerTagEnumCustomerTagEnumId1() != null){
                queryWrapper.eq(CustomerInfo::getCustomerTagEnumCustomerTagEnumId1, statisticVo.getCustomerTagEnumCustomerTagEnumId1());
            }
        if(StringUtils.isNotEmpty(statisticVo.getWechatId())) {
                queryWrapper.eq(CustomerInfo::getWechatId, statisticVo.getWechatId());
            }
        if(StringUtils.isNotEmpty(statisticVo.getWechatDataEncrypted())) {
                queryWrapper.eq(CustomerInfo::getWechatDataEncrypted, statisticVo.getWechatDataEncrypted());
            }
        if(StringUtils.isNotEmpty(statisticVo.getSource())) {
                queryWrapper.eq(CustomerInfo::getSource, statisticVo.getSource());
            }
    }



}






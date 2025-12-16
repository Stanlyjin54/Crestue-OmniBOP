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
import com.kuafuweb.web.entity.CustomerTagEnum;
import com.kuafuweb.web.service.ICustomerTagEnumStatisticsService;
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
@RequestMapping("/customerTagEnumStatistics")

public class CustomerTagEnumStatisticsController  {

    private final ICustomerTagEnumStatisticsService customerTagEnumStatisticsService;


    @PostMapping("customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count")
    public BaseResponse customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count(@RequestBody CustomerTagEnum statisticVo) {
        LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerTagEnumStatisticsService.customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count(queryWrapper));
    }
    @PostMapping("customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count")
    public BaseResponse customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count(@RequestBody CustomerTagEnum statisticVo) {
        LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerTagEnumStatisticsService.customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count(queryWrapper));
    }
    @PostMapping("customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count")
    public BaseResponse customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count(@RequestBody CustomerTagEnum statisticVo) {
        LambdaQueryWrapper<CustomerTagEnum> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( customerTagEnumStatisticsService.customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<CustomerTagEnum> queryWrapper, CustomerTagEnum statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getTagName())) {
                        queryWrapper.like(CustomerTagEnum::getTagName, statisticVo.getTagName());
            }
            if(statisticVo.getIsAiGenerated() != null){
                queryWrapper.eq(CustomerTagEnum::getIsAiGenerated, statisticVo.getIsAiGenerated());
            }
    }



}






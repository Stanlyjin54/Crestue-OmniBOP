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
import com.kuafuweb.web.entity.InventoryRecord;
import com.kuafuweb.web.service.IInventoryRecordStatisticsService;
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
@RequestMapping("/inventoryRecordStatistics")

public class InventoryRecordStatisticsController  {

    private final IInventoryRecordStatisticsService inventoryRecordStatisticsService;


    @PostMapping("inventory_record_expire_date_datetime_statistic_a38e9712_count")
    public BaseResponse inventory_record_expire_date_datetime_statistic_a38e9712_count(@RequestBody InventoryRecord statisticVo) {
        LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( inventoryRecordStatisticsService.inventory_record_expire_date_datetime_statistic_a38e9712_count(queryWrapper));
    }
    @PostMapping("inventory_record_expire_date_datetime_statistic_00541631_count")
    public BaseResponse inventory_record_expire_date_datetime_statistic_00541631_count(@RequestBody InventoryRecord statisticVo) {
        LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( inventoryRecordStatisticsService.inventory_record_expire_date_datetime_statistic_00541631_count(queryWrapper));
    }
    @PostMapping("inventory_record_expire_date_datetime_statistic_5296a1bd_count")
    public BaseResponse inventory_record_expire_date_datetime_statistic_5296a1bd_count(@RequestBody InventoryRecord statisticVo) {
        LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( inventoryRecordStatisticsService.inventory_record_expire_date_datetime_statistic_5296a1bd_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<InventoryRecord> queryWrapper, InventoryRecord statisticVo){
            if(statisticVo.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(InventoryRecord::getProductInfoProductInfoId1, statisticVo.getProductInfoProductInfoId1());
            }
            if(statisticVo.getWarehouseInfoWarehouseInfoId1() != null){
                queryWrapper.eq(InventoryRecord::getWarehouseInfoWarehouseInfoId1, statisticVo.getWarehouseInfoWarehouseInfoId1());
            }
            if(statisticVo.getQuantity() != null){
                queryWrapper.eq(InventoryRecord::getQuantity, statisticVo.getQuantity());
            }
        if(StringUtils.isNotEmpty(statisticVo.getBatchNumber())) {
                queryWrapper.eq(InventoryRecord::getBatchNumber, statisticVo.getBatchNumber());
            }
            if(statisticVo.getExpireDate() != null){
                queryWrapper.eq(InventoryRecord::getExpireDate, statisticVo.getExpireDate());
            }
        if(StringUtils.isNotEmpty(statisticVo.getSerialNumber())) {
                queryWrapper.eq(InventoryRecord::getSerialNumber, statisticVo.getSerialNumber());
            }
    }



}






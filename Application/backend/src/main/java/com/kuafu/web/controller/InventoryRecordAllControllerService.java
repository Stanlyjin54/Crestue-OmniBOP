package com.kuafu.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import java.io.Serializable;
import com.kuafu.common.login.IControllerService;

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
import org.apache.commons.lang3.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.kuafu.login.annotation.*;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.InventoryRecordAll;
import com.kuafu.web.service.IInventoryRecordAllService;
import com.kuafu.web.vo.InventoryRecordAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IProductInfoService;
import com.kuafu.web.entity.ProductInfo;
import com.kuafu.web.service.IWarehouseInfoService;
import com.kuafu.web.entity.WarehouseInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 库存记录 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("InventoryRecordAllControllerService")
@RequiredArgsConstructor
public class InventoryRecordAllControllerService implements IControllerService<InventoryRecordAll> {

    private final IInventoryRecordAllService inventoryRecordAllService;





    public BaseResponse page( InventoryRecordAllPageVO pageVO){
        IPage<InventoryRecordAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<InventoryRecordAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ir.inventory_record_id");
        LambdaQueryWrapper<InventoryRecordAll> queryWrapper = objectQueryWrapper.lambda();


            if(pageVO.getQuantity() != null){
                queryWrapper.eq(InventoryRecordAll::getQuantity, pageVO.getQuantity());
            }
        if(StringUtils.isNotEmpty(pageVO.getBatchNumber())) {
                queryWrapper.like(InventoryRecordAll::getBatchNumber, pageVO.getBatchNumber());
            }
            if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
                queryWrapper.eq(InventoryRecordAll::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
            }
            if(pageVO.getInventoryRecordId() != null){
                queryWrapper.eq(InventoryRecordAll::getInventoryRecordId, pageVO.getInventoryRecordId());
            }
        if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
                queryWrapper.like(InventoryRecordAll::getSpecification, pageVO.getSpecification());
            }
        if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                queryWrapper.like(InventoryRecordAll::getSerialNumber, pageVO.getSerialNumber());
            }
            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(InventoryRecordAll::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }
            if(pageVO.getWarehouseInfoWarehouseInfoId1() != null){
                queryWrapper.eq(InventoryRecordAll::getWarehouseInfoWarehouseInfoId1, pageVO.getWarehouseInfoWarehouseInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getProductName())) {

                        String s_string = pageVO.getProductName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(InventoryRecordAll::getProductName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getShelfLife() != null){
                queryWrapper.eq(InventoryRecordAll::getShelfLife, pageVO.getShelfLife());
            }
        if(StringUtils.isNotEmpty(pageVO.getUnit())) {
                queryWrapper.like(InventoryRecordAll::getUnit, pageVO.getUnit());
            }
            if(pageVO.getExpireDate() != null){
                queryWrapper.eq(InventoryRecordAll::getExpireDate, pageVO.getExpireDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getWarehouseName())) {

                        String s_string = pageVO.getWarehouseName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(InventoryRecordAll::getWarehouseName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getMinStock() != null){
                queryWrapper.eq(InventoryRecordAll::getMinStock, pageVO.getMinStock());
            }
        if(StringUtils.isNotEmpty(pageVO.getLocation())) {
                queryWrapper.like(InventoryRecordAll::getLocation, pageVO.getLocation());
            }
        if(StringUtils.isNotEmpty(pageVO.getCategory())) {
                queryWrapper.like(InventoryRecordAll::getCategory, pageVO.getCategory());
            }
        if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
                queryWrapper.like(InventoryRecordAll::getBarcode, pageVO.getBarcode());
            }
        if(StringUtils.isNotEmpty(pageVO.getBrand())) {
                queryWrapper.like(InventoryRecordAll::getBrand, pageVO.getBrand());
            }

                    inventoryRecordAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(InventoryRecordAllPageVO vo){
            LambdaQueryWrapper<InventoryRecordAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getQuantity() != null){
                    queryWrapper.eq(InventoryRecordAll::getQuantity, vo.getQuantity());
                }
            if(StringUtils.isNotEmpty(vo.getBatchNumber())) {
                    queryWrapper.eq(InventoryRecordAll::getBatchNumber, vo.getBatchNumber());
                }
                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                    queryWrapper.eq(InventoryRecordAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                    queryWrapper.eq(InventoryRecordAll::getSpecification, vo.getSpecification());
                }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                    queryWrapper.eq(InventoryRecordAll::getSerialNumber, vo.getSerialNumber());
                }
                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(InventoryRecordAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
                if(vo.getWarehouseInfoWarehouseInfoId1() != null){
                    queryWrapper.eq(InventoryRecordAll::getWarehouseInfoWarehouseInfoId1, vo.getWarehouseInfoWarehouseInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getProductName())) {
                            queryWrapper.like(InventoryRecordAll::getProductName, vo.getProductName());
                }
                if(vo.getShelfLife() != null){
                    queryWrapper.eq(InventoryRecordAll::getShelfLife, vo.getShelfLife());
                }
            if(StringUtils.isNotEmpty(vo.getUnit())) {
                    queryWrapper.eq(InventoryRecordAll::getUnit, vo.getUnit());
                }
                if(vo.getExpireDate() != null){
                    queryWrapper.eq(InventoryRecordAll::getExpireDate, vo.getExpireDate());
                }
            if(StringUtils.isNotEmpty(vo.getWarehouseName())) {
                            queryWrapper.like(InventoryRecordAll::getWarehouseName, vo.getWarehouseName());
                }
                if(vo.getMinStock() != null){
                    queryWrapper.eq(InventoryRecordAll::getMinStock, vo.getMinStock());
                }
            if(StringUtils.isNotEmpty(vo.getLocation())) {
                    queryWrapper.eq(InventoryRecordAll::getLocation, vo.getLocation());
                }
            if(StringUtils.isNotEmpty(vo.getCategory())) {
                    queryWrapper.eq(InventoryRecordAll::getCategory, vo.getCategory());
                }
            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                    queryWrapper.eq(InventoryRecordAll::getBarcode, vo.getBarcode());
                }
            if(StringUtils.isNotEmpty(vo.getBrand())) {
                    queryWrapper.eq(InventoryRecordAll::getBrand, vo.getBrand());
                }
                return ResultUtils.success(inventoryRecordAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(InventoryRecordAllPageVO vo) {


                LambdaQueryWrapper<InventoryRecordAll> queryWrapper = new LambdaQueryWrapper<>();


                                if(vo.getQuantity() != null){
                                    queryWrapper.eq(InventoryRecordAll::getQuantity, vo.getQuantity());
                                }


                            if(StringUtils.isNotEmpty(vo.getBatchNumber())) {
                                    queryWrapper.eq(InventoryRecordAll::getBatchNumber, vo.getBatchNumber());
                                }


                                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                                    queryWrapper.eq(InventoryRecordAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                                }


                                if(vo.getInventoryRecordId() != null){
                                    queryWrapper.eq(InventoryRecordAll::getInventoryRecordId, vo.getInventoryRecordId());
                                }


                            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                                    queryWrapper.eq(InventoryRecordAll::getSpecification, vo.getSpecification());
                                }


                            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                                    queryWrapper.eq(InventoryRecordAll::getSerialNumber, vo.getSerialNumber());
                                }


                                if(vo.getProductInfoProductInfoId1() != null){
                                    queryWrapper.eq(InventoryRecordAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                                }


                                if(vo.getWarehouseInfoWarehouseInfoId1() != null){
                                    queryWrapper.eq(InventoryRecordAll::getWarehouseInfoWarehouseInfoId1, vo.getWarehouseInfoWarehouseInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getProductName())) {
                                            queryWrapper.like(InventoryRecordAll::getProductName, vo.getProductName());
                                }


                                if(vo.getShelfLife() != null){
                                    queryWrapper.eq(InventoryRecordAll::getShelfLife, vo.getShelfLife());
                                }


                            if(StringUtils.isNotEmpty(vo.getUnit())) {
                                    queryWrapper.eq(InventoryRecordAll::getUnit, vo.getUnit());
                                }


                                if(vo.getExpireDate() != null){
                                    queryWrapper.eq(InventoryRecordAll::getExpireDate, vo.getExpireDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getWarehouseName())) {
                                            queryWrapper.like(InventoryRecordAll::getWarehouseName, vo.getWarehouseName());
                                }


                                if(vo.getMinStock() != null){
                                    queryWrapper.eq(InventoryRecordAll::getMinStock, vo.getMinStock());
                                }


                            if(StringUtils.isNotEmpty(vo.getLocation())) {
                                    queryWrapper.eq(InventoryRecordAll::getLocation, vo.getLocation());
                                }


                            if(StringUtils.isNotEmpty(vo.getCategory())) {
                                    queryWrapper.eq(InventoryRecordAll::getCategory, vo.getCategory());
                                }


                            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                                    queryWrapper.eq(InventoryRecordAll::getBarcode, vo.getBarcode());
                                }


                            if(StringUtils.isNotEmpty(vo.getBrand())) {
                                    queryWrapper.eq(InventoryRecordAll::getBrand, vo.getBrand());
                                }

                // InventoryRecordAll entity = this.inventoryRecordAllService.getById(inventoryRecordId);
                    final Page<InventoryRecordAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    inventoryRecordAllService.pageNew(page, vo, queryWrapper);
                    final List<InventoryRecordAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public InventoryRecordAll getById(Serializable inventoryRecordId) {

                    LambdaQueryWrapper<InventoryRecordAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(InventoryRecordAll::getInventoryRecordId, inventoryRecordId);

                    final Page<InventoryRecordAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    inventoryRecordAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<InventoryRecordAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

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
import com.kuafu.web.entity.TransferInfoAll;
import com.kuafu.web.service.ITransferInfoAllService;
import com.kuafu.web.vo.TransferInfoAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IWarehouseInfoService;
import com.kuafu.web.entity.WarehouseInfo;
import com.kuafu.web.service.IWarehouseInfoService;
import com.kuafu.web.entity.WarehouseInfo;
import com.kuafu.web.service.IProductInfoService;
import com.kuafu.web.entity.ProductInfo;
import com.kuafu.web.service.ITransferStatusEnumService;
import com.kuafu.web.entity.TransferStatusEnum;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 调拨信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("TransferInfoAllControllerService")
@RequiredArgsConstructor
public class TransferInfoAllControllerService implements IControllerService<TransferInfoAll> {

    private final ITransferInfoAllService transferInfoAllService;





    public BaseResponse page( TransferInfoAllPageVO pageVO){
        IPage<TransferInfoAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<TransferInfoAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ti.transfer_info_id");
        LambdaQueryWrapper<TransferInfoAll> queryWrapper = objectQueryWrapper.lambda();


            if(pageVO.getTransferStatusEnumTransferStatusEnumId1() != null){
                queryWrapper.eq(TransferInfoAll::getTransferStatusEnumTransferStatusEnumId1, pageVO.getTransferStatusEnumTransferStatusEnumId1());
            }
            if(pageVO.getFromWarehouseInfoWarehouseInfoId1() != null){
                queryWrapper.eq(TransferInfoAll::getFromWarehouseInfoWarehouseInfoId1, pageVO.getFromWarehouseInfoWarehouseInfoId1());
            }
            if(pageVO.getShelfLife() != null){
                queryWrapper.eq(TransferInfoAll::getShelfLife, pageVO.getShelfLife());
            }
            if(pageVO.getApplyDate() != null){
                queryWrapper.eq(TransferInfoAll::getApplyDate, pageVO.getApplyDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(TransferInfoAll::getPassword, pageVO.getPassword());
            }
        if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
                queryWrapper.like(TransferInfoAll::getBarcode, pageVO.getBarcode());
            }
        if(StringUtils.isNotEmpty(pageVO.getBrand())) {
                queryWrapper.like(TransferInfoAll::getBrand, pageVO.getBrand());
            }
            if(pageVO.getQuantity() != null){
                queryWrapper.eq(TransferInfoAll::getQuantity, pageVO.getQuantity());
            }
            if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
                queryWrapper.eq(TransferInfoAll::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {

                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(TransferInfoAll::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getApplyUserInfoUserInfoId1() != null){
                queryWrapper.eq(TransferInfoAll::getApplyUserInfoUserInfoId1, pageVO.getApplyUserInfoUserInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
                queryWrapper.like(TransferInfoAll::getSpecification, pageVO.getSpecification());
            }
        if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                queryWrapper.like(TransferInfoAll::getSerialNumber, pageVO.getSerialNumber());
            }
            if(pageVO.getConfirmUserInfoUserInfoId2() != null){
                queryWrapper.eq(TransferInfoAll::getConfirmUserInfoUserInfoId2, pageVO.getConfirmUserInfoUserInfoId2());
            }
            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(TransferInfoAll::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getProductName())) {

                        String s_string = pageVO.getProductName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(TransferInfoAll::getProductName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getUnit())) {
                queryWrapper.like(TransferInfoAll::getUnit, pageVO.getUnit());
            }
        if(StringUtils.isNotEmpty(pageVO.getWarehouseName())) {

                        String s_string = pageVO.getWarehouseName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(TransferInfoAll::getWarehouseName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(TransferInfoAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
            if(pageVO.getToWarehouseInfoWarehouseInfoId2() != null){
                queryWrapper.eq(TransferInfoAll::getToWarehouseInfoWarehouseInfoId2, pageVO.getToWarehouseInfoWarehouseInfoId2());
            }
            if(pageVO.getConfirmDate() != null){
                queryWrapper.eq(TransferInfoAll::getConfirmDate, pageVO.getConfirmDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getLocation())) {
                queryWrapper.like(TransferInfoAll::getLocation, pageVO.getLocation());
            }
            if(pageVO.getMinStock() != null){
                queryWrapper.eq(TransferInfoAll::getMinStock, pageVO.getMinStock());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(TransferInfoAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
        if(StringUtils.isNotEmpty(pageVO.getCategory())) {
                queryWrapper.like(TransferInfoAll::getCategory, pageVO.getCategory());
            }
            if(pageVO.getTransferInfoId() != null){
                queryWrapper.eq(TransferInfoAll::getTransferInfoId, pageVO.getTransferInfoId());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(TransferInfoAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    transferInfoAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(TransferInfoAllPageVO vo){
            LambdaQueryWrapper<TransferInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getTransferStatusEnumTransferStatusEnumId1() != null){
                    queryWrapper.eq(TransferInfoAll::getTransferStatusEnumTransferStatusEnumId1, vo.getTransferStatusEnumTransferStatusEnumId1());
                }
                if(vo.getFromWarehouseInfoWarehouseInfoId1() != null){
                    queryWrapper.eq(TransferInfoAll::getFromWarehouseInfoWarehouseInfoId1, vo.getFromWarehouseInfoWarehouseInfoId1());
                }
                if(vo.getShelfLife() != null){
                    queryWrapper.eq(TransferInfoAll::getShelfLife, vo.getShelfLife());
                }
                if(vo.getApplyDate() != null){
                    queryWrapper.eq(TransferInfoAll::getApplyDate, vo.getApplyDate());
                }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(TransferInfoAll::getPassword, vo.getPassword());
                }
            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                    queryWrapper.eq(TransferInfoAll::getBarcode, vo.getBarcode());
                }
            if(StringUtils.isNotEmpty(vo.getBrand())) {
                    queryWrapper.eq(TransferInfoAll::getBrand, vo.getBrand());
                }
                if(vo.getQuantity() != null){
                    queryWrapper.eq(TransferInfoAll::getQuantity, vo.getQuantity());
                }
                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                    queryWrapper.eq(TransferInfoAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(TransferInfoAll::getStatusName, vo.getStatusName());
                }
                if(vo.getApplyUserInfoUserInfoId1() != null){
                    queryWrapper.eq(TransferInfoAll::getApplyUserInfoUserInfoId1, vo.getApplyUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                    queryWrapper.eq(TransferInfoAll::getSpecification, vo.getSpecification());
                }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                    queryWrapper.eq(TransferInfoAll::getSerialNumber, vo.getSerialNumber());
                }
                if(vo.getConfirmUserInfoUserInfoId2() != null){
                    queryWrapper.eq(TransferInfoAll::getConfirmUserInfoUserInfoId2, vo.getConfirmUserInfoUserInfoId2());
                }
                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(TransferInfoAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getProductName())) {
                            queryWrapper.like(TransferInfoAll::getProductName, vo.getProductName());
                }
            if(StringUtils.isNotEmpty(vo.getUnit())) {
                    queryWrapper.eq(TransferInfoAll::getUnit, vo.getUnit());
                }
            if(StringUtils.isNotEmpty(vo.getWarehouseName())) {
                            queryWrapper.like(TransferInfoAll::getWarehouseName, vo.getWarehouseName());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(TransferInfoAll::getRegistrationDate, vo.getRegistrationDate());
                }
                if(vo.getToWarehouseInfoWarehouseInfoId2() != null){
                    queryWrapper.eq(TransferInfoAll::getToWarehouseInfoWarehouseInfoId2, vo.getToWarehouseInfoWarehouseInfoId2());
                }
                if(vo.getConfirmDate() != null){
                    queryWrapper.eq(TransferInfoAll::getConfirmDate, vo.getConfirmDate());
                }
            if(StringUtils.isNotEmpty(vo.getLocation())) {
                    queryWrapper.eq(TransferInfoAll::getLocation, vo.getLocation());
                }
                if(vo.getMinStock() != null){
                    queryWrapper.eq(TransferInfoAll::getMinStock, vo.getMinStock());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(TransferInfoAll::getPhoneNumber, vo.getPhoneNumber());
                }
            if(StringUtils.isNotEmpty(vo.getCategory())) {
                    queryWrapper.eq(TransferInfoAll::getCategory, vo.getCategory());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(TransferInfoAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(transferInfoAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(TransferInfoAllPageVO vo) {


                LambdaQueryWrapper<TransferInfoAll> queryWrapper = new LambdaQueryWrapper<>();


                                if(vo.getTransferStatusEnumTransferStatusEnumId1() != null){
                                    queryWrapper.eq(TransferInfoAll::getTransferStatusEnumTransferStatusEnumId1, vo.getTransferStatusEnumTransferStatusEnumId1());
                                }


                                if(vo.getFromWarehouseInfoWarehouseInfoId1() != null){
                                    queryWrapper.eq(TransferInfoAll::getFromWarehouseInfoWarehouseInfoId1, vo.getFromWarehouseInfoWarehouseInfoId1());
                                }


                                if(vo.getShelfLife() != null){
                                    queryWrapper.eq(TransferInfoAll::getShelfLife, vo.getShelfLife());
                                }


                                if(vo.getApplyDate() != null){
                                    queryWrapper.eq(TransferInfoAll::getApplyDate, vo.getApplyDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(TransferInfoAll::getPassword, vo.getPassword());
                                }


                            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                                    queryWrapper.eq(TransferInfoAll::getBarcode, vo.getBarcode());
                                }


                            if(StringUtils.isNotEmpty(vo.getBrand())) {
                                    queryWrapper.eq(TransferInfoAll::getBrand, vo.getBrand());
                                }


                                if(vo.getQuantity() != null){
                                    queryWrapper.eq(TransferInfoAll::getQuantity, vo.getQuantity());
                                }


                                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                                    queryWrapper.eq(TransferInfoAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                                            queryWrapper.like(TransferInfoAll::getStatusName, vo.getStatusName());
                                }


                                if(vo.getApplyUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(TransferInfoAll::getApplyUserInfoUserInfoId1, vo.getApplyUserInfoUserInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                                    queryWrapper.eq(TransferInfoAll::getSpecification, vo.getSpecification());
                                }


                            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                                    queryWrapper.eq(TransferInfoAll::getSerialNumber, vo.getSerialNumber());
                                }


                                if(vo.getConfirmUserInfoUserInfoId2() != null){
                                    queryWrapper.eq(TransferInfoAll::getConfirmUserInfoUserInfoId2, vo.getConfirmUserInfoUserInfoId2());
                                }


                                if(vo.getProductInfoProductInfoId1() != null){
                                    queryWrapper.eq(TransferInfoAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getProductName())) {
                                            queryWrapper.like(TransferInfoAll::getProductName, vo.getProductName());
                                }


                            if(StringUtils.isNotEmpty(vo.getUnit())) {
                                    queryWrapper.eq(TransferInfoAll::getUnit, vo.getUnit());
                                }


                            if(StringUtils.isNotEmpty(vo.getWarehouseName())) {
                                            queryWrapper.like(TransferInfoAll::getWarehouseName, vo.getWarehouseName());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(TransferInfoAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                                if(vo.getToWarehouseInfoWarehouseInfoId2() != null){
                                    queryWrapper.eq(TransferInfoAll::getToWarehouseInfoWarehouseInfoId2, vo.getToWarehouseInfoWarehouseInfoId2());
                                }


                                if(vo.getConfirmDate() != null){
                                    queryWrapper.eq(TransferInfoAll::getConfirmDate, vo.getConfirmDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getLocation())) {
                                    queryWrapper.eq(TransferInfoAll::getLocation, vo.getLocation());
                                }


                                if(vo.getMinStock() != null){
                                    queryWrapper.eq(TransferInfoAll::getMinStock, vo.getMinStock());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(TransferInfoAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                            if(StringUtils.isNotEmpty(vo.getCategory())) {
                                    queryWrapper.eq(TransferInfoAll::getCategory, vo.getCategory());
                                }


                                if(vo.getTransferInfoId() != null){
                                    queryWrapper.eq(TransferInfoAll::getTransferInfoId, vo.getTransferInfoId());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(TransferInfoAll::getUsername, vo.getUsername());
                                }

                // TransferInfoAll entity = this.transferInfoAllService.getById(transferInfoId);
                    final Page<TransferInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    transferInfoAllService.pageNew(page, vo, queryWrapper);
                    final List<TransferInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public TransferInfoAll getById(Serializable transferInfoId) {

                    LambdaQueryWrapper<TransferInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(TransferInfoAll::getTransferInfoId, transferInfoId);

                    final Page<TransferInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    transferInfoAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<TransferInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

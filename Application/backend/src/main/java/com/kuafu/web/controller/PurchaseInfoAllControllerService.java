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
import com.kuafu.web.entity.PurchaseInfoAll;
import com.kuafu.web.service.IPurchaseInfoAllService;
import com.kuafu.web.vo.PurchaseInfoAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IPurchaseStatusEnumService;
import com.kuafu.web.entity.PurchaseStatusEnum;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 采购信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("PurchaseInfoAllControllerService")
@RequiredArgsConstructor
public class PurchaseInfoAllControllerService implements IControllerService<PurchaseInfoAll> {

    private final IPurchaseInfoAllService purchaseInfoAllService;





    public BaseResponse page( PurchaseInfoAllPageVO pageVO){
        IPage<PurchaseInfoAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<PurchaseInfoAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("pi.purchase_info_id");
        LambdaQueryWrapper<PurchaseInfoAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getPurchaseNo())) {
                queryWrapper.like(PurchaseInfoAll::getPurchaseNo, pageVO.getPurchaseNo());
            }
            if(pageVO.getPurchaseInfoId() != null){
                queryWrapper.eq(PurchaseInfoAll::getPurchaseInfoId, pageVO.getPurchaseInfoId());
            }
            if(pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
                queryWrapper.eq(PurchaseInfoAll::getPurchaseStatusEnumPurchaseStatusEnumId1, pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(PurchaseInfoAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(PurchaseInfoAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(PurchaseInfoAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
            if(pageVO.getTotalAmount() != null){
                queryWrapper.eq(PurchaseInfoAll::getTotalAmount, pageVO.getTotalAmount());
            }
        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {

                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(PurchaseInfoAll::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getSupplier())) {
                queryWrapper.like(PurchaseInfoAll::getSupplier, pageVO.getSupplier());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(PurchaseInfoAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
            if(pageVO.getPurchaseDate() != null){
                queryWrapper.eq(PurchaseInfoAll::getPurchaseDate, pageVO.getPurchaseDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(PurchaseInfoAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    purchaseInfoAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(PurchaseInfoAllPageVO vo){
            LambdaQueryWrapper<PurchaseInfoAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPurchaseNo())) {
                    queryWrapper.eq(PurchaseInfoAll::getPurchaseNo, vo.getPurchaseNo());
                }
                if(vo.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
                    queryWrapper.eq(PurchaseInfoAll::getPurchaseStatusEnumPurchaseStatusEnumId1, vo.getPurchaseStatusEnumPurchaseStatusEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(PurchaseInfoAll::getPassword, vo.getPassword());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(PurchaseInfoAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(PurchaseInfoAll::getRegistrationDate, vo.getRegistrationDate());
                }
                if(vo.getTotalAmount() != null){
                    queryWrapper.eq(PurchaseInfoAll::getTotalAmount, vo.getTotalAmount());
                }
            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(PurchaseInfoAll::getStatusName, vo.getStatusName());
                }
            if(StringUtils.isNotEmpty(vo.getSupplier())) {
                    queryWrapper.eq(PurchaseInfoAll::getSupplier, vo.getSupplier());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(PurchaseInfoAll::getPhoneNumber, vo.getPhoneNumber());
                }
                if(vo.getPurchaseDate() != null){
                    queryWrapper.eq(PurchaseInfoAll::getPurchaseDate, vo.getPurchaseDate());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(PurchaseInfoAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(purchaseInfoAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(PurchaseInfoAllPageVO vo) {


                LambdaQueryWrapper<PurchaseInfoAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getPurchaseNo())) {
                                    queryWrapper.eq(PurchaseInfoAll::getPurchaseNo, vo.getPurchaseNo());
                                }


                                if(vo.getPurchaseInfoId() != null){
                                    queryWrapper.eq(PurchaseInfoAll::getPurchaseInfoId, vo.getPurchaseInfoId());
                                }


                                if(vo.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
                                    queryWrapper.eq(PurchaseInfoAll::getPurchaseStatusEnumPurchaseStatusEnumId1, vo.getPurchaseStatusEnumPurchaseStatusEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(PurchaseInfoAll::getPassword, vo.getPassword());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(PurchaseInfoAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(PurchaseInfoAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                                if(vo.getTotalAmount() != null){
                                    queryWrapper.eq(PurchaseInfoAll::getTotalAmount, vo.getTotalAmount());
                                }


                            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                                            queryWrapper.like(PurchaseInfoAll::getStatusName, vo.getStatusName());
                                }


                            if(StringUtils.isNotEmpty(vo.getSupplier())) {
                                    queryWrapper.eq(PurchaseInfoAll::getSupplier, vo.getSupplier());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(PurchaseInfoAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                                if(vo.getPurchaseDate() != null){
                                    queryWrapper.eq(PurchaseInfoAll::getPurchaseDate, vo.getPurchaseDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(PurchaseInfoAll::getUsername, vo.getUsername());
                                }

                // PurchaseInfoAll entity = this.purchaseInfoAllService.getById(purchaseInfoId);
                    final Page<PurchaseInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    purchaseInfoAllService.pageNew(page, vo, queryWrapper);
                    final List<PurchaseInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public PurchaseInfoAll getById(Serializable purchaseInfoId) {

                    LambdaQueryWrapper<PurchaseInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(PurchaseInfoAll::getPurchaseInfoId, purchaseInfoId);

                    final Page<PurchaseInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    purchaseInfoAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<PurchaseInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

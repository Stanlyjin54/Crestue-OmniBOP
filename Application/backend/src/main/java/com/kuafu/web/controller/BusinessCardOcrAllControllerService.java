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
import com.kuafu.web.entity.BusinessCardOcrAll;
import com.kuafu.web.service.IBusinessCardOcrAllService;
import com.kuafu.web.vo.BusinessCardOcrAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.ICustomerInfoService;
import com.kuafu.web.entity.CustomerInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 名片OCR建档 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("BusinessCardOcrAllControllerService")
@RequiredArgsConstructor
public class BusinessCardOcrAllControllerService implements IControllerService<BusinessCardOcrAll> {

    private final IBusinessCardOcrAllService businessCardOcrAllService;





    public BaseResponse page( BusinessCardOcrAllPageVO pageVO){
        IPage<BusinessCardOcrAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<BusinessCardOcrAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("bco.business_card_ocr_id");
        LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getWechatId())) {
                queryWrapper.like(BusinessCardOcrAll::getWechatId, pageVO.getWechatId());
            }
        if(StringUtils.isNotEmpty(pageVO.getAddress())) {
                queryWrapper.like(BusinessCardOcrAll::getAddress, pageVO.getAddress());
            }
            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(BusinessCardOcrAll::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhone())) {
                queryWrapper.like(BusinessCardOcrAll::getPhone, pageVO.getPhone());
            }
        if(StringUtils.isNotEmpty(pageVO.getWechatDataEncrypted())) {
                queryWrapper.like(BusinessCardOcrAll::getWechatDataEncrypted, pageVO.getWechatDataEncrypted());
            }
            if(pageVO.getBusinessCardOcrId() != null){
                queryWrapper.eq(BusinessCardOcrAll::getBusinessCardOcrId, pageVO.getBusinessCardOcrId());
            }
            if(pageVO.getRfmLevelEnumRfmLevelEnumId1() != null){
                queryWrapper.eq(BusinessCardOcrAll::getRfmLevelEnumRfmLevelEnumId1, pageVO.getRfmLevelEnumRfmLevelEnumId1());
            }
            if(pageVO.getCustomerTagEnumCustomerTagEnumId1() != null){
                queryWrapper.eq(BusinessCardOcrAll::getCustomerTagEnumCustomerTagEnumId1, pageVO.getCustomerTagEnumCustomerTagEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getCustomerName())) {

                        String s_string = pageVO.getCustomerName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(BusinessCardOcrAll::getCustomerName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getSource())) {
                queryWrapper.like(BusinessCardOcrAll::getSource, pageVO.getSource());
            }
        if(StringUtils.isNotEmpty(pageVO.getOcrResult())) {
                queryWrapper.like(BusinessCardOcrAll::getOcrResult, pageVO.getOcrResult());
            }
        if(StringUtils.isNotEmpty(pageVO.getEmail())) {
                queryWrapper.like(BusinessCardOcrAll::getEmail, pageVO.getEmail());
            }

                    businessCardOcrAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(BusinessCardOcrAllPageVO vo){
            LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                    queryWrapper.eq(BusinessCardOcrAll::getWechatId, vo.getWechatId());
                }
            if(StringUtils.isNotEmpty(vo.getAddress())) {
                    queryWrapper.eq(BusinessCardOcrAll::getAddress, vo.getAddress());
                }
                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(BusinessCardOcrAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getPhone())) {
                    queryWrapper.eq(BusinessCardOcrAll::getPhone, vo.getPhone());
                }
            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                    queryWrapper.eq(BusinessCardOcrAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                }
                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                    queryWrapper.eq(BusinessCardOcrAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                }
                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                    queryWrapper.eq(BusinessCardOcrAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                            queryWrapper.like(BusinessCardOcrAll::getCustomerName, vo.getCustomerName());
                }
            if(StringUtils.isNotEmpty(vo.getSource())) {
                    queryWrapper.eq(BusinessCardOcrAll::getSource, vo.getSource());
                }
            if(StringUtils.isNotEmpty(vo.getOcrResult())) {
                    queryWrapper.eq(BusinessCardOcrAll::getOcrResult, vo.getOcrResult());
                }
            if(StringUtils.isNotEmpty(vo.getEmail())) {
                    queryWrapper.eq(BusinessCardOcrAll::getEmail, vo.getEmail());
                }
                return ResultUtils.success(businessCardOcrAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(BusinessCardOcrAllPageVO vo) {


                LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                                    queryWrapper.eq(BusinessCardOcrAll::getWechatId, vo.getWechatId());
                                }


                            if(StringUtils.isNotEmpty(vo.getAddress())) {
                                    queryWrapper.eq(BusinessCardOcrAll::getAddress, vo.getAddress());
                                }


                                if(vo.getCustomerInfoCustomerInfoId1() != null){
                                    queryWrapper.eq(BusinessCardOcrAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhone())) {
                                    queryWrapper.eq(BusinessCardOcrAll::getPhone, vo.getPhone());
                                }


                            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                                    queryWrapper.eq(BusinessCardOcrAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                                }


                                if(vo.getBusinessCardOcrId() != null){
                                    queryWrapper.eq(BusinessCardOcrAll::getBusinessCardOcrId, vo.getBusinessCardOcrId());
                                }


                                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                                    queryWrapper.eq(BusinessCardOcrAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                                }


                                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                                    queryWrapper.eq(BusinessCardOcrAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                                            queryWrapper.like(BusinessCardOcrAll::getCustomerName, vo.getCustomerName());
                                }


                            if(StringUtils.isNotEmpty(vo.getSource())) {
                                    queryWrapper.eq(BusinessCardOcrAll::getSource, vo.getSource());
                                }


                            if(StringUtils.isNotEmpty(vo.getOcrResult())) {
                                    queryWrapper.eq(BusinessCardOcrAll::getOcrResult, vo.getOcrResult());
                                }


                            if(StringUtils.isNotEmpty(vo.getEmail())) {
                                    queryWrapper.eq(BusinessCardOcrAll::getEmail, vo.getEmail());
                                }

                // BusinessCardOcrAll entity = this.businessCardOcrAllService.getById(businessCardOcrId);
                    final Page<BusinessCardOcrAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    businessCardOcrAllService.pageNew(page, vo, queryWrapper);
                    final List<BusinessCardOcrAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public BusinessCardOcrAll getById(Serializable businessCardOcrId) {

                    LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(BusinessCardOcrAll::getBusinessCardOcrId, businessCardOcrId);

                    final Page<BusinessCardOcrAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    businessCardOcrAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<BusinessCardOcrAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

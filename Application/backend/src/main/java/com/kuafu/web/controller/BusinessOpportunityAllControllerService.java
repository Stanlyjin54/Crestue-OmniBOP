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
import com.kuafu.web.entity.BusinessOpportunityAll;
import com.kuafu.web.service.IBusinessOpportunityAllService;
import com.kuafu.web.vo.BusinessOpportunityAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.ICustomerInfoService;
import com.kuafu.web.entity.CustomerInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 商机线索 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("BusinessOpportunityAllControllerService")
@RequiredArgsConstructor
public class BusinessOpportunityAllControllerService implements IControllerService<BusinessOpportunityAll> {

    private final IBusinessOpportunityAllService businessOpportunityAllService;





    public BaseResponse page( BusinessOpportunityAllPageVO pageVO){
        IPage<BusinessOpportunityAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<BusinessOpportunityAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("bo.business_opportunity_id");
        LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getWechatId())) {
                queryWrapper.like(BusinessOpportunityAll::getWechatId, pageVO.getWechatId());
            }
        if(StringUtils.isNotEmpty(pageVO.getFollowUpSuggestion())) {
                queryWrapper.like(BusinessOpportunityAll::getFollowUpSuggestion, pageVO.getFollowUpSuggestion());
            }
        if(StringUtils.isNotEmpty(pageVO.getAddress())) {
                queryWrapper.like(BusinessOpportunityAll::getAddress, pageVO.getAddress());
            }
            if(pageVO.getBusinessOpportunityId() != null){
                queryWrapper.eq(BusinessOpportunityAll::getBusinessOpportunityId, pageVO.getBusinessOpportunityId());
            }
        if(StringUtils.isNotEmpty(pageVO.getSource())) {
                queryWrapper.like(BusinessOpportunityAll::getSource, pageVO.getSource());
            }
            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(BusinessOpportunityAll::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhone())) {
                queryWrapper.like(BusinessOpportunityAll::getPhone, pageVO.getPhone());
            }
        if(StringUtils.isNotEmpty(pageVO.getWechatDataEncrypted())) {
                queryWrapper.like(BusinessOpportunityAll::getWechatDataEncrypted, pageVO.getWechatDataEncrypted());
            }
        if(StringUtils.isNotEmpty(pageVO.getOpportunityName())) {

                        String s_string = pageVO.getOpportunityName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(BusinessOpportunityAll::getOpportunityName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getRfmLevelEnumRfmLevelEnumId1() != null){
                queryWrapper.eq(BusinessOpportunityAll::getRfmLevelEnumRfmLevelEnumId1, pageVO.getRfmLevelEnumRfmLevelEnumId1());
            }
            if(pageVO.getCustomerTagEnumCustomerTagEnumId1() != null){
                queryWrapper.eq(BusinessOpportunityAll::getCustomerTagEnumCustomerTagEnumId1, pageVO.getCustomerTagEnumCustomerTagEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getCustomerName())) {

                        String s_string = pageVO.getCustomerName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(BusinessOpportunityAll::getCustomerName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getEmail())) {
                queryWrapper.like(BusinessOpportunityAll::getEmail, pageVO.getEmail());
            }
        if(StringUtils.isNotEmpty(pageVO.getStatus())) {
                queryWrapper.like(BusinessOpportunityAll::getStatus, pageVO.getStatus());
            }

                    businessOpportunityAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(BusinessOpportunityAllPageVO vo){
            LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                    queryWrapper.eq(BusinessOpportunityAll::getWechatId, vo.getWechatId());
                }
            if(StringUtils.isNotEmpty(vo.getFollowUpSuggestion())) {
                    queryWrapper.eq(BusinessOpportunityAll::getFollowUpSuggestion, vo.getFollowUpSuggestion());
                }
            if(StringUtils.isNotEmpty(vo.getAddress())) {
                    queryWrapper.eq(BusinessOpportunityAll::getAddress, vo.getAddress());
                }
            if(StringUtils.isNotEmpty(vo.getSource())) {
                    queryWrapper.eq(BusinessOpportunityAll::getSource, vo.getSource());
                }
                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(BusinessOpportunityAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getPhone())) {
                    queryWrapper.eq(BusinessOpportunityAll::getPhone, vo.getPhone());
                }
            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                    queryWrapper.eq(BusinessOpportunityAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                }
            if(StringUtils.isNotEmpty(vo.getOpportunityName())) {
                            queryWrapper.like(BusinessOpportunityAll::getOpportunityName, vo.getOpportunityName());
                }
                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                    queryWrapper.eq(BusinessOpportunityAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                }
                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                    queryWrapper.eq(BusinessOpportunityAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                            queryWrapper.like(BusinessOpportunityAll::getCustomerName, vo.getCustomerName());
                }
            if(StringUtils.isNotEmpty(vo.getEmail())) {
                    queryWrapper.eq(BusinessOpportunityAll::getEmail, vo.getEmail());
                }
            if(StringUtils.isNotEmpty(vo.getStatus())) {
                    queryWrapper.eq(BusinessOpportunityAll::getStatus, vo.getStatus());
                }
                return ResultUtils.success(businessOpportunityAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(BusinessOpportunityAllPageVO vo) {


                LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getWechatId, vo.getWechatId());
                                }


                            if(StringUtils.isNotEmpty(vo.getFollowUpSuggestion())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getFollowUpSuggestion, vo.getFollowUpSuggestion());
                                }


                            if(StringUtils.isNotEmpty(vo.getAddress())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getAddress, vo.getAddress());
                                }


                                if(vo.getBusinessOpportunityId() != null){
                                    queryWrapper.eq(BusinessOpportunityAll::getBusinessOpportunityId, vo.getBusinessOpportunityId());
                                }


                            if(StringUtils.isNotEmpty(vo.getSource())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getSource, vo.getSource());
                                }


                                if(vo.getCustomerInfoCustomerInfoId1() != null){
                                    queryWrapper.eq(BusinessOpportunityAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhone())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getPhone, vo.getPhone());
                                }


                            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                                }


                            if(StringUtils.isNotEmpty(vo.getOpportunityName())) {
                                            queryWrapper.like(BusinessOpportunityAll::getOpportunityName, vo.getOpportunityName());
                                }


                                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                                    queryWrapper.eq(BusinessOpportunityAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                                }


                                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                                    queryWrapper.eq(BusinessOpportunityAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                                            queryWrapper.like(BusinessOpportunityAll::getCustomerName, vo.getCustomerName());
                                }


                            if(StringUtils.isNotEmpty(vo.getEmail())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getEmail, vo.getEmail());
                                }


                            if(StringUtils.isNotEmpty(vo.getStatus())) {
                                    queryWrapper.eq(BusinessOpportunityAll::getStatus, vo.getStatus());
                                }

                // BusinessOpportunityAll entity = this.businessOpportunityAllService.getById(businessOpportunityId);
                    final Page<BusinessOpportunityAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    businessOpportunityAllService.pageNew(page, vo, queryWrapper);
                    final List<BusinessOpportunityAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public BusinessOpportunityAll getById(Serializable businessOpportunityId) {

                    LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(BusinessOpportunityAll::getBusinessOpportunityId, businessOpportunityId);

                    final Page<BusinessOpportunityAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    businessOpportunityAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<BusinessOpportunityAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

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
import com.kuafu.web.entity.CustomerInfoAll;
import com.kuafu.web.service.ICustomerInfoAllService;
import com.kuafu.web.vo.CustomerInfoAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IRfmLevelEnumService;
import com.kuafu.web.entity.RfmLevelEnum;
import com.kuafu.web.service.ICustomerTagEnumService;
import com.kuafu.web.entity.CustomerTagEnum;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 客户信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("CustomerInfoAllControllerService")
@RequiredArgsConstructor
public class CustomerInfoAllControllerService implements IControllerService<CustomerInfoAll> {

    private final ICustomerInfoAllService customerInfoAllService;





    public BaseResponse page( CustomerInfoAllPageVO pageVO){
        IPage<CustomerInfoAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<CustomerInfoAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ci.customer_info_id");
        LambdaQueryWrapper<CustomerInfoAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getWechatId())) {
                queryWrapper.like(CustomerInfoAll::getWechatId, pageVO.getWechatId());
            }
        if(StringUtils.isNotEmpty(pageVO.getAddress())) {
                queryWrapper.like(CustomerInfoAll::getAddress, pageVO.getAddress());
            }
            if(pageVO.getCustomerInfoId() != null){
                queryWrapper.eq(CustomerInfoAll::getCustomerInfoId, pageVO.getCustomerInfoId());
            }
        if(StringUtils.isNotEmpty(pageVO.getTagName())) {

                        String s_string = pageVO.getTagName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(CustomerInfoAll::getTagName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getSource())) {
                queryWrapper.like(CustomerInfoAll::getSource, pageVO.getSource());
            }
        if(StringUtils.isNotEmpty(pageVO.getLevelName())) {

                        String s_string = pageVO.getLevelName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(CustomerInfoAll::getLevelName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getPhone())) {
                queryWrapper.like(CustomerInfoAll::getPhone, pageVO.getPhone());
            }
        if(StringUtils.isNotEmpty(pageVO.getWechatDataEncrypted())) {
                queryWrapper.like(CustomerInfoAll::getWechatDataEncrypted, pageVO.getWechatDataEncrypted());
            }
            if(pageVO.getIsAiGenerated() != null){
                queryWrapper.eq(CustomerInfoAll::getIsAiGenerated, pageVO.getIsAiGenerated());
            }
            if(pageVO.getRfmLevelEnumRfmLevelEnumId1() != null){
                queryWrapper.eq(CustomerInfoAll::getRfmLevelEnumRfmLevelEnumId1, pageVO.getRfmLevelEnumRfmLevelEnumId1());
            }
            if(pageVO.getCustomerTagEnumCustomerTagEnumId1() != null){
                queryWrapper.eq(CustomerInfoAll::getCustomerTagEnumCustomerTagEnumId1, pageVO.getCustomerTagEnumCustomerTagEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getCustomerName())) {

                        String s_string = pageVO.getCustomerName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(CustomerInfoAll::getCustomerName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getEmail())) {
                queryWrapper.like(CustomerInfoAll::getEmail, pageVO.getEmail());
            }

                    customerInfoAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(CustomerInfoAllPageVO vo){
            LambdaQueryWrapper<CustomerInfoAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                    queryWrapper.eq(CustomerInfoAll::getWechatId, vo.getWechatId());
                }
            if(StringUtils.isNotEmpty(vo.getAddress())) {
                    queryWrapper.eq(CustomerInfoAll::getAddress, vo.getAddress());
                }
            if(StringUtils.isNotEmpty(vo.getTagName())) {
                            queryWrapper.like(CustomerInfoAll::getTagName, vo.getTagName());
                }
            if(StringUtils.isNotEmpty(vo.getSource())) {
                    queryWrapper.eq(CustomerInfoAll::getSource, vo.getSource());
                }
            if(StringUtils.isNotEmpty(vo.getLevelName())) {
                            queryWrapper.like(CustomerInfoAll::getLevelName, vo.getLevelName());
                }
            if(StringUtils.isNotEmpty(vo.getPhone())) {
                    queryWrapper.eq(CustomerInfoAll::getPhone, vo.getPhone());
                }
            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                    queryWrapper.eq(CustomerInfoAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                }
                if(vo.getIsAiGenerated() != null){
                    queryWrapper.eq(CustomerInfoAll::getIsAiGenerated, vo.getIsAiGenerated());
                }
                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                    queryWrapper.eq(CustomerInfoAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                }
                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                    queryWrapper.eq(CustomerInfoAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                            queryWrapper.like(CustomerInfoAll::getCustomerName, vo.getCustomerName());
                }
            if(StringUtils.isNotEmpty(vo.getEmail())) {
                    queryWrapper.eq(CustomerInfoAll::getEmail, vo.getEmail());
                }
                return ResultUtils.success(customerInfoAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(CustomerInfoAllPageVO vo) {


                LambdaQueryWrapper<CustomerInfoAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                                    queryWrapper.eq(CustomerInfoAll::getWechatId, vo.getWechatId());
                                }


                            if(StringUtils.isNotEmpty(vo.getAddress())) {
                                    queryWrapper.eq(CustomerInfoAll::getAddress, vo.getAddress());
                                }


                                if(vo.getCustomerInfoId() != null){
                                    queryWrapper.eq(CustomerInfoAll::getCustomerInfoId, vo.getCustomerInfoId());
                                }


                            if(StringUtils.isNotEmpty(vo.getTagName())) {
                                            queryWrapper.like(CustomerInfoAll::getTagName, vo.getTagName());
                                }


                            if(StringUtils.isNotEmpty(vo.getSource())) {
                                    queryWrapper.eq(CustomerInfoAll::getSource, vo.getSource());
                                }


                            if(StringUtils.isNotEmpty(vo.getLevelName())) {
                                            queryWrapper.like(CustomerInfoAll::getLevelName, vo.getLevelName());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhone())) {
                                    queryWrapper.eq(CustomerInfoAll::getPhone, vo.getPhone());
                                }


                            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                                    queryWrapper.eq(CustomerInfoAll::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                                }


                                if(vo.getIsAiGenerated() != null){
                                    queryWrapper.eq(CustomerInfoAll::getIsAiGenerated, vo.getIsAiGenerated());
                                }


                                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                                    queryWrapper.eq(CustomerInfoAll::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                                }


                                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                                    queryWrapper.eq(CustomerInfoAll::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                                            queryWrapper.like(CustomerInfoAll::getCustomerName, vo.getCustomerName());
                                }


                            if(StringUtils.isNotEmpty(vo.getEmail())) {
                                    queryWrapper.eq(CustomerInfoAll::getEmail, vo.getEmail());
                                }

                // CustomerInfoAll entity = this.customerInfoAllService.getById(customerInfoId);
                    final Page<CustomerInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    customerInfoAllService.pageNew(page, vo, queryWrapper);
                    final List<CustomerInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public CustomerInfoAll getById(Serializable customerInfoId) {

                    LambdaQueryWrapper<CustomerInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(CustomerInfoAll::getCustomerInfoId, customerInfoId);

                    final Page<CustomerInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    customerInfoAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<CustomerInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

package com.kuafu.web.controller;

import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.resource.service.IStaticResourceService;
import com.kuafu.common.event.EventVo;
import com.kuafu.common.event.MyEventService;
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
import org.springframework.transaction.support.TransactionTemplate;
import com.kuafu.common.sensitive.SensitiveFilter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.CustomerInfo;
import com.kuafu.web.service.ICustomerInfoService;
import com.kuafu.web.vo.CustomerInfoPageVO;
import com.kuafu.web.vo.CustomerInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 客户信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("CustomerInfoControllerService")
@RequiredArgsConstructor
public class CustomerInfoControllerService  {

    private final ICustomerInfoService customerInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( CustomerInfoPageVO pageVO){
        IPage<CustomerInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<CustomerInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<CustomerInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(CustomerInfo::getCustomerInfoId);

            if(pageVO.getCustomerInfoId() != null){
                queryWrapper.eq(CustomerInfo::getCustomerInfoId, pageVO.getCustomerInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getCustomerName())) {
//                        queryWrapper.like(CustomerInfo::getCustomerName, pageVO.getCustomerName());
                        String s_string = pageVO.getCustomerName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(CustomerInfo::getCustomerName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getPhone())) {
                queryWrapper.eq(CustomerInfo::getPhone, pageVO.getPhone());
            }


        if(StringUtils.isNotEmpty(pageVO.getEmail())) {
                queryWrapper.eq(CustomerInfo::getEmail, pageVO.getEmail());
            }


        if(StringUtils.isNotEmpty(pageVO.getAddress())) {
                queryWrapper.eq(CustomerInfo::getAddress, pageVO.getAddress());
            }


            if(pageVO.getRfmLevelEnumRfmLevelEnumId1() != null){
                queryWrapper.eq(CustomerInfo::getRfmLevelEnumRfmLevelEnumId1, pageVO.getRfmLevelEnumRfmLevelEnumId1());
            }


            if(pageVO.getCustomerTagEnumCustomerTagEnumId1() != null){
                queryWrapper.eq(CustomerInfo::getCustomerTagEnumCustomerTagEnumId1, pageVO.getCustomerTagEnumCustomerTagEnumId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getWechatId())) {
                queryWrapper.eq(CustomerInfo::getWechatId, pageVO.getWechatId());
            }


        if(StringUtils.isNotEmpty(pageVO.getWechatDataEncrypted())) {
                queryWrapper.eq(CustomerInfo::getWechatDataEncrypted, pageVO.getWechatDataEncrypted());
            }


        if(StringUtils.isNotEmpty(pageVO.getSource())) {
                queryWrapper.eq(CustomerInfo::getSource, pageVO.getSource());
            }

            return ResultUtils.success(customerInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  CustomerInfoVO vo){
            LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getCustomerName())) {
                            queryWrapper.like(CustomerInfo::getCustomerName, vo.getCustomerName());
                }
            if(StringUtils.isNotEmpty(vo.getPhone())) {
                    queryWrapper.eq(CustomerInfo::getPhone, vo.getPhone());
                }
            if(StringUtils.isNotEmpty(vo.getEmail())) {
                    queryWrapper.eq(CustomerInfo::getEmail, vo.getEmail());
                }
            if(StringUtils.isNotEmpty(vo.getAddress())) {
                    queryWrapper.eq(CustomerInfo::getAddress, vo.getAddress());
                }
                if(vo.getRfmLevelEnumRfmLevelEnumId1() != null){
                    queryWrapper.eq(CustomerInfo::getRfmLevelEnumRfmLevelEnumId1, vo.getRfmLevelEnumRfmLevelEnumId1());
                }
                if(vo.getCustomerTagEnumCustomerTagEnumId1() != null){
                    queryWrapper.eq(CustomerInfo::getCustomerTagEnumCustomerTagEnumId1, vo.getCustomerTagEnumCustomerTagEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getWechatId())) {
                    queryWrapper.eq(CustomerInfo::getWechatId, vo.getWechatId());
                }
            if(StringUtils.isNotEmpty(vo.getWechatDataEncrypted())) {
                    queryWrapper.eq(CustomerInfo::getWechatDataEncrypted, vo.getWechatDataEncrypted());
                }
            if(StringUtils.isNotEmpty(vo.getSource())) {
                    queryWrapper.eq(CustomerInfo::getSource, vo.getSource());
                }
                return ResultUtils.success(customerInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  CustomerInfoVO vo) {
                CustomerInfo entity =  CustomerInfo.builder()
                    .customerName(vo.getCustomerName())
                    .phone(vo.getPhone())
                    .email(vo.getEmail())
                    .address(vo.getAddress())
                    .rfmLevelEnumRfmLevelEnumId1(vo.getRfmLevelEnumRfmLevelEnumId1())
                    .customerTagEnumCustomerTagEnumId1(vo.getCustomerTagEnumCustomerTagEnumId1())
                    .wechatId(vo.getWechatId())
                    .wechatDataEncrypted(vo.getWechatDataEncrypted())
                    .source(vo.getSource())
                .build();








                boolean flag =this.customerInfoService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("customer_info").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getCustomerInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( CustomerInfoVO vo) {
                CustomerInfo entity =  CustomerInfo.builder()
                    .customerInfoId(vo.getCustomerInfoId())
                    .customerName(vo.getCustomerName())
                    .phone(vo.getPhone())
                    .email(vo.getEmail())
                    .address(vo.getAddress())
                    .rfmLevelEnumRfmLevelEnumId1(vo.getRfmLevelEnumRfmLevelEnumId1())
                    .customerTagEnumCustomerTagEnumId1(vo.getCustomerTagEnumCustomerTagEnumId1())
                    .wechatId(vo.getWechatId())
                    .wechatDataEncrypted(vo.getWechatDataEncrypted())
                    .source(vo.getSource())
                .build();



                        boolean flag = this.customerInfoService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getCustomerInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( CustomerInfoVO vo ) {
                CustomerInfo entity = this.customerInfoService.getById(vo.getCustomerInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( CustomerInfoVO vo) {
                boolean flag = this.customerInfoService.removeById(vo.getCustomerInfoId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

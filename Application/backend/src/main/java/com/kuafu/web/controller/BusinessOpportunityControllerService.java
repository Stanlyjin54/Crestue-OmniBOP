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
import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.service.IBusinessOpportunityService;
import com.kuafu.web.vo.BusinessOpportunityPageVO;
import com.kuafu.web.vo.BusinessOpportunityVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import  com.kuafu.common.ai_agent.publisher.AIAgentPublisher;

/**
 * <p> 商机线索 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("BusinessOpportunityControllerService")
@RequiredArgsConstructor
public class BusinessOpportunityControllerService  {

    private final IBusinessOpportunityService businessOpportunityService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;


    private final AIAgentPublisher aiAgentPublisher;

    public BaseResponse page( BusinessOpportunityPageVO pageVO){
        IPage<BusinessOpportunity> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<BusinessOpportunity> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(BusinessOpportunity::getBusinessOpportunityId);

            if(pageVO.getBusinessOpportunityId() != null){
                queryWrapper.eq(BusinessOpportunity::getBusinessOpportunityId, pageVO.getBusinessOpportunityId());
            }


            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(BusinessOpportunity::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getOpportunityName())) {
//                        queryWrapper.like(BusinessOpportunity::getOpportunityName, pageVO.getOpportunityName());
                        String s_string = pageVO.getOpportunityName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(BusinessOpportunity::getOpportunityName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getStatus())) {
                queryWrapper.eq(BusinessOpportunity::getStatus, pageVO.getStatus());
            }


        if(StringUtils.isNotEmpty(pageVO.getFollowUpSuggestion())) {
                queryWrapper.eq(BusinessOpportunity::getFollowUpSuggestion, pageVO.getFollowUpSuggestion());
            }

            return ResultUtils.success(businessOpportunityService.page(page, queryWrapper));
        }


        public BaseResponse list(  BusinessOpportunityVO vo){
            LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(BusinessOpportunity::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getOpportunityName())) {
                            queryWrapper.like(BusinessOpportunity::getOpportunityName, vo.getOpportunityName());
                }
            if(StringUtils.isNotEmpty(vo.getStatus())) {
                    queryWrapper.eq(BusinessOpportunity::getStatus, vo.getStatus());
                }
            if(StringUtils.isNotEmpty(vo.getFollowUpSuggestion())) {
                    queryWrapper.eq(BusinessOpportunity::getFollowUpSuggestion, vo.getFollowUpSuggestion());
                }
                return ResultUtils.success(businessOpportunityService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  BusinessOpportunityVO vo) {
                BusinessOpportunity entity =  BusinessOpportunity.builder()
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .opportunityName(vo.getOpportunityName())
                    .status(vo.getStatus())
                    .followUpSuggestion(vo.getFollowUpSuggestion())
                .build();








                boolean flag =this.businessOpportunityService.save(entity);





                aiAgentPublisher.publishEvent("BusinessOpportunity",String.valueOf(entity.getBusinessOpportunityId()),
                        "business_opportunity_id","add","follow_up_suggestion","你是销售专家，请根据客户和商机状态给出专业的跟进建议，直接输出",entity);

                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("business_opportunity").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getBusinessOpportunityId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( BusinessOpportunityVO vo) {
                BusinessOpportunity entity =  BusinessOpportunity.builder()
                    .businessOpportunityId(vo.getBusinessOpportunityId())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .opportunityName(vo.getOpportunityName())
                    .status(vo.getStatus())
                    .followUpSuggestion(vo.getFollowUpSuggestion())
                .build();



                        boolean flag = this.businessOpportunityService.saveOrUpdate(entity);








                    aiAgentPublisher.publishEvent("BusinessOpportunity",String.valueOf(entity.getBusinessOpportunityId()),
                    "business_opportunity_id","update","follow_up_suggestion","你是销售专家，请根据客户和商机状态给出专业的跟进建议，直接输出",entity);


                return  flag
                         ? ResultUtils.success(entity.getBusinessOpportunityId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( BusinessOpportunityVO vo ) {
                BusinessOpportunity entity = this.businessOpportunityService.getById(vo.getBusinessOpportunityId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( BusinessOpportunityVO vo) {
                boolean flag = this.businessOpportunityService.removeById(vo.getBusinessOpportunityId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

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
import com.kuafu.web.entity.AiAssistantLogAll;
import com.kuafu.web.service.IAiAssistantLogAllService;
import com.kuafu.web.vo.AiAssistantLogAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> AI助手日志 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("AiAssistantLogAllControllerService")
@RequiredArgsConstructor
public class AiAssistantLogAllControllerService implements IControllerService<AiAssistantLogAll> {

    private final IAiAssistantLogAllService aiAssistantLogAllService;





    public BaseResponse page( AiAssistantLogAllPageVO pageVO){
        IPage<AiAssistantLogAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<AiAssistantLogAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("aal.ai_assistant_log_id");
        LambdaQueryWrapper<AiAssistantLogAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(AiAssistantLogAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getAiAssistantLogId() != null){
                queryWrapper.eq(AiAssistantLogAll::getAiAssistantLogId, pageVO.getAiAssistantLogId());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(AiAssistantLogAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(AiAssistantLogAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getResponseText())) {
                queryWrapper.like(AiAssistantLogAll::getResponseText, pageVO.getResponseText());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(AiAssistantLogAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
        if(StringUtils.isNotEmpty(pageVO.getQueryText())) {
                queryWrapper.like(AiAssistantLogAll::getQueryText, pageVO.getQueryText());
            }
            if(pageVO.getQueryTime() != null){
                queryWrapper.eq(AiAssistantLogAll::getQueryTime, pageVO.getQueryTime());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(AiAssistantLogAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    aiAssistantLogAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(AiAssistantLogAllPageVO vo){
            LambdaQueryWrapper<AiAssistantLogAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(AiAssistantLogAll::getPassword, vo.getPassword());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(AiAssistantLogAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(AiAssistantLogAll::getRegistrationDate, vo.getRegistrationDate());
                }
            if(StringUtils.isNotEmpty(vo.getResponseText())) {
                    queryWrapper.eq(AiAssistantLogAll::getResponseText, vo.getResponseText());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(AiAssistantLogAll::getPhoneNumber, vo.getPhoneNumber());
                }
            if(StringUtils.isNotEmpty(vo.getQueryText())) {
                    queryWrapper.eq(AiAssistantLogAll::getQueryText, vo.getQueryText());
                }
                if(vo.getQueryTime() != null){
                    queryWrapper.eq(AiAssistantLogAll::getQueryTime, vo.getQueryTime());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(AiAssistantLogAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(aiAssistantLogAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(AiAssistantLogAllPageVO vo) {


                LambdaQueryWrapper<AiAssistantLogAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(AiAssistantLogAll::getPassword, vo.getPassword());
                                }


                                if(vo.getAiAssistantLogId() != null){
                                    queryWrapper.eq(AiAssistantLogAll::getAiAssistantLogId, vo.getAiAssistantLogId());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(AiAssistantLogAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(AiAssistantLogAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getResponseText())) {
                                    queryWrapper.eq(AiAssistantLogAll::getResponseText, vo.getResponseText());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(AiAssistantLogAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                            if(StringUtils.isNotEmpty(vo.getQueryText())) {
                                    queryWrapper.eq(AiAssistantLogAll::getQueryText, vo.getQueryText());
                                }


                                if(vo.getQueryTime() != null){
                                    queryWrapper.eq(AiAssistantLogAll::getQueryTime, vo.getQueryTime());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(AiAssistantLogAll::getUsername, vo.getUsername());
                                }

                // AiAssistantLogAll entity = this.aiAssistantLogAllService.getById(aiAssistantLogId);
                    final Page<AiAssistantLogAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    aiAssistantLogAllService.pageNew(page, vo, queryWrapper);
                    final List<AiAssistantLogAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public AiAssistantLogAll getById(Serializable aiAssistantLogId) {

                    LambdaQueryWrapper<AiAssistantLogAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(AiAssistantLogAll::getAiAssistantLogId, aiAssistantLogId);

                    final Page<AiAssistantLogAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    aiAssistantLogAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<AiAssistantLogAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

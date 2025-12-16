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
import com.kuafu.web.entity.AiAssistantLog;
import com.kuafu.web.service.IAiAssistantLogService;
import com.kuafu.web.vo.AiAssistantLogPageVO;
import com.kuafu.web.vo.AiAssistantLogVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> AI助手日志 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("AiAssistantLogControllerService")
@RequiredArgsConstructor
public class AiAssistantLogControllerService  {

    private final IAiAssistantLogService aiAssistantLogService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( AiAssistantLogPageVO pageVO){
        IPage<AiAssistantLog> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<AiAssistantLog> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<AiAssistantLog> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(AiAssistantLog::getAiAssistantLogId);

            if(pageVO.getAiAssistantLogId() != null){
                queryWrapper.eq(AiAssistantLog::getAiAssistantLogId, pageVO.getAiAssistantLogId());
            }


            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(AiAssistantLog::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getQueryText())) {
                queryWrapper.eq(AiAssistantLog::getQueryText, pageVO.getQueryText());
            }


        if(StringUtils.isNotEmpty(pageVO.getResponseText())) {
                queryWrapper.eq(AiAssistantLog::getResponseText, pageVO.getResponseText());
            }


            if(pageVO.getQueryTime() != null){
                queryWrapper.eq(AiAssistantLog::getQueryTime, pageVO.getQueryTime());
            }

            return ResultUtils.success(aiAssistantLogService.page(page, queryWrapper));
        }


        public BaseResponse list(  AiAssistantLogVO vo){
            LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(AiAssistantLog::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getQueryText())) {
                    queryWrapper.eq(AiAssistantLog::getQueryText, vo.getQueryText());
                }
            if(StringUtils.isNotEmpty(vo.getResponseText())) {
                    queryWrapper.eq(AiAssistantLog::getResponseText, vo.getResponseText());
                }
                if(vo.getQueryTime() != null){
                    queryWrapper.eq(AiAssistantLog::getQueryTime, vo.getQueryTime());
                }
                return ResultUtils.success(aiAssistantLogService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  AiAssistantLogVO vo) {
                AiAssistantLog entity =  AiAssistantLog.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .queryText(vo.getQueryText())
                    .responseText(vo.getResponseText())
                    .queryTime(vo.getQueryTime())
                .build();








                boolean flag =this.aiAssistantLogService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("ai_assistant_log").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getAiAssistantLogId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( AiAssistantLogVO vo) {
                AiAssistantLog entity =  AiAssistantLog.builder()
                    .aiAssistantLogId(vo.getAiAssistantLogId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .queryText(vo.getQueryText())
                    .responseText(vo.getResponseText())
                    .queryTime(vo.getQueryTime())
                .build();



                        boolean flag = this.aiAssistantLogService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getAiAssistantLogId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( AiAssistantLogVO vo ) {
                AiAssistantLog entity = this.aiAssistantLogService.getById(vo.getAiAssistantLogId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( AiAssistantLogVO vo) {
                boolean flag = this.aiAssistantLogService.removeById(vo.getAiAssistantLogId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

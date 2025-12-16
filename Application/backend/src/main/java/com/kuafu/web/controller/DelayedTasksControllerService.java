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
import com.kuafu.web.entity.DelayedTasks;
import com.kuafu.web.service.IDelayedTasksService;
import com.kuafu.web.vo.DelayedTasksPageVO;
import com.kuafu.web.vo.DelayedTasksVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 延迟任务 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("DelayedTasksControllerService")
@RequiredArgsConstructor
public class DelayedTasksControllerService  {

    private final IDelayedTasksService delayedTasksService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( DelayedTasksPageVO pageVO){
        IPage<DelayedTasks> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<DelayedTasks> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<DelayedTasks> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(DelayedTasks::getId);

            if(pageVO.getId() != null){
                queryWrapper.eq(DelayedTasks::getId, pageVO.getId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTaskType())) {
                queryWrapper.eq(DelayedTasks::getTaskType, pageVO.getTaskType());
            }


        if(StringUtils.isNotEmpty(pageVO.getTaskData())) {
                queryWrapper.eq(DelayedTasks::getTaskData, pageVO.getTaskData());
            }


        if(StringUtils.isNotEmpty(pageVO.getStatus())) {
                queryWrapper.eq(DelayedTasks::getStatus, pageVO.getStatus());
            }


            if(pageVO.getExecuteTime() != null){
                queryWrapper.eq(DelayedTasks::getExecuteTime, pageVO.getExecuteTime());
            }


            if(pageVO.getCreateTime() != null){
                queryWrapper.eq(DelayedTasks::getCreateTime, pageVO.getCreateTime());
            }


            if(pageVO.getUpdateTime() != null){
                queryWrapper.eq(DelayedTasks::getUpdateTime, pageVO.getUpdateTime());
            }

            return ResultUtils.success(delayedTasksService.page(page, queryWrapper));
        }


        public BaseResponse list(  DelayedTasksVO vo){
            LambdaQueryWrapper<DelayedTasks> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTaskType())) {
                    queryWrapper.eq(DelayedTasks::getTaskType, vo.getTaskType());
                }
            if(StringUtils.isNotEmpty(vo.getTaskData())) {
                    queryWrapper.eq(DelayedTasks::getTaskData, vo.getTaskData());
                }
            if(StringUtils.isNotEmpty(vo.getStatus())) {
                    queryWrapper.eq(DelayedTasks::getStatus, vo.getStatus());
                }
                if(vo.getExecuteTime() != null){
                    queryWrapper.eq(DelayedTasks::getExecuteTime, vo.getExecuteTime());
                }
                if(vo.getCreateTime() != null){
                    queryWrapper.eq(DelayedTasks::getCreateTime, vo.getCreateTime());
                }
                if(vo.getUpdateTime() != null){
                    queryWrapper.eq(DelayedTasks::getUpdateTime, vo.getUpdateTime());
                }
                return ResultUtils.success(delayedTasksService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  DelayedTasksVO vo) {
                DelayedTasks entity =  DelayedTasks.builder()
                    .taskType(vo.getTaskType())
                    .taskData(vo.getTaskData())
                    .status(vo.getStatus())
                    .executeTime(vo.getExecuteTime())
                .build();








                boolean flag =this.delayedTasksService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("delayed_tasks").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( DelayedTasksVO vo) {
                DelayedTasks entity =  DelayedTasks.builder()
                    .id(vo.getId())
                    .taskType(vo.getTaskType())
                    .taskData(vo.getTaskData())
                    .status(vo.getStatus())
                    .executeTime(vo.getExecuteTime())
                .build();



                        boolean flag = this.delayedTasksService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( DelayedTasksVO vo ) {
                DelayedTasks entity = this.delayedTasksService.getById(vo.getId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( DelayedTasksVO vo) {
                boolean flag = this.delayedTasksService.removeById(vo.getId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

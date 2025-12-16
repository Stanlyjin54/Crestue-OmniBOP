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
import com.kuafu.web.entity.PermissionLog;
import com.kuafu.web.service.IPermissionLogService;
import com.kuafu.web.vo.PermissionLogPageVO;
import com.kuafu.web.vo.PermissionLogVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 权限变更日志 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("PermissionLogControllerService")
@RequiredArgsConstructor
public class PermissionLogControllerService  {

    private final IPermissionLogService permissionLogService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( PermissionLogPageVO pageVO){
        IPage<PermissionLog> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<PermissionLog> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<PermissionLog> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(PermissionLog::getPermissionLogId);

            if(pageVO.getPermissionLogId() != null){
                queryWrapper.eq(PermissionLog::getPermissionLogId, pageVO.getPermissionLogId());
            }


            if(pageVO.getUserInfoId() != null){
                queryWrapper.eq(PermissionLog::getUserInfoId, pageVO.getUserInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getActionType())) {
                queryWrapper.eq(PermissionLog::getActionType, pageVO.getActionType());
            }


        if(StringUtils.isNotEmpty(pageVO.getTargetObject())) {
                queryWrapper.eq(PermissionLog::getTargetObject, pageVO.getTargetObject());
            }


        if(StringUtils.isNotEmpty(pageVO.getChangeContent())) {
                queryWrapper.eq(PermissionLog::getChangeContent, pageVO.getChangeContent());
            }


            if(pageVO.getActionTime() != null){
                queryWrapper.eq(PermissionLog::getActionTime, pageVO.getActionTime());
            }

            return ResultUtils.success(permissionLogService.page(page, queryWrapper));
        }


        public BaseResponse list(  PermissionLogVO vo){
            LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoId() != null){
                    queryWrapper.eq(PermissionLog::getUserInfoId, vo.getUserInfoId());
                }
            if(StringUtils.isNotEmpty(vo.getActionType())) {
                    queryWrapper.eq(PermissionLog::getActionType, vo.getActionType());
                }
            if(StringUtils.isNotEmpty(vo.getTargetObject())) {
                    queryWrapper.eq(PermissionLog::getTargetObject, vo.getTargetObject());
                }
            if(StringUtils.isNotEmpty(vo.getChangeContent())) {
                    queryWrapper.eq(PermissionLog::getChangeContent, vo.getChangeContent());
                }
                if(vo.getActionTime() != null){
                    queryWrapper.eq(PermissionLog::getActionTime, vo.getActionTime());
                }
                return ResultUtils.success(permissionLogService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  PermissionLogVO vo) {
                PermissionLog entity =  PermissionLog.builder()
                    .userInfoId(vo.getUserInfoId())
                    .actionType(vo.getActionType())
                    .targetObject(vo.getTargetObject())
                    .changeContent(vo.getChangeContent())
                    .actionTime(vo.getActionTime())
                .build();








                boolean flag =this.permissionLogService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("permission_log").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getPermissionLogId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( PermissionLogVO vo) {
                PermissionLog entity =  PermissionLog.builder()
                    .permissionLogId(vo.getPermissionLogId())
                    .userInfoId(vo.getUserInfoId())
                    .actionType(vo.getActionType())
                    .targetObject(vo.getTargetObject())
                    .changeContent(vo.getChangeContent())
                    .actionTime(vo.getActionTime())
                .build();



                        boolean flag = this.permissionLogService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getPermissionLogId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( PermissionLogVO vo ) {
                PermissionLog entity = this.permissionLogService.getById(vo.getPermissionLogId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( PermissionLogVO vo) {
                boolean flag = this.permissionLogService.removeById(vo.getPermissionLogId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

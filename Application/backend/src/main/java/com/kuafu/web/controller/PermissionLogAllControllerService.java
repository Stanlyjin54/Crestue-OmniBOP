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
import com.kuafu.web.entity.PermissionLogAll;
import com.kuafu.web.service.IPermissionLogAllService;
import com.kuafu.web.vo.PermissionLogAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 权限变更日志 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("PermissionLogAllControllerService")
@RequiredArgsConstructor
public class PermissionLogAllControllerService implements IControllerService<PermissionLogAll> {

    private final IPermissionLogAllService permissionLogAllService;





    public BaseResponse page( PermissionLogAllPageVO pageVO){
        IPage<PermissionLogAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<PermissionLogAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("pl.permission_log_id");
        LambdaQueryWrapper<PermissionLogAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(PermissionLogAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getActionTime() != null){
                queryWrapper.eq(PermissionLogAll::getActionTime, pageVO.getActionTime());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(PermissionLogAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getActionType())) {
                queryWrapper.like(PermissionLogAll::getActionType, pageVO.getActionType());
            }
        if(StringUtils.isNotEmpty(pageVO.getTargetObject())) {
                queryWrapper.like(PermissionLogAll::getTargetObject, pageVO.getTargetObject());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(PermissionLogAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
            if(pageVO.getPermissionLogId() != null){
                queryWrapper.eq(PermissionLogAll::getPermissionLogId, pageVO.getPermissionLogId());
            }
            if(pageVO.getUserInfoId() != null){
                queryWrapper.eq(PermissionLogAll::getUserInfoId, pageVO.getUserInfoId());
            }
        if(StringUtils.isNotEmpty(pageVO.getChangeContent())) {
                queryWrapper.like(PermissionLogAll::getChangeContent, pageVO.getChangeContent());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(PermissionLogAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    permissionLogAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(PermissionLogAllPageVO vo){
            LambdaQueryWrapper<PermissionLogAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(PermissionLogAll::getPassword, vo.getPassword());
                }
                if(vo.getActionTime() != null){
                    queryWrapper.eq(PermissionLogAll::getActionTime, vo.getActionTime());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(PermissionLogAll::getRegistrationDate, vo.getRegistrationDate());
                }
            if(StringUtils.isNotEmpty(vo.getActionType())) {
                    queryWrapper.eq(PermissionLogAll::getActionType, vo.getActionType());
                }
            if(StringUtils.isNotEmpty(vo.getTargetObject())) {
                    queryWrapper.eq(PermissionLogAll::getTargetObject, vo.getTargetObject());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(PermissionLogAll::getPhoneNumber, vo.getPhoneNumber());
                }
                if(vo.getUserInfoId() != null){
                    queryWrapper.eq(PermissionLogAll::getUserInfoId, vo.getUserInfoId());
                }
            if(StringUtils.isNotEmpty(vo.getChangeContent())) {
                    queryWrapper.eq(PermissionLogAll::getChangeContent, vo.getChangeContent());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(PermissionLogAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(permissionLogAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(PermissionLogAllPageVO vo) {


                LambdaQueryWrapper<PermissionLogAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(PermissionLogAll::getPassword, vo.getPassword());
                                }


                                if(vo.getActionTime() != null){
                                    queryWrapper.eq(PermissionLogAll::getActionTime, vo.getActionTime());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(PermissionLogAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getActionType())) {
                                    queryWrapper.eq(PermissionLogAll::getActionType, vo.getActionType());
                                }


                            if(StringUtils.isNotEmpty(vo.getTargetObject())) {
                                    queryWrapper.eq(PermissionLogAll::getTargetObject, vo.getTargetObject());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(PermissionLogAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                                if(vo.getPermissionLogId() != null){
                                    queryWrapper.eq(PermissionLogAll::getPermissionLogId, vo.getPermissionLogId());
                                }


                                if(vo.getUserInfoId() != null){
                                    queryWrapper.eq(PermissionLogAll::getUserInfoId, vo.getUserInfoId());
                                }


                            if(StringUtils.isNotEmpty(vo.getChangeContent())) {
                                    queryWrapper.eq(PermissionLogAll::getChangeContent, vo.getChangeContent());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(PermissionLogAll::getUsername, vo.getUsername());
                                }

                // PermissionLogAll entity = this.permissionLogAllService.getById(permissionLogId);
                    final Page<PermissionLogAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    permissionLogAllService.pageNew(page, vo, queryWrapper);
                    final List<PermissionLogAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public PermissionLogAll getById(Serializable permissionLogId) {

                    LambdaQueryWrapper<PermissionLogAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(PermissionLogAll::getPermissionLogId, permissionLogId);

                    final Page<PermissionLogAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    permissionLogAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<PermissionLogAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

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
import com.kuafu.web.entity.RolePermissionAll;
import com.kuafu.web.service.IRolePermissionAllService;
import com.kuafu.web.vo.RolePermissionAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IRoleService;
import com.kuafu.web.entity.Role;
import com.kuafu.web.service.IPermissionService;
import com.kuafu.web.entity.Permission;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 角色权限关联 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("RolePermissionAllControllerService")
@RequiredArgsConstructor
public class RolePermissionAllControllerService implements IControllerService<RolePermissionAll> {

    private final IRolePermissionAllService rolePermissionAllService;





    public BaseResponse page( RolePermissionAllPageVO pageVO){
        IPage<RolePermissionAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<RolePermissionAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("rp.role_permission_id");
        LambdaQueryWrapper<RolePermissionAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getRoleName())) {

                        String s_string = pageVO.getRoleName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(RolePermissionAll::getRoleName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getPermissionDesc())) {
                queryWrapper.like(RolePermissionAll::getPermissionDesc, pageVO.getPermissionDesc());
            }
            if(pageVO.getCreateTime() != null){
                queryWrapper.eq(RolePermissionAll::getCreateTime, pageVO.getCreateTime());
            }
            if(pageVO.getRoleId() != null){
                queryWrapper.eq(RolePermissionAll::getRoleId, pageVO.getRoleId());
            }
        if(StringUtils.isNotEmpty(pageVO.getRoleDesc())) {
                queryWrapper.like(RolePermissionAll::getRoleDesc, pageVO.getRoleDesc());
            }
            if(pageVO.getPermissionId() != null){
                queryWrapper.eq(RolePermissionAll::getPermissionId, pageVO.getPermissionId());
            }
        if(StringUtils.isNotEmpty(pageVO.getPermissionName())) {

                        String s_string = pageVO.getPermissionName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(RolePermissionAll::getPermissionName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getFeatureCode())) {
                queryWrapper.like(RolePermissionAll::getFeatureCode, pageVO.getFeatureCode());
            }
            if(pageVO.getRolePermissionId() != null){
                queryWrapper.eq(RolePermissionAll::getRolePermissionId, pageVO.getRolePermissionId());
            }

                    rolePermissionAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(RolePermissionAllPageVO vo){
            LambdaQueryWrapper<RolePermissionAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getRoleName())) {
                            queryWrapper.like(RolePermissionAll::getRoleName, vo.getRoleName());
                }
            if(StringUtils.isNotEmpty(vo.getPermissionDesc())) {
                    queryWrapper.eq(RolePermissionAll::getPermissionDesc, vo.getPermissionDesc());
                }
                if(vo.getCreateTime() != null){
                    queryWrapper.eq(RolePermissionAll::getCreateTime, vo.getCreateTime());
                }
                if(vo.getRoleId() != null){
                    queryWrapper.eq(RolePermissionAll::getRoleId, vo.getRoleId());
                }
            if(StringUtils.isNotEmpty(vo.getRoleDesc())) {
                    queryWrapper.eq(RolePermissionAll::getRoleDesc, vo.getRoleDesc());
                }
                if(vo.getPermissionId() != null){
                    queryWrapper.eq(RolePermissionAll::getPermissionId, vo.getPermissionId());
                }
            if(StringUtils.isNotEmpty(vo.getPermissionName())) {
                            queryWrapper.like(RolePermissionAll::getPermissionName, vo.getPermissionName());
                }
            if(StringUtils.isNotEmpty(vo.getFeatureCode())) {
                    queryWrapper.eq(RolePermissionAll::getFeatureCode, vo.getFeatureCode());
                }
                return ResultUtils.success(rolePermissionAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(RolePermissionAllPageVO vo) {


                LambdaQueryWrapper<RolePermissionAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getRoleName())) {
                                            queryWrapper.like(RolePermissionAll::getRoleName, vo.getRoleName());
                                }


                            if(StringUtils.isNotEmpty(vo.getPermissionDesc())) {
                                    queryWrapper.eq(RolePermissionAll::getPermissionDesc, vo.getPermissionDesc());
                                }


                                if(vo.getCreateTime() != null){
                                    queryWrapper.eq(RolePermissionAll::getCreateTime, vo.getCreateTime());
                                }


                                if(vo.getRoleId() != null){
                                    queryWrapper.eq(RolePermissionAll::getRoleId, vo.getRoleId());
                                }


                            if(StringUtils.isNotEmpty(vo.getRoleDesc())) {
                                    queryWrapper.eq(RolePermissionAll::getRoleDesc, vo.getRoleDesc());
                                }


                                if(vo.getPermissionId() != null){
                                    queryWrapper.eq(RolePermissionAll::getPermissionId, vo.getPermissionId());
                                }


                            if(StringUtils.isNotEmpty(vo.getPermissionName())) {
                                            queryWrapper.like(RolePermissionAll::getPermissionName, vo.getPermissionName());
                                }


                            if(StringUtils.isNotEmpty(vo.getFeatureCode())) {
                                    queryWrapper.eq(RolePermissionAll::getFeatureCode, vo.getFeatureCode());
                                }


                                if(vo.getRolePermissionId() != null){
                                    queryWrapper.eq(RolePermissionAll::getRolePermissionId, vo.getRolePermissionId());
                                }

                // RolePermissionAll entity = this.rolePermissionAllService.getById(rolePermissionId);
                    final Page<RolePermissionAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    rolePermissionAllService.pageNew(page, vo, queryWrapper);
                    final List<RolePermissionAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public RolePermissionAll getById(Serializable rolePermissionId) {

                    LambdaQueryWrapper<RolePermissionAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(RolePermissionAll::getRolePermissionId, rolePermissionId);

                    final Page<RolePermissionAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    rolePermissionAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<RolePermissionAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

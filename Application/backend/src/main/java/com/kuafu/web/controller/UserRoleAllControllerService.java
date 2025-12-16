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
import com.kuafu.web.entity.UserRoleAll;
import com.kuafu.web.service.IUserRoleAllService;
import com.kuafu.web.vo.UserRoleAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IRoleService;
import com.kuafu.web.entity.Role;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 用户角色关联 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("UserRoleAllControllerService")
@RequiredArgsConstructor
public class UserRoleAllControllerService implements IControllerService<UserRoleAll> {

    private final IUserRoleAllService userRoleAllService;





    public BaseResponse page( UserRoleAllPageVO pageVO){
        IPage<UserRoleAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<UserRoleAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ur.user_role_id");
        LambdaQueryWrapper<UserRoleAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getRoleName())) {

                        String s_string = pageVO.getRoleName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(UserRoleAll::getRoleName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getUserRoleId() != null){
                queryWrapper.eq(UserRoleAll::getUserRoleId, pageVO.getUserRoleId());
            }
        if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                queryWrapper.like(UserRoleAll::getPassword, pageVO.getPassword());
            }
            if(pageVO.getRegistrationDate() != null){
                queryWrapper.eq(UserRoleAll::getRegistrationDate, pageVO.getRegistrationDate());
            }
            if(pageVO.getCreateTime() != null){
                queryWrapper.eq(UserRoleAll::getCreateTime, pageVO.getCreateTime());
            }
            if(pageVO.getRoleId() != null){
                queryWrapper.eq(UserRoleAll::getRoleId, pageVO.getRoleId());
            }
        if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                queryWrapper.like(UserRoleAll::getPhoneNumber, pageVO.getPhoneNumber());
            }
        if(StringUtils.isNotEmpty(pageVO.getRoleDesc())) {
                queryWrapper.like(UserRoleAll::getRoleDesc, pageVO.getRoleDesc());
            }
            if(pageVO.getUserInfoId() != null){
                queryWrapper.eq(UserRoleAll::getUserInfoId, pageVO.getUserInfoId());
            }
        if(StringUtils.isNotEmpty(pageVO.getUsername())) {

                        String s_string = pageVO.getUsername();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(UserRoleAll::getUsername, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

                    userRoleAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(UserRoleAllPageVO vo){
            LambdaQueryWrapper<UserRoleAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getRoleName())) {
                            queryWrapper.like(UserRoleAll::getRoleName, vo.getRoleName());
                }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
                    queryWrapper.eq(UserRoleAll::getPassword, vo.getPassword());
                }
                if(vo.getRegistrationDate() != null){
                    queryWrapper.eq(UserRoleAll::getRegistrationDate, vo.getRegistrationDate());
                }
                if(vo.getCreateTime() != null){
                    queryWrapper.eq(UserRoleAll::getCreateTime, vo.getCreateTime());
                }
                if(vo.getRoleId() != null){
                    queryWrapper.eq(UserRoleAll::getRoleId, vo.getRoleId());
                }
            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                    queryWrapper.eq(UserRoleAll::getPhoneNumber, vo.getPhoneNumber());
                }
            if(StringUtils.isNotEmpty(vo.getRoleDesc())) {
                    queryWrapper.eq(UserRoleAll::getRoleDesc, vo.getRoleDesc());
                }
                if(vo.getUserInfoId() != null){
                    queryWrapper.eq(UserRoleAll::getUserInfoId, vo.getUserInfoId());
                }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
                            queryWrapper.like(UserRoleAll::getUsername, vo.getUsername());
                }
                return ResultUtils.success(userRoleAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(UserRoleAllPageVO vo) {


                LambdaQueryWrapper<UserRoleAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getRoleName())) {
                                            queryWrapper.like(UserRoleAll::getRoleName, vo.getRoleName());
                                }


                                if(vo.getUserRoleId() != null){
                                    queryWrapper.eq(UserRoleAll::getUserRoleId, vo.getUserRoleId());
                                }


                            if(StringUtils.isNotEmpty(vo.getPassword())) {
                                    queryWrapper.eq(UserRoleAll::getPassword, vo.getPassword());
                                }


                                if(vo.getRegistrationDate() != null){
                                    queryWrapper.eq(UserRoleAll::getRegistrationDate, vo.getRegistrationDate());
                                }


                                if(vo.getCreateTime() != null){
                                    queryWrapper.eq(UserRoleAll::getCreateTime, vo.getCreateTime());
                                }


                                if(vo.getRoleId() != null){
                                    queryWrapper.eq(UserRoleAll::getRoleId, vo.getRoleId());
                                }


                            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
                                    queryWrapper.eq(UserRoleAll::getPhoneNumber, vo.getPhoneNumber());
                                }


                            if(StringUtils.isNotEmpty(vo.getRoleDesc())) {
                                    queryWrapper.eq(UserRoleAll::getRoleDesc, vo.getRoleDesc());
                                }


                                if(vo.getUserInfoId() != null){
                                    queryWrapper.eq(UserRoleAll::getUserInfoId, vo.getUserInfoId());
                                }


                            if(StringUtils.isNotEmpty(vo.getUsername())) {
                                            queryWrapper.like(UserRoleAll::getUsername, vo.getUsername());
                                }

                // UserRoleAll entity = this.userRoleAllService.getById(userRoleId);
                    final Page<UserRoleAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    userRoleAllService.pageNew(page, vo, queryWrapper);
                    final List<UserRoleAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public UserRoleAll getById(Serializable userRoleId) {

                    LambdaQueryWrapper<UserRoleAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(UserRoleAll::getUserRoleId, userRoleId);

                    final Page<UserRoleAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    userRoleAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<UserRoleAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

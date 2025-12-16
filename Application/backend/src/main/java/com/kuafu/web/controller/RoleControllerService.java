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
import com.kuafu.web.entity.Role;
import com.kuafu.web.service.IRoleService;
import com.kuafu.web.vo.RolePageVO;
import com.kuafu.web.vo.RoleVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 角色 接口 </p>
 *
 * @description
 * @date 2025/06/18 13:48
 */
@Service("RoleControllerService")
@RequiredArgsConstructor
public class RoleControllerService  {

    private final IRoleService roleService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( RolePageVO pageVO){
        IPage<Role> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<Role> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<Role> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(Role::getRoleId);

            if(pageVO.getRoleId() != null){
                queryWrapper.eq(Role::getRoleId, pageVO.getRoleId());
            }


        if(StringUtils.isNotEmpty(pageVO.getRoleName())) {
//                        queryWrapper.like(Role::getRoleName, pageVO.getRoleName());
                        String s_string = pageVO.getRoleName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(Role::getRoleName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getRoleDesc())) {
                queryWrapper.eq(Role::getRoleDesc, pageVO.getRoleDesc());
            }


            if(pageVO.getCreateTime() != null){
                queryWrapper.eq(Role::getCreateTime, pageVO.getCreateTime());
            }

            return ResultUtils.success(roleService.page(page, queryWrapper));
        }


        public BaseResponse list(  RoleVO vo){
            LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getRoleName())) {
                            queryWrapper.like(Role::getRoleName, vo.getRoleName());
                }
            if(StringUtils.isNotEmpty(vo.getRoleDesc())) {
                    queryWrapper.eq(Role::getRoleDesc, vo.getRoleDesc());
                }
                if(vo.getCreateTime() != null){
                    queryWrapper.eq(Role::getCreateTime, vo.getCreateTime());
                }
                return ResultUtils.success(roleService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  RoleVO vo) {
                Role entity =  Role.builder()
                    .roleName(vo.getRoleName())
                    .roleDesc(vo.getRoleDesc())
                .build();








                boolean flag =this.roleService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("role").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getRoleId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( RoleVO vo) {
                Role entity =  Role.builder()
                    .roleId(vo.getRoleId())
                    .roleName(vo.getRoleName())
                    .roleDesc(vo.getRoleDesc())
                .build();



                        boolean flag = this.roleService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getRoleId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( RoleVO vo ) {
                Role entity = this.roleService.getById(vo.getRoleId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( RoleVO vo) {
                boolean flag = this.roleService.removeById(vo.getRoleId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

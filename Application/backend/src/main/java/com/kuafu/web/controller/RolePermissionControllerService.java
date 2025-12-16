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
import com.kuafu.web.entity.RolePermission;
import com.kuafu.web.service.IRolePermissionService;
import com.kuafu.web.vo.RolePermissionPageVO;
import com.kuafu.web.vo.RolePermissionVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 角色权限关联 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("RolePermissionControllerService")
@RequiredArgsConstructor
public class RolePermissionControllerService  {

    private final IRolePermissionService rolePermissionService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( RolePermissionPageVO pageVO){
        IPage<RolePermission> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<RolePermission> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<RolePermission> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(RolePermission::getRolePermissionId);

            if(pageVO.getRolePermissionId() != null){
                queryWrapper.eq(RolePermission::getRolePermissionId, pageVO.getRolePermissionId());
            }


            if(pageVO.getRoleId() != null){
                queryWrapper.eq(RolePermission::getRoleId, pageVO.getRoleId());
            }


            if(pageVO.getPermissionId() != null){
                queryWrapper.eq(RolePermission::getPermissionId, pageVO.getPermissionId());
            }

            return ResultUtils.success(rolePermissionService.page(page, queryWrapper));
        }


        public BaseResponse list(  RolePermissionVO vo){
            LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getRoleId() != null){
                    queryWrapper.eq(RolePermission::getRoleId, vo.getRoleId());
                }
                if(vo.getPermissionId() != null){
                    queryWrapper.eq(RolePermission::getPermissionId, vo.getPermissionId());
                }
                return ResultUtils.success(rolePermissionService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  RolePermissionVO vo) {
                RolePermission entity =  RolePermission.builder()
                    .roleId(vo.getRoleId())
                    .permissionId(vo.getPermissionId())
                .build();








                boolean flag =this.rolePermissionService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("role_permission").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getRolePermissionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( RolePermissionVO vo) {
                RolePermission entity =  RolePermission.builder()
                    .rolePermissionId(vo.getRolePermissionId())
                    .roleId(vo.getRoleId())
                    .permissionId(vo.getPermissionId())
                .build();



                        boolean flag = this.rolePermissionService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getRolePermissionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( RolePermissionVO vo ) {
                RolePermission entity = this.rolePermissionService.getById(vo.getRolePermissionId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( RolePermissionVO vo) {
                boolean flag = this.rolePermissionService.removeById(vo.getRolePermissionId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

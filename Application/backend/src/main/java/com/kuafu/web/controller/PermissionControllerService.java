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
import com.kuafu.web.entity.Permission;
import com.kuafu.web.service.IPermissionService;
import com.kuafu.web.vo.PermissionPageVO;
import com.kuafu.web.vo.PermissionVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 权限 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("PermissionControllerService")
@RequiredArgsConstructor
public class PermissionControllerService  {

    private final IPermissionService permissionService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( PermissionPageVO pageVO){
        IPage<Permission> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<Permission> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<Permission> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(Permission::getPermissionId);

            if(pageVO.getPermissionId() != null){
                queryWrapper.eq(Permission::getPermissionId, pageVO.getPermissionId());
            }


        if(StringUtils.isNotEmpty(pageVO.getPermissionName())) {
//                        queryWrapper.like(Permission::getPermissionName, pageVO.getPermissionName());
                        String s_string = pageVO.getPermissionName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(Permission::getPermissionName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getPermissionDesc())) {
                queryWrapper.eq(Permission::getPermissionDesc, pageVO.getPermissionDesc());
            }


        if(StringUtils.isNotEmpty(pageVO.getFeatureCode())) {
                queryWrapper.eq(Permission::getFeatureCode, pageVO.getFeatureCode());
            }

            return ResultUtils.success(permissionService.page(page, queryWrapper));
        }


        public BaseResponse list(  PermissionVO vo){
            LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPermissionName())) {
                            queryWrapper.like(Permission::getPermissionName, vo.getPermissionName());
                }
            if(StringUtils.isNotEmpty(vo.getPermissionDesc())) {
                    queryWrapper.eq(Permission::getPermissionDesc, vo.getPermissionDesc());
                }
            if(StringUtils.isNotEmpty(vo.getFeatureCode())) {
                    queryWrapper.eq(Permission::getFeatureCode, vo.getFeatureCode());
                }
                return ResultUtils.success(permissionService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  PermissionVO vo) {
                Permission entity =  Permission.builder()
                    .permissionName(vo.getPermissionName())
                    .permissionDesc(vo.getPermissionDesc())
                    .featureCode(vo.getFeatureCode())
                .build();








                boolean flag =this.permissionService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("permission").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getPermissionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( PermissionVO vo) {
                Permission entity =  Permission.builder()
                    .permissionId(vo.getPermissionId())
                    .permissionName(vo.getPermissionName())
                    .permissionDesc(vo.getPermissionDesc())
                    .featureCode(vo.getFeatureCode())
                .build();



                        boolean flag = this.permissionService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getPermissionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( PermissionVO vo ) {
                Permission entity = this.permissionService.getById(vo.getPermissionId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( PermissionVO vo) {
                boolean flag = this.permissionService.removeById(vo.getPermissionId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

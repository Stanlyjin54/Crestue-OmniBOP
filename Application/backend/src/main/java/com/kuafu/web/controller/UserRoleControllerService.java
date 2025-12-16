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
import com.kuafu.web.entity.UserRole;
import com.kuafu.web.service.IUserRoleService;
import com.kuafu.web.vo.UserRolePageVO;
import com.kuafu.web.vo.UserRoleVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 用户角色关联 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("UserRoleControllerService")
@RequiredArgsConstructor
public class UserRoleControllerService  {

    private final IUserRoleService userRoleService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( UserRolePageVO pageVO){
        IPage<UserRole> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<UserRole> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<UserRole> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(UserRole::getUserRoleId);

            if(pageVO.getUserRoleId() != null){
                queryWrapper.eq(UserRole::getUserRoleId, pageVO.getUserRoleId());
            }


            if(pageVO.getUserInfoId() != null){
                queryWrapper.eq(UserRole::getUserInfoId, pageVO.getUserInfoId());
            }


            if(pageVO.getRoleId() != null){
                queryWrapper.eq(UserRole::getRoleId, pageVO.getRoleId());
            }

            return ResultUtils.success(userRoleService.page(page, queryWrapper));
        }


        public BaseResponse list(  UserRoleVO vo){
            LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoId() != null){
                    queryWrapper.eq(UserRole::getUserInfoId, vo.getUserInfoId());
                }
                if(vo.getRoleId() != null){
                    queryWrapper.eq(UserRole::getRoleId, vo.getRoleId());
                }
                return ResultUtils.success(userRoleService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  UserRoleVO vo) {
                UserRole entity =  UserRole.builder()
                    .userInfoId(vo.getUserInfoId())
                    .roleId(vo.getRoleId())
                .build();








                boolean flag =this.userRoleService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("user_role").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getUserRoleId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( UserRoleVO vo) {
                UserRole entity =  UserRole.builder()
                    .userRoleId(vo.getUserRoleId())
                    .userInfoId(vo.getUserInfoId())
                    .roleId(vo.getRoleId())
                .build();



                        boolean flag = this.userRoleService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getUserRoleId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( UserRoleVO vo ) {
                UserRole entity = this.userRoleService.getById(vo.getUserRoleId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( UserRoleVO vo) {
                boolean flag = this.userRoleService.removeById(vo.getUserRoleId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

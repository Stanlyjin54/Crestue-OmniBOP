package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.RolePermissionAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  角色权限关联 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IRolePermissionAllService extends IService<RolePermissionAll> {
        List<RolePermissionAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<RolePermissionAll> queryWrapper);
        List<RolePermissionAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<RolePermissionAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<RolePermissionAll> queryWrapper);

        IPage pageNew(IPage<RolePermissionAll> page, PageRequest pageRequest, LambdaQueryWrapper<RolePermissionAll> queryWrapper);
        }

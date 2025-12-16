package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.PermissionLogAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  权限变更日志 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IPermissionLogAllService extends IService<PermissionLogAll> {
        List<PermissionLogAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<PermissionLogAll> queryWrapper);
        List<PermissionLogAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<PermissionLogAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<PermissionLogAll> queryWrapper);

        IPage pageNew(IPage<PermissionLogAll> page, PageRequest pageRequest, LambdaQueryWrapper<PermissionLogAll> queryWrapper);
        }

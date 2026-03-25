package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.UserRoleAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  用户角色关联 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IUserRoleAllService extends IService<UserRoleAll> {
        List<UserRoleAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<UserRoleAll> queryWrapper);
        List<UserRoleAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<UserRoleAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<UserRoleAll> queryWrapper);

        IPage pageNew(IPage<UserRoleAll> page, PageRequest pageRequest, LambdaQueryWrapper<UserRoleAll> queryWrapper);
        }

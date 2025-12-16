package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.UserInfoAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  用户信息 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IUserInfoAllService extends IService<UserInfoAll> {
        List<UserInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<UserInfoAll> queryWrapper);
        List<UserInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<UserInfoAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<UserInfoAll> queryWrapper);

        IPage pageNew(IPage<UserInfoAll> page, PageRequest pageRequest, LambdaQueryWrapper<UserInfoAll> queryWrapper);
        }

package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.CustomerInfoAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  客户信息 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ICustomerInfoAllService extends IService<CustomerInfoAll> {
        List<CustomerInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<CustomerInfoAll> queryWrapper);
        List<CustomerInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<CustomerInfoAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<CustomerInfoAll> queryWrapper);

        IPage pageNew(IPage<CustomerInfoAll> page, PageRequest pageRequest, LambdaQueryWrapper<CustomerInfoAll> queryWrapper);
        }

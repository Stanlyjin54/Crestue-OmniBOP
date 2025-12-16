package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.BusinessOpportunityAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  商机线索 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IBusinessOpportunityAllService extends IService<BusinessOpportunityAll> {
        List<BusinessOpportunityAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper);
        List<BusinessOpportunityAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper);

        IPage pageNew(IPage<BusinessOpportunityAll> page, PageRequest pageRequest, LambdaQueryWrapper<BusinessOpportunityAll> queryWrapper);
        }

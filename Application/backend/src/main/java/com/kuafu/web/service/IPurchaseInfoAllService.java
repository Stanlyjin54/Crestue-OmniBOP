package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.PurchaseInfoAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  采购信息 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IPurchaseInfoAllService extends IService<PurchaseInfoAll> {
        List<PurchaseInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<PurchaseInfoAll> queryWrapper);
        List<PurchaseInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<PurchaseInfoAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<PurchaseInfoAll> queryWrapper);

        IPage pageNew(IPage<PurchaseInfoAll> page, PageRequest pageRequest, LambdaQueryWrapper<PurchaseInfoAll> queryWrapper);
        }

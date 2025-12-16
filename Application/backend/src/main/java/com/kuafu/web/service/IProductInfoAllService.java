package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.ProductInfoAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  商品信息 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IProductInfoAllService extends IService<ProductInfoAll> {
        List<ProductInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<ProductInfoAll> queryWrapper);
        List<ProductInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<ProductInfoAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<ProductInfoAll> queryWrapper);

        IPage pageNew(IPage<ProductInfoAll> page, PageRequest pageRequest, LambdaQueryWrapper<ProductInfoAll> queryWrapper);
        }

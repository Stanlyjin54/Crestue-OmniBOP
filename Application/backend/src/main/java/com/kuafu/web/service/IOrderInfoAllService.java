package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.OrderInfoAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  订单信息 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IOrderInfoAllService extends IService<OrderInfoAll> {
        List<OrderInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<OrderInfoAll> queryWrapper);
        List<OrderInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<OrderInfoAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<OrderInfoAll> queryWrapper);

        IPage pageNew(IPage<OrderInfoAll> page, PageRequest pageRequest, LambdaQueryWrapper<OrderInfoAll> queryWrapper);
        }

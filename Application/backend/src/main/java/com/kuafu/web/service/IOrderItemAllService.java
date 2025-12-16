package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.OrderItemAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  订单商品明细 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IOrderItemAllService extends IService<OrderItemAll> {
        List<OrderItemAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<OrderItemAll> queryWrapper);
        List<OrderItemAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<OrderItemAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<OrderItemAll> queryWrapper);

        IPage pageNew(IPage<OrderItemAll> page, PageRequest pageRequest, LambdaQueryWrapper<OrderItemAll> queryWrapper);
        }

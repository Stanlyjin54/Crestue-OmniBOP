package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.InventoryRecordAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  库存记录 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IInventoryRecordAllService extends IService<InventoryRecordAll> {
        List<InventoryRecordAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<InventoryRecordAll> queryWrapper);
        List<InventoryRecordAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<InventoryRecordAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<InventoryRecordAll> queryWrapper);

        IPage pageNew(IPage<InventoryRecordAll> page, PageRequest pageRequest, LambdaQueryWrapper<InventoryRecordAll> queryWrapper);
        }

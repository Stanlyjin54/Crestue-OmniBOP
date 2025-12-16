package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.TransferInfoAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  调拨信息 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ITransferInfoAllService extends IService<TransferInfoAll> {
        List<TransferInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<TransferInfoAll> queryWrapper);
        List<TransferInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<TransferInfoAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<TransferInfoAll> queryWrapper);

        IPage pageNew(IPage<TransferInfoAll> page, PageRequest pageRequest, LambdaQueryWrapper<TransferInfoAll> queryWrapper);
        }

package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.ProcurementSuggestionAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  采购建议 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IProcurementSuggestionAllService extends IService<ProcurementSuggestionAll> {
        List<ProcurementSuggestionAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper);
        List<ProcurementSuggestionAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper);

        IPage pageNew(IPage<ProcurementSuggestionAll> page, PageRequest pageRequest, LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper);
        }

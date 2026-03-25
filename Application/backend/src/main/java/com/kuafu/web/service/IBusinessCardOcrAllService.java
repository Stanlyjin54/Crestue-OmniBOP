package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.BusinessCardOcrAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  名片OCR建档 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IBusinessCardOcrAllService extends IService<BusinessCardOcrAll> {
        List<BusinessCardOcrAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper);
        List<BusinessCardOcrAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper);

        IPage pageNew(IPage<BusinessCardOcrAll> page, PageRequest pageRequest, LambdaQueryWrapper<BusinessCardOcrAll> queryWrapper);
        }

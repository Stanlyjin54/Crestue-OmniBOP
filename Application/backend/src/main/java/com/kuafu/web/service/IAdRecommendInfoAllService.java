package com.kuafu.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.web.entity.AdRecommendInfoAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  广告推荐 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IAdRecommendInfoAllService extends IService<AdRecommendInfoAll> {
        List<AdRecommendInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper);
        List<AdRecommendInfoAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper);

        IPage pageNew(IPage<AdRecommendInfoAll> page, PageRequest pageRequest, LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper);
        }

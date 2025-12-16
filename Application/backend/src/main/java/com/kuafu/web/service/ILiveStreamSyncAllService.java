package com.kuafuweb.web.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.common.domin.PageRequest;
import com.kuafuweb.web.entity.LiveStreamSyncAll;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>  直播带货同步 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ILiveStreamSyncAllService extends IService<LiveStreamSyncAll> {
        List<LiveStreamSyncAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper);
        List<LiveStreamSyncAll> selectListNew(PageRequest pageRequest, LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper,boolean isPage);

        long selectCount(PageRequest pageRequest, LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper);

        IPage pageNew(IPage<LiveStreamSyncAll> page, PageRequest pageRequest, LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper);
        }

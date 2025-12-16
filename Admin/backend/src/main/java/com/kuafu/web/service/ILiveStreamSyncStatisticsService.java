package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.LiveStreamSync;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  live_stream_sync 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ILiveStreamSyncStatisticsService extends IService<LiveStreamSync> {
        List<Object> live_stream_sync_sync_time_datetime_statistic_f04701f0_count(LambdaQueryWrapper queryWrapper);

        List<Object> live_stream_sync_sync_time_datetime_statistic_9943545f_count(LambdaQueryWrapper queryWrapper);

        List<Object> live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count(LambdaQueryWrapper queryWrapper);

}






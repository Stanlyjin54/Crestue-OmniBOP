package com.kuafuweb.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafuweb.web.mapper.LiveStreamSyncStatisticsMapper;
import com.kuafuweb.web.entity.LiveStreamSync;
import com.kuafuweb.web.service.ILiveStreamSyncStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> live_stream_sync 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("LiveStreamSyncStatistics")
public class LiveStreamSyncStatisticsServiceImpl extends ServiceImpl<LiveStreamSyncStatisticsMapper, LiveStreamSync> implements ILiveStreamSyncStatisticsService {
    @Resource
    private LiveStreamSyncStatisticsMapper liveStreamSyncStaticMapper;

                        @Override
                        public List<Object> live_stream_sync_sync_time_datetime_statistic_f04701f0_count(LambdaQueryWrapper queryWrapper) {
                                return liveStreamSyncStaticMapper.live_stream_sync_sync_time_datetime_statistic_f04701f0_count(queryWrapper);
                                }


                        @Override
                        public List<Object> live_stream_sync_sync_time_datetime_statistic_9943545f_count(LambdaQueryWrapper queryWrapper) {
                                return liveStreamSyncStaticMapper.live_stream_sync_sync_time_datetime_statistic_9943545f_count(queryWrapper);
                                }


                        @Override
                        public List<Object> live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count(LambdaQueryWrapper queryWrapper) {
                                return liveStreamSyncStaticMapper.live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count(queryWrapper);
                                }


}






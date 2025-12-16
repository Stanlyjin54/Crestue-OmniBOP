package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.LiveStreamSync;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> live_stream_sync Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface LiveStreamSyncStatisticsMapper extends BaseMapper<LiveStreamSync> {

                        List<Object> live_stream_sync_sync_time_datetime_statistic_f04701f0_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> live_stream_sync_sync_time_datetime_statistic_9943545f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafuweb.common.domin.BaseResponse;
import com.kuafuweb.common.domin.ErrorCode;
import com.kuafuweb.common.domin.ResultUtils;
import com.kuafuweb.common.util.StringUtils;
import com.kuafuweb.web.entity.LiveStreamSync;
import com.kuafuweb.web.service.ILiveStreamSyncStatisticsService;
import com.kuafuweb.web.entity.SelectVO;

import com.kuafuweb.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;





/**

 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/liveStreamSyncStatistics")

public class LiveStreamSyncStatisticsController  {

    private final ILiveStreamSyncStatisticsService liveStreamSyncStatisticsService;


    @PostMapping("live_stream_sync_sync_time_datetime_statistic_f04701f0_count")
    public BaseResponse live_stream_sync_sync_time_datetime_statistic_f04701f0_count(@RequestBody LiveStreamSync statisticVo) {
        LambdaQueryWrapper<LiveStreamSync> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( liveStreamSyncStatisticsService.live_stream_sync_sync_time_datetime_statistic_f04701f0_count(queryWrapper));
    }
    @PostMapping("live_stream_sync_sync_time_datetime_statistic_9943545f_count")
    public BaseResponse live_stream_sync_sync_time_datetime_statistic_9943545f_count(@RequestBody LiveStreamSync statisticVo) {
        LambdaQueryWrapper<LiveStreamSync> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( liveStreamSyncStatisticsService.live_stream_sync_sync_time_datetime_statistic_9943545f_count(queryWrapper));
    }
    @PostMapping("live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count")
    public BaseResponse live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count(@RequestBody LiveStreamSync statisticVo) {
        LambdaQueryWrapper<LiveStreamSync> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( liveStreamSyncStatisticsService.live_stream_sync_sync_time_datetime_statistic_5ec92ac3_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<LiveStreamSync> queryWrapper, LiveStreamSync statisticVo){
            if(statisticVo.getOrderInfoOrderInfoId1() != null){
                queryWrapper.eq(LiveStreamSync::getOrderInfoOrderInfoId1, statisticVo.getOrderInfoOrderInfoId1());
            }
        if(StringUtils.isNotEmpty(statisticVo.getPlatform())) {
                queryWrapper.eq(LiveStreamSync::getPlatform, statisticVo.getPlatform());
            }
            if(statisticVo.getSyncTime() != null){
                queryWrapper.eq(LiveStreamSync::getSyncTime, statisticVo.getSyncTime());
            }
    }



}






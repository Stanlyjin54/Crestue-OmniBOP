package com.kuafu.web.controller;

import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.resource.service.IStaticResourceService;
import com.kuafu.common.event.EventVo;
import com.kuafu.common.event.MyEventService;
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
import org.springframework.transaction.support.TransactionTemplate;
import com.kuafu.common.sensitive.SensitiveFilter;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.LiveStreamSync;
import com.kuafu.web.service.ILiveStreamSyncService;
import com.kuafu.web.vo.LiveStreamSyncPageVO;
import com.kuafu.web.vo.LiveStreamSyncVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 直播带货同步 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("LiveStreamSyncControllerService")
@RequiredArgsConstructor
public class LiveStreamSyncControllerService  {

    private final ILiveStreamSyncService liveStreamSyncService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( LiveStreamSyncPageVO pageVO){
        IPage<LiveStreamSync> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<LiveStreamSync> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<LiveStreamSync> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(LiveStreamSync::getLiveStreamSyncId);

            if(pageVO.getLiveStreamSyncId() != null){
                queryWrapper.eq(LiveStreamSync::getLiveStreamSyncId, pageVO.getLiveStreamSyncId());
            }


            if(pageVO.getOrderInfoOrderInfoId1() != null){
                queryWrapper.eq(LiveStreamSync::getOrderInfoOrderInfoId1, pageVO.getOrderInfoOrderInfoId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getPlatform())) {
                queryWrapper.eq(LiveStreamSync::getPlatform, pageVO.getPlatform());
            }


            if(pageVO.getSyncTime() != null){
                queryWrapper.eq(LiveStreamSync::getSyncTime, pageVO.getSyncTime());
            }

            return ResultUtils.success(liveStreamSyncService.page(page, queryWrapper));
        }


        public BaseResponse list(  LiveStreamSyncVO vo){
            LambdaQueryWrapper<LiveStreamSync> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getOrderInfoOrderInfoId1() != null){
                    queryWrapper.eq(LiveStreamSync::getOrderInfoOrderInfoId1, vo.getOrderInfoOrderInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getPlatform())) {
                    queryWrapper.eq(LiveStreamSync::getPlatform, vo.getPlatform());
                }
                if(vo.getSyncTime() != null){
                    queryWrapper.eq(LiveStreamSync::getSyncTime, vo.getSyncTime());
                }
                return ResultUtils.success(liveStreamSyncService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  LiveStreamSyncVO vo) {
                LiveStreamSync entity =  LiveStreamSync.builder()
                    .orderInfoOrderInfoId1(vo.getOrderInfoOrderInfoId1())
                    .platform(vo.getPlatform())
                    .syncTime(vo.getSyncTime())
                .build();








                boolean flag =this.liveStreamSyncService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("live_stream_sync").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getLiveStreamSyncId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( LiveStreamSyncVO vo) {
                LiveStreamSync entity =  LiveStreamSync.builder()
                    .liveStreamSyncId(vo.getLiveStreamSyncId())
                    .orderInfoOrderInfoId1(vo.getOrderInfoOrderInfoId1())
                    .platform(vo.getPlatform())
                    .syncTime(vo.getSyncTime())
                .build();



                        boolean flag = this.liveStreamSyncService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getLiveStreamSyncId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( LiveStreamSyncVO vo ) {
                LiveStreamSync entity = this.liveStreamSyncService.getById(vo.getLiveStreamSyncId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( LiveStreamSyncVO vo) {
                boolean flag = this.liveStreamSyncService.removeById(vo.getLiveStreamSyncId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

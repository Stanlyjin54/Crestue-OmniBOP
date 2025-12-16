package com.kuafu.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import java.io.Serializable;
import com.kuafu.common.login.IControllerService;

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
import org.apache.commons.lang3.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.kuafu.login.annotation.*;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.LiveStreamSyncAll;
import com.kuafu.web.service.ILiveStreamSyncAllService;
import com.kuafu.web.vo.LiveStreamSyncAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IOrderInfoService;
import com.kuafu.web.entity.OrderInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 直播带货同步 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("LiveStreamSyncAllControllerService")
@RequiredArgsConstructor
public class LiveStreamSyncAllControllerService implements IControllerService<LiveStreamSyncAll> {

    private final ILiveStreamSyncAllService liveStreamSyncAllService;





    public BaseResponse page( LiveStreamSyncAllPageVO pageVO){
        IPage<LiveStreamSyncAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<LiveStreamSyncAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("lss.live_stream_sync_id");
        LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getOrderNo())) {
                queryWrapper.like(LiveStreamSyncAll::getOrderNo, pageVO.getOrderNo());
            }
            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(LiveStreamSyncAll::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }
            if(pageVO.getSyncTime() != null){
                queryWrapper.eq(LiveStreamSyncAll::getSyncTime, pageVO.getSyncTime());
            }
            if(pageVO.getProfitAmount() != null){
                queryWrapper.eq(LiveStreamSyncAll::getProfitAmount, pageVO.getProfitAmount());
            }
            if(pageVO.getLiveStreamSyncId() != null){
                queryWrapper.eq(LiveStreamSyncAll::getLiveStreamSyncId, pageVO.getLiveStreamSyncId());
            }
        if(StringUtils.isNotEmpty(pageVO.getPlatform())) {
                queryWrapper.like(LiveStreamSyncAll::getPlatform, pageVO.getPlatform());
            }
            if(pageVO.getOrderStatusEnumOrderStatusEnumId1() != null){
                queryWrapper.eq(LiveStreamSyncAll::getOrderStatusEnumOrderStatusEnumId1, pageVO.getOrderStatusEnumOrderStatusEnumId1());
            }
            if(pageVO.getOrderInfoOrderInfoId1() != null){
                queryWrapper.eq(LiveStreamSyncAll::getOrderInfoOrderInfoId1, pageVO.getOrderInfoOrderInfoId1());
            }
            if(pageVO.getShippingFee() != null){
                queryWrapper.eq(LiveStreamSyncAll::getShippingFee, pageVO.getShippingFee());
            }
            if(pageVO.getOrderDate() != null){
                queryWrapper.eq(LiveStreamSyncAll::getOrderDate, pageVO.getOrderDate());
            }
        if(StringUtils.isNotEmpty(pageVO.getGeneralOrders())) {
                queryWrapper.like(LiveStreamSyncAll::getGeneralOrders, pageVO.getGeneralOrders());
            }
            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(LiveStreamSyncAll::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }
            if(pageVO.getTotalAmount() != null){
                queryWrapper.eq(LiveStreamSyncAll::getTotalAmount, pageVO.getTotalAmount());
            }
            if(pageVO.getCommission() != null){
                queryWrapper.eq(LiveStreamSyncAll::getCommission, pageVO.getCommission());
            }

                    liveStreamSyncAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(LiveStreamSyncAllPageVO vo){
            LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                    queryWrapper.eq(LiveStreamSyncAll::getOrderNo, vo.getOrderNo());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
                if(vo.getSyncTime() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getSyncTime, vo.getSyncTime());
                }
                if(vo.getProfitAmount() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getProfitAmount, vo.getProfitAmount());
                }
            if(StringUtils.isNotEmpty(vo.getPlatform())) {
                    queryWrapper.eq(LiveStreamSyncAll::getPlatform, vo.getPlatform());
                }
                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
                }
                if(vo.getOrderInfoOrderInfoId1() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getOrderInfoOrderInfoId1, vo.getOrderInfoOrderInfoId1());
                }
                if(vo.getShippingFee() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getShippingFee, vo.getShippingFee());
                }
                if(vo.getOrderDate() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getOrderDate, vo.getOrderDate());
                }
            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
                    queryWrapper.eq(LiveStreamSyncAll::getGeneralOrders, vo.getGeneralOrders());
                }
                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
                if(vo.getTotalAmount() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getTotalAmount, vo.getTotalAmount());
                }
                if(vo.getCommission() != null){
                    queryWrapper.eq(LiveStreamSyncAll::getCommission, vo.getCommission());
                }
                return ResultUtils.success(liveStreamSyncAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(LiveStreamSyncAllPageVO vo) {


                LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getOrderNo())) {
                                    queryWrapper.eq(LiveStreamSyncAll::getOrderNo, vo.getOrderNo());
                                }


                                if(vo.getUserInfoUserInfoId1() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                                }


                                if(vo.getSyncTime() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getSyncTime, vo.getSyncTime());
                                }


                                if(vo.getProfitAmount() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getProfitAmount, vo.getProfitAmount());
                                }


                                if(vo.getLiveStreamSyncId() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getLiveStreamSyncId, vo.getLiveStreamSyncId());
                                }


                            if(StringUtils.isNotEmpty(vo.getPlatform())) {
                                    queryWrapper.eq(LiveStreamSyncAll::getPlatform, vo.getPlatform());
                                }


                                if(vo.getOrderStatusEnumOrderStatusEnumId1() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getOrderStatusEnumOrderStatusEnumId1, vo.getOrderStatusEnumOrderStatusEnumId1());
                                }


                                if(vo.getOrderInfoOrderInfoId1() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getOrderInfoOrderInfoId1, vo.getOrderInfoOrderInfoId1());
                                }


                                if(vo.getShippingFee() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getShippingFee, vo.getShippingFee());
                                }


                                if(vo.getOrderDate() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getOrderDate, vo.getOrderDate());
                                }


                            if(StringUtils.isNotEmpty(vo.getGeneralOrders())) {
                                    queryWrapper.eq(LiveStreamSyncAll::getGeneralOrders, vo.getGeneralOrders());
                                }


                                if(vo.getCustomerInfoCustomerInfoId1() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                                }


                                if(vo.getTotalAmount() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getTotalAmount, vo.getTotalAmount());
                                }


                                if(vo.getCommission() != null){
                                    queryWrapper.eq(LiveStreamSyncAll::getCommission, vo.getCommission());
                                }

                // LiveStreamSyncAll entity = this.liveStreamSyncAllService.getById(liveStreamSyncId);
                    final Page<LiveStreamSyncAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    liveStreamSyncAllService.pageNew(page, vo, queryWrapper);
                    final List<LiveStreamSyncAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public LiveStreamSyncAll getById(Serializable liveStreamSyncId) {

                    LambdaQueryWrapper<LiveStreamSyncAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(LiveStreamSyncAll::getLiveStreamSyncId, liveStreamSyncId);

                    final Page<LiveStreamSyncAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    liveStreamSyncAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<LiveStreamSyncAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

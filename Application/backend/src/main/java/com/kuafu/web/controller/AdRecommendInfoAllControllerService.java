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
import com.kuafu.web.entity.AdRecommendInfoAll;
import com.kuafu.web.service.IAdRecommendInfoAllService;
import com.kuafu.web.vo.AdRecommendInfoAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 广告推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("AdRecommendInfoAllControllerService")
@RequiredArgsConstructor
public class AdRecommendInfoAllControllerService implements IControllerService<AdRecommendInfoAll> {

    private final IAdRecommendInfoAllService adRecommendInfoAllService;





    public BaseResponse page( AdRecommendInfoAllPageVO pageVO){
        IPage<AdRecommendInfoAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<AdRecommendInfoAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ari.ad_recommend_info_id");
        LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper = objectQueryWrapper.lambda();


            if(pageVO.getAdRecommendInfoId() != null){
                queryWrapper.eq(AdRecommendInfoAll::getAdRecommendInfoId, pageVO.getAdRecommendInfoId());
            }
        if(StringUtils.isNotEmpty(pageVO.getAdUrl())) {
                queryWrapper.like(AdRecommendInfoAll::getAdUrl, pageVO.getAdUrl());
            }
            if(pageVO.getCpcFee() != null){
                queryWrapper.eq(AdRecommendInfoAll::getCpcFee, pageVO.getCpcFee());
            }
        if(StringUtils.isNotEmpty(pageVO.getAdTitle())) {
                queryWrapper.like(AdRecommendInfoAll::getAdTitle, pageVO.getAdTitle());
            }

                    adRecommendInfoAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(AdRecommendInfoAllPageVO vo){
            LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getAdUrl())) {
                    queryWrapper.eq(AdRecommendInfoAll::getAdUrl, vo.getAdUrl());
                }
                if(vo.getCpcFee() != null){
                    queryWrapper.eq(AdRecommendInfoAll::getCpcFee, vo.getCpcFee());
                }
            if(StringUtils.isNotEmpty(vo.getAdTitle())) {
                    queryWrapper.eq(AdRecommendInfoAll::getAdTitle, vo.getAdTitle());
                }
                return ResultUtils.success(adRecommendInfoAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(AdRecommendInfoAllPageVO vo) {


                LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper = new LambdaQueryWrapper<>();


                                if(vo.getAdRecommendInfoId() != null){
                                    queryWrapper.eq(AdRecommendInfoAll::getAdRecommendInfoId, vo.getAdRecommendInfoId());
                                }


                            if(StringUtils.isNotEmpty(vo.getAdUrl())) {
                                    queryWrapper.eq(AdRecommendInfoAll::getAdUrl, vo.getAdUrl());
                                }


                                if(vo.getCpcFee() != null){
                                    queryWrapper.eq(AdRecommendInfoAll::getCpcFee, vo.getCpcFee());
                                }


                            if(StringUtils.isNotEmpty(vo.getAdTitle())) {
                                    queryWrapper.eq(AdRecommendInfoAll::getAdTitle, vo.getAdTitle());
                                }

                // AdRecommendInfoAll entity = this.adRecommendInfoAllService.getById(adRecommendInfoId);
                    final Page<AdRecommendInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    adRecommendInfoAllService.pageNew(page, vo, queryWrapper);
                    final List<AdRecommendInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public AdRecommendInfoAll getById(Serializable adRecommendInfoId) {

                    LambdaQueryWrapper<AdRecommendInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(AdRecommendInfoAll::getAdRecommendInfoId, adRecommendInfoId);

                    final Page<AdRecommendInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    adRecommendInfoAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<AdRecommendInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

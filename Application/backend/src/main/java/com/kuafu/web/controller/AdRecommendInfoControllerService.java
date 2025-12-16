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
import com.kuafu.web.entity.AdRecommendInfo;
import com.kuafu.web.service.IAdRecommendInfoService;
import com.kuafu.web.vo.AdRecommendInfoPageVO;
import com.kuafu.web.vo.AdRecommendInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 广告推荐 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("AdRecommendInfoControllerService")
@RequiredArgsConstructor
public class AdRecommendInfoControllerService  {

    private final IAdRecommendInfoService adRecommendInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( AdRecommendInfoPageVO pageVO){
        IPage<AdRecommendInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<AdRecommendInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<AdRecommendInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(AdRecommendInfo::getAdRecommendInfoId);

            if(pageVO.getAdRecommendInfoId() != null){
                queryWrapper.eq(AdRecommendInfo::getAdRecommendInfoId, pageVO.getAdRecommendInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getAdTitle())) {
                queryWrapper.eq(AdRecommendInfo::getAdTitle, pageVO.getAdTitle());
            }


        if(StringUtils.isNotEmpty(pageVO.getAdUrl())) {
                queryWrapper.eq(AdRecommendInfo::getAdUrl, pageVO.getAdUrl());
            }


            if(pageVO.getCpcFee() != null){
                queryWrapper.eq(AdRecommendInfo::getCpcFee, pageVO.getCpcFee());
            }

            return ResultUtils.success(adRecommendInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  AdRecommendInfoVO vo){
            LambdaQueryWrapper<AdRecommendInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getAdTitle())) {
                    queryWrapper.eq(AdRecommendInfo::getAdTitle, vo.getAdTitle());
                }
            if(StringUtils.isNotEmpty(vo.getAdUrl())) {
                    queryWrapper.eq(AdRecommendInfo::getAdUrl, vo.getAdUrl());
                }
                if(vo.getCpcFee() != null){
                    queryWrapper.eq(AdRecommendInfo::getCpcFee, vo.getCpcFee());
                }
                return ResultUtils.success(adRecommendInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  AdRecommendInfoVO vo) {
                AdRecommendInfo entity =  AdRecommendInfo.builder()
                    .adTitle(vo.getAdTitle())
                    .adUrl(vo.getAdUrl())
                    .cpcFee(vo.getCpcFee())
                .build();







                boolean  f = transactionTemplate.execute(status -> {
                    try {

                boolean flag =this.adRecommendInfoService.save(entity);


                final List<StaticResource> AdImageImage = vo.getAdImage();
                if (AdImageImage  != null && !AdImageImage .isEmpty()) {
                    AdImageImage.forEach(i -> {
                        i.setRelateTableColumnName("ad_image");
                        i.setRelatedTableName("ad_recommend_info");
                        i.setResourceId(null);
                        i.setRelatedTableKey(entity.getAdRecommendInfoId());
                    });
                }
                staticResourceService.saveBatch(AdImageImage);




                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        status.setRollbackOnly();
                        throw e;
                    }
                });


                if(
                f
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("ad_recommend_info").data(entity).build());
                }

                return
                                f
                              
                ? ResultUtils.success(entity.getAdRecommendInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( AdRecommendInfoVO vo) {
                AdRecommendInfo entity =  AdRecommendInfo.builder()
                    .adRecommendInfoId(vo.getAdRecommendInfoId())
                    .adTitle(vo.getAdTitle())
                    .adUrl(vo.getAdUrl())
                    .cpcFee(vo.getCpcFee())
                .build();


                boolean  f = transactionTemplate.execute(status -> {
                    try {

                        boolean flag = this.adRecommendInfoService.saveOrUpdate(entity);




                        final LambdaQueryWrapper<StaticResource> ad_imagequeryWrapper = new LambdaQueryWrapper<>();
                        ad_imagequeryWrapper.eq(StaticResource::getRelatedTableName,"ad_recommend_info").eq(StaticResource::getRelateTableColumnName,"ad_image").eq(StaticResource::getRelatedTableKey,entity.getAdRecommendInfoId());
                        staticResourceService.remove(ad_imagequeryWrapper);


                        final List<StaticResource> AdImageImage = vo.getAdImage();
                        if (AdImageImage  != null && !AdImageImage .isEmpty()) {
                            AdImageImage.forEach(i -> {
                                i.setRelateTableColumnName("ad_image");
                                i.setRelatedTableName("ad_recommend_info");
                                i.setRelatedTableKey(entity.getAdRecommendInfoId());
                            });
                        }
                        staticResourceService.saveBatch(AdImageImage);




                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        status.setRollbackOnly();
                        throw e;
                    }
                });


                return  
                f
                         ? ResultUtils.success(entity.getAdRecommendInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( AdRecommendInfoVO vo ) {
                AdRecommendInfo entity = this.adRecommendInfoService.getById(vo.getAdRecommendInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( AdRecommendInfoVO vo) {
                boolean flag = this.adRecommendInfoService.removeById(vo.getAdRecommendInfoId());




                final LambdaQueryWrapper<StaticResource> ad_imagequeryWrapper = new LambdaQueryWrapper<>();
                ad_imagequeryWrapper.eq(StaticResource::getRelatedTableName,"ad_recommend_info").eq(StaticResource::getRelateTableColumnName,"ad_image").eq(StaticResource::getRelatedTableKey,vo.getAdRecommendInfoId());
                staticResourceService.remove(ad_imagequeryWrapper);


                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

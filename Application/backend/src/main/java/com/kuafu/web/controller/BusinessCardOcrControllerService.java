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
import com.kuafu.web.entity.BusinessCardOcr;
import com.kuafu.web.service.IBusinessCardOcrService;
import com.kuafu.web.vo.BusinessCardOcrPageVO;
import com.kuafu.web.vo.BusinessCardOcrVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 名片OCR建档 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("BusinessCardOcrControllerService")
@RequiredArgsConstructor
public class BusinessCardOcrControllerService  {

    private final IBusinessCardOcrService businessCardOcrService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( BusinessCardOcrPageVO pageVO){
        IPage<BusinessCardOcr> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<BusinessCardOcr> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<BusinessCardOcr> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(BusinessCardOcr::getBusinessCardOcrId);

            if(pageVO.getBusinessCardOcrId() != null){
                queryWrapper.eq(BusinessCardOcr::getBusinessCardOcrId, pageVO.getBusinessCardOcrId());
            }


            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                queryWrapper.eq(BusinessCardOcr::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getOcrResult())) {
                queryWrapper.eq(BusinessCardOcr::getOcrResult, pageVO.getOcrResult());
            }

            return ResultUtils.success(businessCardOcrService.page(page, queryWrapper));
        }


        public BaseResponse list(  BusinessCardOcrVO vo){
            LambdaQueryWrapper<BusinessCardOcr> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getCustomerInfoCustomerInfoId1() != null){
                    queryWrapper.eq(BusinessCardOcr::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getOcrResult())) {
                    queryWrapper.eq(BusinessCardOcr::getOcrResult, vo.getOcrResult());
                }
                return ResultUtils.success(businessCardOcrService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  BusinessCardOcrVO vo) {
                BusinessCardOcr entity =  BusinessCardOcr.builder()
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .ocrResult(vo.getOcrResult())
                .build();







                boolean  f = transactionTemplate.execute(status -> {
                    try {

                boolean flag =this.businessCardOcrService.save(entity);


                final List<StaticResource> CardImageImage = vo.getCardImage();
                if (CardImageImage  != null && !CardImageImage .isEmpty()) {
                    CardImageImage.forEach(i -> {
                        i.setRelateTableColumnName("card_image");
                        i.setRelatedTableName("business_card_ocr");
                        i.setResourceId(null);
                        i.setRelatedTableKey(entity.getBusinessCardOcrId());
                    });
                }
                staticResourceService.saveBatch(CardImageImage);




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
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("business_card_ocr").data(entity).build());
                }

                return
                                f
                              
                ? ResultUtils.success(entity.getBusinessCardOcrId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( BusinessCardOcrVO vo) {
                BusinessCardOcr entity =  BusinessCardOcr.builder()
                    .businessCardOcrId(vo.getBusinessCardOcrId())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .ocrResult(vo.getOcrResult())
                .build();


                boolean  f = transactionTemplate.execute(status -> {
                    try {

                        boolean flag = this.businessCardOcrService.saveOrUpdate(entity);




                        final LambdaQueryWrapper<StaticResource> card_imagequeryWrapper = new LambdaQueryWrapper<>();
                        card_imagequeryWrapper.eq(StaticResource::getRelatedTableName,"business_card_ocr").eq(StaticResource::getRelateTableColumnName,"card_image").eq(StaticResource::getRelatedTableKey,entity.getBusinessCardOcrId());
                        staticResourceService.remove(card_imagequeryWrapper);


                        final List<StaticResource> CardImageImage = vo.getCardImage();
                        if (CardImageImage  != null && !CardImageImage .isEmpty()) {
                            CardImageImage.forEach(i -> {
                                i.setRelateTableColumnName("card_image");
                                i.setRelatedTableName("business_card_ocr");
                                i.setRelatedTableKey(entity.getBusinessCardOcrId());
                            });
                        }
                        staticResourceService.saveBatch(CardImageImage);




                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        status.setRollbackOnly();
                        throw e;
                    }
                });


                return  
                f
                         ? ResultUtils.success(entity.getBusinessCardOcrId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( BusinessCardOcrVO vo ) {
                BusinessCardOcr entity = this.businessCardOcrService.getById(vo.getBusinessCardOcrId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( BusinessCardOcrVO vo) {
                boolean flag = this.businessCardOcrService.removeById(vo.getBusinessCardOcrId());




                final LambdaQueryWrapper<StaticResource> card_imagequeryWrapper = new LambdaQueryWrapper<>();
                card_imagequeryWrapper.eq(StaticResource::getRelatedTableName,"business_card_ocr").eq(StaticResource::getRelateTableColumnName,"card_image").eq(StaticResource::getRelatedTableKey,vo.getBusinessCardOcrId());
                staticResourceService.remove(card_imagequeryWrapper);


                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

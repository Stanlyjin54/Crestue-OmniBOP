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
import com.kuafu.web.entity.ProductInfo;
import com.kuafu.web.service.IProductInfoService;
import com.kuafu.web.vo.ProductInfoPageVO;
import com.kuafu.web.vo.ProductInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 商品信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("ProductInfoControllerService")
@RequiredArgsConstructor
public class ProductInfoControllerService  {

    private final IProductInfoService productInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( ProductInfoPageVO pageVO){
        IPage<ProductInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<ProductInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<ProductInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(ProductInfo::getProductInfoId);

            if(pageVO.getProductInfoId() != null){
                queryWrapper.eq(ProductInfo::getProductInfoId, pageVO.getProductInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getProductName())) {
//                        queryWrapper.like(ProductInfo::getProductName, pageVO.getProductName());
                        String s_string = pageVO.getProductName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(ProductInfo::getProductName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
                queryWrapper.eq(ProductInfo::getBarcode, pageVO.getBarcode());
            }


        if(StringUtils.isNotEmpty(pageVO.getCategory())) {
                queryWrapper.eq(ProductInfo::getCategory, pageVO.getCategory());
            }


        if(StringUtils.isNotEmpty(pageVO.getBrand())) {
                queryWrapper.eq(ProductInfo::getBrand, pageVO.getBrand());
            }


        if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
                queryWrapper.eq(ProductInfo::getSpecification, pageVO.getSpecification());
            }


        if(StringUtils.isNotEmpty(pageVO.getUnit())) {
                queryWrapper.eq(ProductInfo::getUnit, pageVO.getUnit());
            }


        if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                queryWrapper.eq(ProductInfo::getSerialNumber, pageVO.getSerialNumber());
            }


            if(pageVO.getShelfLife() != null){
                queryWrapper.eq(ProductInfo::getShelfLife, pageVO.getShelfLife());
            }


            if(pageVO.getMinStock() != null){
                queryWrapper.eq(ProductInfo::getMinStock, pageVO.getMinStock());
            }


            if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
                queryWrapper.eq(ProductInfo::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
            }

            return ResultUtils.success(productInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  ProductInfoVO vo){
            LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getProductName())) {
                            queryWrapper.like(ProductInfo::getProductName, vo.getProductName());
                }
            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                    queryWrapper.eq(ProductInfo::getBarcode, vo.getBarcode());
                }
            if(StringUtils.isNotEmpty(vo.getCategory())) {
                    queryWrapper.eq(ProductInfo::getCategory, vo.getCategory());
                }
            if(StringUtils.isNotEmpty(vo.getBrand())) {
                    queryWrapper.eq(ProductInfo::getBrand, vo.getBrand());
                }
            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                    queryWrapper.eq(ProductInfo::getSpecification, vo.getSpecification());
                }
            if(StringUtils.isNotEmpty(vo.getUnit())) {
                    queryWrapper.eq(ProductInfo::getUnit, vo.getUnit());
                }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                    queryWrapper.eq(ProductInfo::getSerialNumber, vo.getSerialNumber());
                }
                if(vo.getShelfLife() != null){
                    queryWrapper.eq(ProductInfo::getShelfLife, vo.getShelfLife());
                }
                if(vo.getMinStock() != null){
                    queryWrapper.eq(ProductInfo::getMinStock, vo.getMinStock());
                }
                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                    queryWrapper.eq(ProductInfo::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                }
                return ResultUtils.success(productInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  ProductInfoVO vo) {
                ProductInfo entity =  ProductInfo.builder()
                    .productName(vo.getProductName())
                    .barcode(vo.getBarcode())
                    .category(vo.getCategory())
                    .brand(vo.getBrand())
                    .specification(vo.getSpecification())
                    .unit(vo.getUnit())
                    .serialNumber(vo.getSerialNumber())
                    .shelfLife(vo.getShelfLife())
                    .minStock(vo.getMinStock())
                    .productStatusEnumProductStatusEnumId1(vo.getProductStatusEnumProductStatusEnumId1())
                .build();







                boolean  f = transactionTemplate.execute(status -> {
                    try {

                boolean flag =this.productInfoService.save(entity);


                final List<StaticResource> ImagesImage = vo.getImages();
                if (ImagesImage  != null && !ImagesImage .isEmpty()) {
                    ImagesImage.forEach(i -> {
                        i.setRelateTableColumnName("images");
                        i.setRelatedTableName("product_info");
                        i.setResourceId(null);
                        i.setRelatedTableKey(entity.getProductInfoId());
                    });
                }
                staticResourceService.saveBatch(ImagesImage);




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
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("product_info").data(entity).build());
                }

                return
                                f
                              
                ? ResultUtils.success(entity.getProductInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( ProductInfoVO vo) {
                ProductInfo entity =  ProductInfo.builder()
                    .productInfoId(vo.getProductInfoId())
                    .productName(vo.getProductName())
                    .barcode(vo.getBarcode())
                    .category(vo.getCategory())
                    .brand(vo.getBrand())
                    .specification(vo.getSpecification())
                    .unit(vo.getUnit())
                    .serialNumber(vo.getSerialNumber())
                    .shelfLife(vo.getShelfLife())
                    .minStock(vo.getMinStock())
                    .productStatusEnumProductStatusEnumId1(vo.getProductStatusEnumProductStatusEnumId1())
                .build();


                boolean  f = transactionTemplate.execute(status -> {
                    try {

                        boolean flag = this.productInfoService.saveOrUpdate(entity);




                        final LambdaQueryWrapper<StaticResource> imagesqueryWrapper = new LambdaQueryWrapper<>();
                        imagesqueryWrapper.eq(StaticResource::getRelatedTableName,"product_info").eq(StaticResource::getRelateTableColumnName,"images").eq(StaticResource::getRelatedTableKey,entity.getProductInfoId());
                        staticResourceService.remove(imagesqueryWrapper);


                        final List<StaticResource> ImagesImage = vo.getImages();
                        if (ImagesImage  != null && !ImagesImage .isEmpty()) {
                            ImagesImage.forEach(i -> {
                                i.setRelateTableColumnName("images");
                                i.setRelatedTableName("product_info");
                                i.setRelatedTableKey(entity.getProductInfoId());
                            });
                        }
                        staticResourceService.saveBatch(ImagesImage);




                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        status.setRollbackOnly();
                        throw e;
                    }
                });


                return  
                f
                         ? ResultUtils.success(entity.getProductInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( ProductInfoVO vo ) {
                ProductInfo entity = this.productInfoService.getById(vo.getProductInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( ProductInfoVO vo) {
                boolean flag = this.productInfoService.removeById(vo.getProductInfoId());




                final LambdaQueryWrapper<StaticResource> imagesqueryWrapper = new LambdaQueryWrapper<>();
                imagesqueryWrapper.eq(StaticResource::getRelatedTableName,"product_info").eq(StaticResource::getRelateTableColumnName,"images").eq(StaticResource::getRelatedTableKey,vo.getProductInfoId());
                staticResourceService.remove(imagesqueryWrapper);


                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

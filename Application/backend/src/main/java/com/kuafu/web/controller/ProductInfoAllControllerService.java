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
import com.kuafu.web.entity.ProductInfoAll;
import com.kuafu.web.service.IProductInfoAllService;
import com.kuafu.web.vo.ProductInfoAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IProductStatusEnumService;
import com.kuafu.web.entity.ProductStatusEnum;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 商品信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("ProductInfoAllControllerService")
@RequiredArgsConstructor
public class ProductInfoAllControllerService implements IControllerService<ProductInfoAll> {

    private final IProductInfoAllService productInfoAllService;





    public BaseResponse page( ProductInfoAllPageVO pageVO){
        IPage<ProductInfoAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<ProductInfoAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("pi.product_info_id");
        LambdaQueryWrapper<ProductInfoAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getUnit())) {
                queryWrapper.like(ProductInfoAll::getUnit, pageVO.getUnit());
            }
            if(pageVO.getProductInfoId() != null){
                queryWrapper.eq(ProductInfoAll::getProductInfoId, pageVO.getProductInfoId());
            }
            if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
                queryWrapper.eq(ProductInfoAll::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {

                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(ProductInfoAll::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
                queryWrapper.like(ProductInfoAll::getSpecification, pageVO.getSpecification());
            }
        if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                queryWrapper.like(ProductInfoAll::getSerialNumber, pageVO.getSerialNumber());
            }
            if(pageVO.getMinStock() != null){
                queryWrapper.eq(ProductInfoAll::getMinStock, pageVO.getMinStock());
            }
        if(StringUtils.isNotEmpty(pageVO.getCategory())) {
                queryWrapper.like(ProductInfoAll::getCategory, pageVO.getCategory());
            }
        if(StringUtils.isNotEmpty(pageVO.getProductName())) {

                        String s_string = pageVO.getProductName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(ProductInfoAll::getProductName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
        if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
                queryWrapper.like(ProductInfoAll::getBarcode, pageVO.getBarcode());
            }
        if(StringUtils.isNotEmpty(pageVO.getBrand())) {
                queryWrapper.like(ProductInfoAll::getBrand, pageVO.getBrand());
            }
            if(pageVO.getShelfLife() != null){
                queryWrapper.eq(ProductInfoAll::getShelfLife, pageVO.getShelfLife());
            }

                    productInfoAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(ProductInfoAllPageVO vo){
            LambdaQueryWrapper<ProductInfoAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getUnit())) {
                    queryWrapper.eq(ProductInfoAll::getUnit, vo.getUnit());
                }
                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                    queryWrapper.eq(ProductInfoAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(ProductInfoAll::getStatusName, vo.getStatusName());
                }
            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                    queryWrapper.eq(ProductInfoAll::getSpecification, vo.getSpecification());
                }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                    queryWrapper.eq(ProductInfoAll::getSerialNumber, vo.getSerialNumber());
                }
                if(vo.getMinStock() != null){
                    queryWrapper.eq(ProductInfoAll::getMinStock, vo.getMinStock());
                }
            if(StringUtils.isNotEmpty(vo.getCategory())) {
                    queryWrapper.eq(ProductInfoAll::getCategory, vo.getCategory());
                }
            if(StringUtils.isNotEmpty(vo.getProductName())) {
                            queryWrapper.like(ProductInfoAll::getProductName, vo.getProductName());
                }
            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                    queryWrapper.eq(ProductInfoAll::getBarcode, vo.getBarcode());
                }
            if(StringUtils.isNotEmpty(vo.getBrand())) {
                    queryWrapper.eq(ProductInfoAll::getBrand, vo.getBrand());
                }
                if(vo.getShelfLife() != null){
                    queryWrapper.eq(ProductInfoAll::getShelfLife, vo.getShelfLife());
                }
                return ResultUtils.success(productInfoAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(ProductInfoAllPageVO vo) {


                LambdaQueryWrapper<ProductInfoAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getUnit())) {
                                    queryWrapper.eq(ProductInfoAll::getUnit, vo.getUnit());
                                }


                                if(vo.getProductInfoId() != null){
                                    queryWrapper.eq(ProductInfoAll::getProductInfoId, vo.getProductInfoId());
                                }


                                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                                    queryWrapper.eq(ProductInfoAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                                            queryWrapper.like(ProductInfoAll::getStatusName, vo.getStatusName());
                                }


                            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                                    queryWrapper.eq(ProductInfoAll::getSpecification, vo.getSpecification());
                                }


                            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                                    queryWrapper.eq(ProductInfoAll::getSerialNumber, vo.getSerialNumber());
                                }


                                if(vo.getMinStock() != null){
                                    queryWrapper.eq(ProductInfoAll::getMinStock, vo.getMinStock());
                                }


                            if(StringUtils.isNotEmpty(vo.getCategory())) {
                                    queryWrapper.eq(ProductInfoAll::getCategory, vo.getCategory());
                                }


                            if(StringUtils.isNotEmpty(vo.getProductName())) {
                                            queryWrapper.like(ProductInfoAll::getProductName, vo.getProductName());
                                }


                            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                                    queryWrapper.eq(ProductInfoAll::getBarcode, vo.getBarcode());
                                }


                            if(StringUtils.isNotEmpty(vo.getBrand())) {
                                    queryWrapper.eq(ProductInfoAll::getBrand, vo.getBrand());
                                }


                                if(vo.getShelfLife() != null){
                                    queryWrapper.eq(ProductInfoAll::getShelfLife, vo.getShelfLife());
                                }

                // ProductInfoAll entity = this.productInfoAllService.getById(productInfoId);
                    final Page<ProductInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    productInfoAllService.pageNew(page, vo, queryWrapper);
                    final List<ProductInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public ProductInfoAll getById(Serializable productInfoId) {

                    LambdaQueryWrapper<ProductInfoAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(ProductInfoAll::getProductInfoId, productInfoId);

                    final Page<ProductInfoAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    productInfoAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<ProductInfoAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

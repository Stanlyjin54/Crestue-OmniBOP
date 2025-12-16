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
import com.kuafu.web.entity.ProcurementSuggestionAll;
import com.kuafu.web.service.IProcurementSuggestionAllService;
import com.kuafu.web.vo.ProcurementSuggestionAllPageVO;
import com.kuafu.web.entity.SelectVo;
import com.kuafu.web.service.IProductInfoService;
import com.kuafu.web.entity.ProductInfo;
import com.kuafu.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.util.QueryUtils;





/**
 * <p> 采购建议 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("ProcurementSuggestionAllControllerService")
@RequiredArgsConstructor
public class ProcurementSuggestionAllControllerService implements IControllerService<ProcurementSuggestionAll> {

    private final IProcurementSuggestionAllService procurementSuggestionAllService;





    public BaseResponse page( ProcurementSuggestionAllPageVO pageVO){
        IPage<ProcurementSuggestionAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<ProcurementSuggestionAll> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        objectQueryWrapper.orderByDesc("ps.procurement_suggestion_id");
        LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper = objectQueryWrapper.lambda();


        if(StringUtils.isNotEmpty(pageVO.getReason())) {
                queryWrapper.like(ProcurementSuggestionAll::getReason, pageVO.getReason());
            }
            if(pageVO.getProductStatusEnumProductStatusEnumId1() != null){
                queryWrapper.eq(ProcurementSuggestionAll::getProductStatusEnumProductStatusEnumId1, pageVO.getProductStatusEnumProductStatusEnumId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getSpecification())) {
                queryWrapper.like(ProcurementSuggestionAll::getSpecification, pageVO.getSpecification());
            }
        if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                queryWrapper.like(ProcurementSuggestionAll::getSerialNumber, pageVO.getSerialNumber());
            }
            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(ProcurementSuggestionAll::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }
        if(StringUtils.isNotEmpty(pageVO.getProductName())) {

                        String s_string = pageVO.getProductName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(ProcurementSuggestionAll::getProductName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }
            if(pageVO.getSuggestDate() != null){
                queryWrapper.eq(ProcurementSuggestionAll::getSuggestDate, pageVO.getSuggestDate());
            }
            if(pageVO.getSuggestQuantity() != null){
                queryWrapper.eq(ProcurementSuggestionAll::getSuggestQuantity, pageVO.getSuggestQuantity());
            }
            if(pageVO.getShelfLife() != null){
                queryWrapper.eq(ProcurementSuggestionAll::getShelfLife, pageVO.getShelfLife());
            }
        if(StringUtils.isNotEmpty(pageVO.getUnit())) {
                queryWrapper.like(ProcurementSuggestionAll::getUnit, pageVO.getUnit());
            }
            if(pageVO.getMinStock() != null){
                queryWrapper.eq(ProcurementSuggestionAll::getMinStock, pageVO.getMinStock());
            }
        if(StringUtils.isNotEmpty(pageVO.getCategory())) {
                queryWrapper.like(ProcurementSuggestionAll::getCategory, pageVO.getCategory());
            }
            if(pageVO.getProcurementSuggestionId() != null){
                queryWrapper.eq(ProcurementSuggestionAll::getProcurementSuggestionId, pageVO.getProcurementSuggestionId());
            }
        if(StringUtils.isNotEmpty(pageVO.getBarcode())) {
                queryWrapper.like(ProcurementSuggestionAll::getBarcode, pageVO.getBarcode());
            }
        if(StringUtils.isNotEmpty(pageVO.getBrand())) {
                queryWrapper.like(ProcurementSuggestionAll::getBrand, pageVO.getBrand());
            }

                    procurementSuggestionAllService.pageNew(page, pageVO, queryWrapper);
                    page.getRecords().forEach(item -> {
                    });


            return ResultUtils.success(page);
        }

        public BaseResponse list(ProcurementSuggestionAllPageVO vo){
            LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getReason())) {
                    queryWrapper.eq(ProcurementSuggestionAll::getReason, vo.getReason());
                }
                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                    queryWrapper.eq(ProcurementSuggestionAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                }
            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                    queryWrapper.eq(ProcurementSuggestionAll::getSpecification, vo.getSpecification());
                }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                    queryWrapper.eq(ProcurementSuggestionAll::getSerialNumber, vo.getSerialNumber());
                }
                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(ProcurementSuggestionAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getProductName())) {
                            queryWrapper.like(ProcurementSuggestionAll::getProductName, vo.getProductName());
                }
                if(vo.getSuggestDate() != null){
                    queryWrapper.eq(ProcurementSuggestionAll::getSuggestDate, vo.getSuggestDate());
                }
                if(vo.getSuggestQuantity() != null){
                    queryWrapper.eq(ProcurementSuggestionAll::getSuggestQuantity, vo.getSuggestQuantity());
                }
                if(vo.getShelfLife() != null){
                    queryWrapper.eq(ProcurementSuggestionAll::getShelfLife, vo.getShelfLife());
                }
            if(StringUtils.isNotEmpty(vo.getUnit())) {
                    queryWrapper.eq(ProcurementSuggestionAll::getUnit, vo.getUnit());
                }
                if(vo.getMinStock() != null){
                    queryWrapper.eq(ProcurementSuggestionAll::getMinStock, vo.getMinStock());
                }
            if(StringUtils.isNotEmpty(vo.getCategory())) {
                    queryWrapper.eq(ProcurementSuggestionAll::getCategory, vo.getCategory());
                }
            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                    queryWrapper.eq(ProcurementSuggestionAll::getBarcode, vo.getBarcode());
                }
            if(StringUtils.isNotEmpty(vo.getBrand())) {
                    queryWrapper.eq(ProcurementSuggestionAll::getBrand, vo.getBrand());
                }
                return ResultUtils.success(procurementSuggestionAllService.selectListNew(new PageRequest(),queryWrapper,false));
            }


            public BaseResponse get(ProcurementSuggestionAllPageVO vo) {


                LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper = new LambdaQueryWrapper<>();


                            if(StringUtils.isNotEmpty(vo.getReason())) {
                                    queryWrapper.eq(ProcurementSuggestionAll::getReason, vo.getReason());
                                }


                                if(vo.getProductStatusEnumProductStatusEnumId1() != null){
                                    queryWrapper.eq(ProcurementSuggestionAll::getProductStatusEnumProductStatusEnumId1, vo.getProductStatusEnumProductStatusEnumId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getSpecification())) {
                                    queryWrapper.eq(ProcurementSuggestionAll::getSpecification, vo.getSpecification());
                                }


                            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                                    queryWrapper.eq(ProcurementSuggestionAll::getSerialNumber, vo.getSerialNumber());
                                }


                                if(vo.getProductInfoProductInfoId1() != null){
                                    queryWrapper.eq(ProcurementSuggestionAll::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                                }


                            if(StringUtils.isNotEmpty(vo.getProductName())) {
                                            queryWrapper.like(ProcurementSuggestionAll::getProductName, vo.getProductName());
                                }


                                if(vo.getSuggestDate() != null){
                                    queryWrapper.eq(ProcurementSuggestionAll::getSuggestDate, vo.getSuggestDate());
                                }


                                if(vo.getSuggestQuantity() != null){
                                    queryWrapper.eq(ProcurementSuggestionAll::getSuggestQuantity, vo.getSuggestQuantity());
                                }


                                if(vo.getShelfLife() != null){
                                    queryWrapper.eq(ProcurementSuggestionAll::getShelfLife, vo.getShelfLife());
                                }


                            if(StringUtils.isNotEmpty(vo.getUnit())) {
                                    queryWrapper.eq(ProcurementSuggestionAll::getUnit, vo.getUnit());
                                }


                                if(vo.getMinStock() != null){
                                    queryWrapper.eq(ProcurementSuggestionAll::getMinStock, vo.getMinStock());
                                }


                            if(StringUtils.isNotEmpty(vo.getCategory())) {
                                    queryWrapper.eq(ProcurementSuggestionAll::getCategory, vo.getCategory());
                                }


                                if(vo.getProcurementSuggestionId() != null){
                                    queryWrapper.eq(ProcurementSuggestionAll::getProcurementSuggestionId, vo.getProcurementSuggestionId());
                                }


                            if(StringUtils.isNotEmpty(vo.getBarcode())) {
                                    queryWrapper.eq(ProcurementSuggestionAll::getBarcode, vo.getBarcode());
                                }


                            if(StringUtils.isNotEmpty(vo.getBrand())) {
                                    queryWrapper.eq(ProcurementSuggestionAll::getBrand, vo.getBrand());
                                }

                // ProcurementSuggestionAll entity = this.procurementSuggestionAllService.getById(procurementSuggestionId);
                    final Page<ProcurementSuggestionAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    procurementSuggestionAllService.pageNew(page, vo, queryWrapper);
                    final List<ProcurementSuggestionAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? ResultUtils.success(records.get(0)) : ResultUtils.error(ErrorCode.OPERATION_ERROR);

                    // return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

            public ProcurementSuggestionAll getById(Serializable procurementSuggestionId) {

                    LambdaQueryWrapper<ProcurementSuggestionAll> queryWrapper = new LambdaQueryWrapper<>();

                            queryWrapper.eq(ProcurementSuggestionAll::getProcurementSuggestionId, procurementSuggestionId);

                    final Page<ProcurementSuggestionAll> page = new Page<>();
                    page.setCurrent(1);
                    page.setSize(1);
                    procurementSuggestionAllService.pageNew(page, new PageRequest() , queryWrapper);
                    final List<ProcurementSuggestionAll> records = page.getRecords();
                    return ObjectUtils.isNotEmpty(records) ? records.get(0) : null;

            }


        }

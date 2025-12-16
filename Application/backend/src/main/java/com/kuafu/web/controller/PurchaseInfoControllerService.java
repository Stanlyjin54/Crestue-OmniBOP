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
import com.kuafu.web.entity.PurchaseInfo;
import com.kuafu.web.service.IPurchaseInfoService;
import com.kuafu.web.vo.PurchaseInfoPageVO;
import com.kuafu.web.vo.PurchaseInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 采购信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("PurchaseInfoControllerService")
@RequiredArgsConstructor
public class PurchaseInfoControllerService  {

    private final IPurchaseInfoService purchaseInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( PurchaseInfoPageVO pageVO){
        IPage<PurchaseInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<PurchaseInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<PurchaseInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(PurchaseInfo::getPurchaseInfoId);

            if(pageVO.getPurchaseInfoId() != null){
                queryWrapper.eq(PurchaseInfo::getPurchaseInfoId, pageVO.getPurchaseInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getPurchaseNo())) {
                queryWrapper.eq(PurchaseInfo::getPurchaseNo, pageVO.getPurchaseNo());
            }


            if(pageVO.getUserInfoUserInfoId1() != null){
                queryWrapper.eq(PurchaseInfo::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
            }


        if(StringUtils.isNotEmpty(pageVO.getSupplier())) {
                queryWrapper.eq(PurchaseInfo::getSupplier, pageVO.getSupplier());
            }


            if(pageVO.getTotalAmount() != null){
                queryWrapper.eq(PurchaseInfo::getTotalAmount, pageVO.getTotalAmount());
            }


            if(pageVO.getPurchaseDate() != null){
                queryWrapper.eq(PurchaseInfo::getPurchaseDate, pageVO.getPurchaseDate());
            }


            if(pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
                queryWrapper.eq(PurchaseInfo::getPurchaseStatusEnumPurchaseStatusEnumId1, pageVO.getPurchaseStatusEnumPurchaseStatusEnumId1());
            }

            return ResultUtils.success(purchaseInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  PurchaseInfoVO vo){
            LambdaQueryWrapper<PurchaseInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPurchaseNo())) {
                    queryWrapper.eq(PurchaseInfo::getPurchaseNo, vo.getPurchaseNo());
                }
                if(vo.getUserInfoUserInfoId1() != null){
                    queryWrapper.eq(PurchaseInfo::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
                }
            if(StringUtils.isNotEmpty(vo.getSupplier())) {
                    queryWrapper.eq(PurchaseInfo::getSupplier, vo.getSupplier());
                }
                if(vo.getTotalAmount() != null){
                    queryWrapper.eq(PurchaseInfo::getTotalAmount, vo.getTotalAmount());
                }
                if(vo.getPurchaseDate() != null){
                    queryWrapper.eq(PurchaseInfo::getPurchaseDate, vo.getPurchaseDate());
                }
                if(vo.getPurchaseStatusEnumPurchaseStatusEnumId1() != null){
                    queryWrapper.eq(PurchaseInfo::getPurchaseStatusEnumPurchaseStatusEnumId1, vo.getPurchaseStatusEnumPurchaseStatusEnumId1());
                }
                return ResultUtils.success(purchaseInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  PurchaseInfoVO vo) {
                PurchaseInfo entity =  PurchaseInfo.builder()
                    .purchaseNo(vo.getPurchaseNo())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .supplier(vo.getSupplier())
                    .totalAmount(vo.getTotalAmount())
                    .purchaseDate(vo.getPurchaseDate())
                    .purchaseStatusEnumPurchaseStatusEnumId1(vo.getPurchaseStatusEnumPurchaseStatusEnumId1())
                .build();








                boolean flag =this.purchaseInfoService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("purchase_info").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getPurchaseInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( PurchaseInfoVO vo) {
                PurchaseInfo entity =  PurchaseInfo.builder()
                    .purchaseInfoId(vo.getPurchaseInfoId())
                    .purchaseNo(vo.getPurchaseNo())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .supplier(vo.getSupplier())
                    .totalAmount(vo.getTotalAmount())
                    .purchaseDate(vo.getPurchaseDate())
                    .purchaseStatusEnumPurchaseStatusEnumId1(vo.getPurchaseStatusEnumPurchaseStatusEnumId1())
                .build();



                        boolean flag = this.purchaseInfoService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getPurchaseInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( PurchaseInfoVO vo ) {
                PurchaseInfo entity = this.purchaseInfoService.getById(vo.getPurchaseInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( PurchaseInfoVO vo) {
                boolean flag = this.purchaseInfoService.removeById(vo.getPurchaseInfoId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

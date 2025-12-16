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
import com.kuafu.web.entity.TransferInfo;
import com.kuafu.web.service.ITransferInfoService;
import com.kuafu.web.vo.TransferInfoPageVO;
import com.kuafu.web.vo.TransferInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 调拨信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("TransferInfoControllerService")
@RequiredArgsConstructor
public class TransferInfoControllerService  {

    private final ITransferInfoService transferInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( TransferInfoPageVO pageVO){
        IPage<TransferInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<TransferInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<TransferInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(TransferInfo::getTransferInfoId);

            if(pageVO.getTransferInfoId() != null){
                queryWrapper.eq(TransferInfo::getTransferInfoId, pageVO.getTransferInfoId());
            }


            if(pageVO.getFromWarehouseInfoWarehouseInfoId1() != null){
                queryWrapper.eq(TransferInfo::getFromWarehouseInfoWarehouseInfoId1, pageVO.getFromWarehouseInfoWarehouseInfoId1());
            }


            if(pageVO.getToWarehouseInfoWarehouseInfoId2() != null){
                queryWrapper.eq(TransferInfo::getToWarehouseInfoWarehouseInfoId2, pageVO.getToWarehouseInfoWarehouseInfoId2());
            }


            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(TransferInfo::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }


            if(pageVO.getQuantity() != null){
                queryWrapper.eq(TransferInfo::getQuantity, pageVO.getQuantity());
            }


            if(pageVO.getTransferStatusEnumTransferStatusEnumId1() != null){
                queryWrapper.eq(TransferInfo::getTransferStatusEnumTransferStatusEnumId1, pageVO.getTransferStatusEnumTransferStatusEnumId1());
            }


            if(pageVO.getApplyUserInfoUserInfoId1() != null){
                queryWrapper.eq(TransferInfo::getApplyUserInfoUserInfoId1, pageVO.getApplyUserInfoUserInfoId1());
            }


            if(pageVO.getConfirmUserInfoUserInfoId2() != null){
                queryWrapper.eq(TransferInfo::getConfirmUserInfoUserInfoId2, pageVO.getConfirmUserInfoUserInfoId2());
            }


            if(pageVO.getApplyDate() != null){
                queryWrapper.eq(TransferInfo::getApplyDate, pageVO.getApplyDate());
            }


            if(pageVO.getConfirmDate() != null){
                queryWrapper.eq(TransferInfo::getConfirmDate, pageVO.getConfirmDate());
            }

            return ResultUtils.success(transferInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  TransferInfoVO vo){
            LambdaQueryWrapper<TransferInfo> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getFromWarehouseInfoWarehouseInfoId1() != null){
                    queryWrapper.eq(TransferInfo::getFromWarehouseInfoWarehouseInfoId1, vo.getFromWarehouseInfoWarehouseInfoId1());
                }
                if(vo.getToWarehouseInfoWarehouseInfoId2() != null){
                    queryWrapper.eq(TransferInfo::getToWarehouseInfoWarehouseInfoId2, vo.getToWarehouseInfoWarehouseInfoId2());
                }
                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(TransferInfo::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
                if(vo.getQuantity() != null){
                    queryWrapper.eq(TransferInfo::getQuantity, vo.getQuantity());
                }
                if(vo.getTransferStatusEnumTransferStatusEnumId1() != null){
                    queryWrapper.eq(TransferInfo::getTransferStatusEnumTransferStatusEnumId1, vo.getTransferStatusEnumTransferStatusEnumId1());
                }
                if(vo.getApplyUserInfoUserInfoId1() != null){
                    queryWrapper.eq(TransferInfo::getApplyUserInfoUserInfoId1, vo.getApplyUserInfoUserInfoId1());
                }
                if(vo.getConfirmUserInfoUserInfoId2() != null){
                    queryWrapper.eq(TransferInfo::getConfirmUserInfoUserInfoId2, vo.getConfirmUserInfoUserInfoId2());
                }
                if(vo.getApplyDate() != null){
                    queryWrapper.eq(TransferInfo::getApplyDate, vo.getApplyDate());
                }
                if(vo.getConfirmDate() != null){
                    queryWrapper.eq(TransferInfo::getConfirmDate, vo.getConfirmDate());
                }
                return ResultUtils.success(transferInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  TransferInfoVO vo) {
                TransferInfo entity =  TransferInfo.builder()
                    .fromWarehouseInfoWarehouseInfoId1(vo.getFromWarehouseInfoWarehouseInfoId1())
                    .toWarehouseInfoWarehouseInfoId2(vo.getToWarehouseInfoWarehouseInfoId2())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .quantity(vo.getQuantity())
                    .transferStatusEnumTransferStatusEnumId1(vo.getTransferStatusEnumTransferStatusEnumId1())
                    .applyUserInfoUserInfoId1(vo.getApplyUserInfoUserInfoId1())
                    .confirmUserInfoUserInfoId2(vo.getConfirmUserInfoUserInfoId2())
                    .applyDate(vo.getApplyDate())
                    .confirmDate(vo.getConfirmDate())
                .build();








                boolean flag =this.transferInfoService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("transfer_info").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getTransferInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( TransferInfoVO vo) {
                TransferInfo entity =  TransferInfo.builder()
                    .transferInfoId(vo.getTransferInfoId())
                    .fromWarehouseInfoWarehouseInfoId1(vo.getFromWarehouseInfoWarehouseInfoId1())
                    .toWarehouseInfoWarehouseInfoId2(vo.getToWarehouseInfoWarehouseInfoId2())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .quantity(vo.getQuantity())
                    .transferStatusEnumTransferStatusEnumId1(vo.getTransferStatusEnumTransferStatusEnumId1())
                    .applyUserInfoUserInfoId1(vo.getApplyUserInfoUserInfoId1())
                    .confirmUserInfoUserInfoId2(vo.getConfirmUserInfoUserInfoId2())
                    .applyDate(vo.getApplyDate())
                    .confirmDate(vo.getConfirmDate())
                .build();



                        boolean flag = this.transferInfoService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getTransferInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( TransferInfoVO vo ) {
                TransferInfo entity = this.transferInfoService.getById(vo.getTransferInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( TransferInfoVO vo) {
                boolean flag = this.transferInfoService.removeById(vo.getTransferInfoId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

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
import com.kuafu.web.entity.InventoryRecord;
import com.kuafu.web.service.IInventoryRecordService;
import com.kuafu.web.vo.InventoryRecordPageVO;
import com.kuafu.web.vo.InventoryRecordVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 库存记录 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("InventoryRecordControllerService")
@RequiredArgsConstructor
public class InventoryRecordControllerService  {

    private final IInventoryRecordService inventoryRecordService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( InventoryRecordPageVO pageVO){
        IPage<InventoryRecord> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<InventoryRecord> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<InventoryRecord> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(InventoryRecord::getInventoryRecordId);

            if(pageVO.getInventoryRecordId() != null){
                queryWrapper.eq(InventoryRecord::getInventoryRecordId, pageVO.getInventoryRecordId());
            }


            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(InventoryRecord::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }


            if(pageVO.getWarehouseInfoWarehouseInfoId1() != null){
                queryWrapper.eq(InventoryRecord::getWarehouseInfoWarehouseInfoId1, pageVO.getWarehouseInfoWarehouseInfoId1());
            }


            if(pageVO.getQuantity() != null){
                queryWrapper.eq(InventoryRecord::getQuantity, pageVO.getQuantity());
            }


        if(StringUtils.isNotEmpty(pageVO.getBatchNumber())) {
                queryWrapper.eq(InventoryRecord::getBatchNumber, pageVO.getBatchNumber());
            }


            if(pageVO.getExpireDate() != null){
                queryWrapper.eq(InventoryRecord::getExpireDate, pageVO.getExpireDate());
            }


        if(StringUtils.isNotEmpty(pageVO.getSerialNumber())) {
                queryWrapper.eq(InventoryRecord::getSerialNumber, pageVO.getSerialNumber());
            }

            return ResultUtils.success(inventoryRecordService.page(page, queryWrapper));
        }


        public BaseResponse list(  InventoryRecordVO vo){
            LambdaQueryWrapper<InventoryRecord> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(InventoryRecord::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
                if(vo.getWarehouseInfoWarehouseInfoId1() != null){
                    queryWrapper.eq(InventoryRecord::getWarehouseInfoWarehouseInfoId1, vo.getWarehouseInfoWarehouseInfoId1());
                }
                if(vo.getQuantity() != null){
                    queryWrapper.eq(InventoryRecord::getQuantity, vo.getQuantity());
                }
            if(StringUtils.isNotEmpty(vo.getBatchNumber())) {
                    queryWrapper.eq(InventoryRecord::getBatchNumber, vo.getBatchNumber());
                }
                if(vo.getExpireDate() != null){
                    queryWrapper.eq(InventoryRecord::getExpireDate, vo.getExpireDate());
                }
            if(StringUtils.isNotEmpty(vo.getSerialNumber())) {
                    queryWrapper.eq(InventoryRecord::getSerialNumber, vo.getSerialNumber());
                }
                return ResultUtils.success(inventoryRecordService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  InventoryRecordVO vo) {
                InventoryRecord entity =  InventoryRecord.builder()
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .warehouseInfoWarehouseInfoId1(vo.getWarehouseInfoWarehouseInfoId1())
                    .quantity(vo.getQuantity())
                    .batchNumber(vo.getBatchNumber())
                    .expireDate(vo.getExpireDate())
                    .serialNumber(vo.getSerialNumber())
                .build();








                boolean flag =this.inventoryRecordService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("inventory_record").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getInventoryRecordId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( InventoryRecordVO vo) {
                InventoryRecord entity =  InventoryRecord.builder()
                    .inventoryRecordId(vo.getInventoryRecordId())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .warehouseInfoWarehouseInfoId1(vo.getWarehouseInfoWarehouseInfoId1())
                    .quantity(vo.getQuantity())
                    .batchNumber(vo.getBatchNumber())
                    .expireDate(vo.getExpireDate())
                    .serialNumber(vo.getSerialNumber())
                .build();



                        boolean flag = this.inventoryRecordService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getInventoryRecordId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( InventoryRecordVO vo ) {
                InventoryRecord entity = this.inventoryRecordService.getById(vo.getInventoryRecordId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( InventoryRecordVO vo) {
                boolean flag = this.inventoryRecordService.removeById(vo.getInventoryRecordId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

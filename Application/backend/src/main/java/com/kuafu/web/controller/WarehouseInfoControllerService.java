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
import com.kuafu.web.entity.WarehouseInfo;
import com.kuafu.web.service.IWarehouseInfoService;
import com.kuafu.web.vo.WarehouseInfoPageVO;
import com.kuafu.web.vo.WarehouseInfoVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 仓库信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("WarehouseInfoControllerService")
@RequiredArgsConstructor
public class WarehouseInfoControllerService  {

    private final IWarehouseInfoService warehouseInfoService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( WarehouseInfoPageVO pageVO){
        IPage<WarehouseInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<WarehouseInfo> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<WarehouseInfo> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(WarehouseInfo::getWarehouseInfoId);

            if(pageVO.getWarehouseInfoId() != null){
                queryWrapper.eq(WarehouseInfo::getWarehouseInfoId, pageVO.getWarehouseInfoId());
            }


        if(StringUtils.isNotEmpty(pageVO.getWarehouseName())) {
//                        queryWrapper.like(WarehouseInfo::getWarehouseName, pageVO.getWarehouseName());
                        String s_string = pageVO.getWarehouseName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(WarehouseInfo::getWarehouseName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }


        if(StringUtils.isNotEmpty(pageVO.getLocation())) {
                queryWrapper.eq(WarehouseInfo::getLocation, pageVO.getLocation());
            }

            return ResultUtils.success(warehouseInfoService.page(page, queryWrapper));
        }


        public BaseResponse list(  WarehouseInfoVO vo){
            LambdaQueryWrapper<WarehouseInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getWarehouseName())) {
                            queryWrapper.like(WarehouseInfo::getWarehouseName, vo.getWarehouseName());
                }
            if(StringUtils.isNotEmpty(vo.getLocation())) {
                    queryWrapper.eq(WarehouseInfo::getLocation, vo.getLocation());
                }
                return ResultUtils.success(warehouseInfoService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  WarehouseInfoVO vo) {
                WarehouseInfo entity =  WarehouseInfo.builder()
                    .warehouseName(vo.getWarehouseName())
                    .location(vo.getLocation())
                .build();








                boolean flag =this.warehouseInfoService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("warehouse_info").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getWarehouseInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( WarehouseInfoVO vo) {
                WarehouseInfo entity =  WarehouseInfo.builder()
                    .warehouseInfoId(vo.getWarehouseInfoId())
                    .warehouseName(vo.getWarehouseName())
                    .location(vo.getLocation())
                .build();



                        boolean flag = this.warehouseInfoService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getWarehouseInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( WarehouseInfoVO vo ) {
                WarehouseInfo entity = this.warehouseInfoService.getById(vo.getWarehouseInfoId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( WarehouseInfoVO vo) {
                boolean flag = this.warehouseInfoService.removeById(vo.getWarehouseInfoId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

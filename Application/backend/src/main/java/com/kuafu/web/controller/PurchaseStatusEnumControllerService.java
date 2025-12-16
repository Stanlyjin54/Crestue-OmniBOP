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
import com.kuafu.web.entity.PurchaseStatusEnum;
import com.kuafu.web.service.IPurchaseStatusEnumService;
import com.kuafu.web.vo.PurchaseStatusEnumPageVO;
import com.kuafu.web.vo.PurchaseStatusEnumVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 采购状态枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("PurchaseStatusEnumControllerService")
@RequiredArgsConstructor
public class PurchaseStatusEnumControllerService  {

    private final IPurchaseStatusEnumService purchaseStatusEnumService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( PurchaseStatusEnumPageVO pageVO){
        IPage<PurchaseStatusEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<PurchaseStatusEnum> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(PurchaseStatusEnum::getPurchaseStatusEnumId);

            if(pageVO.getPurchaseStatusEnumId() != null){
                queryWrapper.eq(PurchaseStatusEnum::getPurchaseStatusEnumId, pageVO.getPurchaseStatusEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {
//                        queryWrapper.like(PurchaseStatusEnum::getStatusName, pageVO.getStatusName());
                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(PurchaseStatusEnum::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

            return ResultUtils.success(purchaseStatusEnumService.page(page, queryWrapper));
        }


        public BaseResponse list(  PurchaseStatusEnumVO vo){
            LambdaQueryWrapper<PurchaseStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(PurchaseStatusEnum::getStatusName, vo.getStatusName());
                }
                return ResultUtils.success(purchaseStatusEnumService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  PurchaseStatusEnumVO vo) {
                PurchaseStatusEnum entity =  PurchaseStatusEnum.builder()
                    .statusName(vo.getStatusName())
                .build();








                boolean flag =this.purchaseStatusEnumService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("purchase_status_enum").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getPurchaseStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( PurchaseStatusEnumVO vo) {
                PurchaseStatusEnum entity =  PurchaseStatusEnum.builder()
                    .purchaseStatusEnumId(vo.getPurchaseStatusEnumId())
                    .statusName(vo.getStatusName())
                .build();



                        boolean flag = this.purchaseStatusEnumService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getPurchaseStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( PurchaseStatusEnumVO vo ) {
                PurchaseStatusEnum entity = this.purchaseStatusEnumService.getById(vo.getPurchaseStatusEnumId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( PurchaseStatusEnumVO vo) {
                boolean flag = this.purchaseStatusEnumService.removeById(vo.getPurchaseStatusEnumId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

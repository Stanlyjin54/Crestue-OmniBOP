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
import com.kuafu.web.entity.TransferStatusEnum;
import com.kuafu.web.service.ITransferStatusEnumService;
import com.kuafu.web.vo.TransferStatusEnumPageVO;
import com.kuafu.web.vo.TransferStatusEnumVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 调拨状态枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("TransferStatusEnumControllerService")
@RequiredArgsConstructor
public class TransferStatusEnumControllerService  {

    private final ITransferStatusEnumService transferStatusEnumService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( TransferStatusEnumPageVO pageVO){
        IPage<TransferStatusEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<TransferStatusEnum> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<TransferStatusEnum> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(TransferStatusEnum::getTransferStatusEnumId);

            if(pageVO.getTransferStatusEnumId() != null){
                queryWrapper.eq(TransferStatusEnum::getTransferStatusEnumId, pageVO.getTransferStatusEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getStatusName())) {
//                        queryWrapper.like(TransferStatusEnum::getStatusName, pageVO.getStatusName());
                        String s_string = pageVO.getStatusName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(TransferStatusEnum::getStatusName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

            return ResultUtils.success(transferStatusEnumService.page(page, queryWrapper));
        }


        public BaseResponse list(  TransferStatusEnumVO vo){
            LambdaQueryWrapper<TransferStatusEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getStatusName())) {
                            queryWrapper.like(TransferStatusEnum::getStatusName, vo.getStatusName());
                }
                return ResultUtils.success(transferStatusEnumService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  TransferStatusEnumVO vo) {
                TransferStatusEnum entity =  TransferStatusEnum.builder()
                    .statusName(vo.getStatusName())
                .build();








                boolean flag =this.transferStatusEnumService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("transfer_status_enum").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getTransferStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( TransferStatusEnumVO vo) {
                TransferStatusEnum entity =  TransferStatusEnum.builder()
                    .transferStatusEnumId(vo.getTransferStatusEnumId())
                    .statusName(vo.getStatusName())
                .build();



                        boolean flag = this.transferStatusEnumService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getTransferStatusEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( TransferStatusEnumVO vo ) {
                TransferStatusEnum entity = this.transferStatusEnumService.getById(vo.getTransferStatusEnumId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( TransferStatusEnumVO vo) {
                boolean flag = this.transferStatusEnumService.removeById(vo.getTransferStatusEnumId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

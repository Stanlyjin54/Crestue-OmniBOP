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
import com.kuafu.web.entity.RfmLevelEnum;
import com.kuafu.web.service.IRfmLevelEnumService;
import com.kuafu.web.vo.RfmLevelEnumPageVO;
import com.kuafu.web.vo.RfmLevelEnumVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> RFM分层枚举 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("RfmLevelEnumControllerService")
@RequiredArgsConstructor
public class RfmLevelEnumControllerService  {

    private final IRfmLevelEnumService rfmLevelEnumService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( RfmLevelEnumPageVO pageVO){
        IPage<RfmLevelEnum> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<RfmLevelEnum> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<RfmLevelEnum> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(RfmLevelEnum::getRfmLevelEnumId);

            if(pageVO.getRfmLevelEnumId() != null){
                queryWrapper.eq(RfmLevelEnum::getRfmLevelEnumId, pageVO.getRfmLevelEnumId());
            }


        if(StringUtils.isNotEmpty(pageVO.getLevelName())) {
//                        queryWrapper.like(RfmLevelEnum::getLevelName, pageVO.getLevelName());
                        String s_string = pageVO.getLevelName();
                String[] strings = s_string.split(" ");
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < strings.length; i++) {
                        String s = strings[i];
                        if (StringUtils.isNotEmpty(s)) {
                            final String trim = s.trim();
                            wrapper.like(RfmLevelEnum::getLevelName, trim);
                            if (i != strings.length - 1) {
                                wrapper.or();
                            }
                        }
                    }
                });
            }

            return ResultUtils.success(rfmLevelEnumService.page(page, queryWrapper));
        }


        public BaseResponse list(  RfmLevelEnumVO vo){
            LambdaQueryWrapper<RfmLevelEnum> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getLevelName())) {
                            queryWrapper.like(RfmLevelEnum::getLevelName, vo.getLevelName());
                }
                return ResultUtils.success(rfmLevelEnumService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  RfmLevelEnumVO vo) {
                RfmLevelEnum entity =  RfmLevelEnum.builder()
                    .levelName(vo.getLevelName())
                .build();








                boolean flag =this.rfmLevelEnumService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("rfm_level_enum").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getRfmLevelEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( RfmLevelEnumVO vo) {
                RfmLevelEnum entity =  RfmLevelEnum.builder()
                    .rfmLevelEnumId(vo.getRfmLevelEnumId())
                    .levelName(vo.getLevelName())
                .build();



                        boolean flag = this.rfmLevelEnumService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getRfmLevelEnumId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( RfmLevelEnumVO vo ) {
                RfmLevelEnum entity = this.rfmLevelEnumService.getById(vo.getRfmLevelEnumId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( RfmLevelEnumVO vo) {
                boolean flag = this.rfmLevelEnumService.removeById(vo.getRfmLevelEnumId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

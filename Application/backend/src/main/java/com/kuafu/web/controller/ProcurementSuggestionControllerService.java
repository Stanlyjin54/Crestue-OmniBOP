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
import com.kuafu.web.entity.ProcurementSuggestion;
import com.kuafu.web.service.IProcurementSuggestionService;
import com.kuafu.web.vo.ProcurementSuggestionPageVO;
import com.kuafu.web.vo.ProcurementSuggestionVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 采购建议 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("ProcurementSuggestionControllerService")
@RequiredArgsConstructor
public class ProcurementSuggestionControllerService  {

    private final IProcurementSuggestionService procurementSuggestionService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( ProcurementSuggestionPageVO pageVO){
        IPage<ProcurementSuggestion> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<ProcurementSuggestion> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(ProcurementSuggestion::getProcurementSuggestionId);

            if(pageVO.getProcurementSuggestionId() != null){
                queryWrapper.eq(ProcurementSuggestion::getProcurementSuggestionId, pageVO.getProcurementSuggestionId());
            }


            if(pageVO.getProductInfoProductInfoId1() != null){
                queryWrapper.eq(ProcurementSuggestion::getProductInfoProductInfoId1, pageVO.getProductInfoProductInfoId1());
            }


            if(pageVO.getSuggestQuantity() != null){
                queryWrapper.eq(ProcurementSuggestion::getSuggestQuantity, pageVO.getSuggestQuantity());
            }


        if(StringUtils.isNotEmpty(pageVO.getReason())) {
                queryWrapper.eq(ProcurementSuggestion::getReason, pageVO.getReason());
            }


            if(pageVO.getSuggestDate() != null){
                queryWrapper.eq(ProcurementSuggestion::getSuggestDate, pageVO.getSuggestDate());
            }

            return ResultUtils.success(procurementSuggestionService.page(page, queryWrapper));
        }


        public BaseResponse list(  ProcurementSuggestionVO vo){
            LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getProductInfoProductInfoId1() != null){
                    queryWrapper.eq(ProcurementSuggestion::getProductInfoProductInfoId1, vo.getProductInfoProductInfoId1());
                }
                if(vo.getSuggestQuantity() != null){
                    queryWrapper.eq(ProcurementSuggestion::getSuggestQuantity, vo.getSuggestQuantity());
                }
            if(StringUtils.isNotEmpty(vo.getReason())) {
                    queryWrapper.eq(ProcurementSuggestion::getReason, vo.getReason());
                }
                if(vo.getSuggestDate() != null){
                    queryWrapper.eq(ProcurementSuggestion::getSuggestDate, vo.getSuggestDate());
                }
                return ResultUtils.success(procurementSuggestionService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  ProcurementSuggestionVO vo) {
                ProcurementSuggestion entity =  ProcurementSuggestion.builder()
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .suggestQuantity(vo.getSuggestQuantity())
                    .reason(vo.getReason())
                    .suggestDate(vo.getSuggestDate())
                .build();








                boolean flag =this.procurementSuggestionService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("procurement_suggestion").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getProcurementSuggestionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( ProcurementSuggestionVO vo) {
                ProcurementSuggestion entity =  ProcurementSuggestion.builder()
                    .procurementSuggestionId(vo.getProcurementSuggestionId())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .suggestQuantity(vo.getSuggestQuantity())
                    .reason(vo.getReason())
                    .suggestDate(vo.getSuggestDate())
                .build();



                        boolean flag = this.procurementSuggestionService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getProcurementSuggestionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( ProcurementSuggestionVO vo ) {
                ProcurementSuggestion entity = this.procurementSuggestionService.getById(vo.getProcurementSuggestionId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( ProcurementSuggestionVO vo) {
                boolean flag = this.procurementSuggestionService.removeById(vo.getProcurementSuggestionId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

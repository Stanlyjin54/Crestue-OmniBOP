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
import com.kuafu.web.entity.ProfitBoard;
import com.kuafu.web.service.IProfitBoardService;
import com.kuafu.web.vo.ProfitBoardPageVO;
import com.kuafu.web.vo.ProfitBoardVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 利润看板 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("ProfitBoardControllerService")
@RequiredArgsConstructor
public class ProfitBoardControllerService  {

    private final IProfitBoardService profitBoardService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( ProfitBoardPageVO pageVO){
        IPage<ProfitBoard> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<ProfitBoard> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<ProfitBoard> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(ProfitBoard::getProfitBoardId);

            if(pageVO.getProfitBoardId() != null){
                queryWrapper.eq(ProfitBoard::getProfitBoardId, pageVO.getProfitBoardId());
            }


        if(StringUtils.isNotEmpty(pageVO.getTargetType())) {
                queryWrapper.eq(ProfitBoard::getTargetType, pageVO.getTargetType());
            }


            if(pageVO.getTargetId() != null){
                queryWrapper.eq(ProfitBoard::getTargetId, pageVO.getTargetId());
            }


            if(pageVO.getProfitAmount() != null){
                queryWrapper.eq(ProfitBoard::getProfitAmount, pageVO.getProfitAmount());
            }


            if(pageVO.getCommission() != null){
                queryWrapper.eq(ProfitBoard::getCommission, pageVO.getCommission());
            }


            if(pageVO.getShippingFee() != null){
                queryWrapper.eq(ProfitBoard::getShippingFee, pageVO.getShippingFee());
            }


            if(pageVO.getStatDate() != null){
                queryWrapper.eq(ProfitBoard::getStatDate, pageVO.getStatDate());
            }

            return ResultUtils.success(profitBoardService.page(page, queryWrapper));
        }


        public BaseResponse list(  ProfitBoardVO vo){
            LambdaQueryWrapper<ProfitBoard> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTargetType())) {
                    queryWrapper.eq(ProfitBoard::getTargetType, vo.getTargetType());
                }
                if(vo.getTargetId() != null){
                    queryWrapper.eq(ProfitBoard::getTargetId, vo.getTargetId());
                }
                if(vo.getProfitAmount() != null){
                    queryWrapper.eq(ProfitBoard::getProfitAmount, vo.getProfitAmount());
                }
                if(vo.getCommission() != null){
                    queryWrapper.eq(ProfitBoard::getCommission, vo.getCommission());
                }
                if(vo.getShippingFee() != null){
                    queryWrapper.eq(ProfitBoard::getShippingFee, vo.getShippingFee());
                }
                if(vo.getStatDate() != null){
                    queryWrapper.eq(ProfitBoard::getStatDate, vo.getStatDate());
                }
                return ResultUtils.success(profitBoardService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  ProfitBoardVO vo) {
                ProfitBoard entity =  ProfitBoard.builder()
                    .targetType(vo.getTargetType())
                    .targetId(vo.getTargetId())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .statDate(vo.getStatDate())
                .build();








                boolean flag =this.profitBoardService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("profit_board").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getProfitBoardId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( ProfitBoardVO vo) {
                ProfitBoard entity =  ProfitBoard.builder()
                    .profitBoardId(vo.getProfitBoardId())
                    .targetType(vo.getTargetType())
                    .targetId(vo.getTargetId())
                    .profitAmount(vo.getProfitAmount())
                    .commission(vo.getCommission())
                    .shippingFee(vo.getShippingFee())
                    .statDate(vo.getStatDate())
                .build();



                        boolean flag = this.profitBoardService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getProfitBoardId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( ProfitBoardVO vo ) {
                ProfitBoard entity = this.profitBoardService.getById(vo.getProfitBoardId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( ProfitBoardVO vo) {
                boolean flag = this.profitBoardService.removeById(vo.getProfitBoardId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

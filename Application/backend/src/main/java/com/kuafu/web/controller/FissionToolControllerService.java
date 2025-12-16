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
import com.kuafu.web.entity.FissionTool;
import com.kuafu.web.service.IFissionToolService;
import com.kuafu.web.vo.FissionToolPageVO;
import com.kuafu.web.vo.FissionToolVO;
import com.kuafu.common.util.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * <p> 裂变工具 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Service("FissionToolControllerService")
@RequiredArgsConstructor
public class FissionToolControllerService  {

    private final IFissionToolService fissionToolService;

    private final IStaticResourceService staticResourceService;

    private final TransactionTemplate transactionTemplate;

    private final MyEventService myEventService;



    public BaseResponse page( FissionToolPageVO pageVO){
        IPage<FissionTool> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

         final QueryWrapper<FissionTool> objectQueryWrapper = new QueryWrapper<>();

        QueryUtils.getQueryWrapper(objectQueryWrapper,pageVO);
        LambdaQueryWrapper<FissionTool> queryWrapper = objectQueryWrapper.lambda();
        queryWrapper.orderByDesc(FissionTool::getFissionToolId);

            if(pageVO.getFissionToolId() != null){
                queryWrapper.eq(FissionTool::getFissionToolId, pageVO.getFissionToolId());
            }


        if(StringUtils.isNotEmpty(pageVO.getToolType())) {
                queryWrapper.eq(FissionTool::getToolType, pageVO.getToolType());
            }


        if(StringUtils.isNotEmpty(pageVO.getTitle())) {
                queryWrapper.eq(FissionTool::getTitle, pageVO.getTitle());
            }


        if(StringUtils.isNotEmpty(pageVO.getConfig())) {
                queryWrapper.eq(FissionTool::getConfig, pageVO.getConfig());
            }


            if(pageVO.getStartDate() != null){
                queryWrapper.eq(FissionTool::getStartDate, pageVO.getStartDate());
            }


            if(pageVO.getEndDate() != null){
                queryWrapper.eq(FissionTool::getEndDate, pageVO.getEndDate());
            }

            return ResultUtils.success(fissionToolService.page(page, queryWrapper));
        }


        public BaseResponse list(  FissionToolVO vo){
            LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getToolType())) {
                    queryWrapper.eq(FissionTool::getToolType, vo.getToolType());
                }
            if(StringUtils.isNotEmpty(vo.getTitle())) {
                    queryWrapper.eq(FissionTool::getTitle, vo.getTitle());
                }
            if(StringUtils.isNotEmpty(vo.getConfig())) {
                    queryWrapper.eq(FissionTool::getConfig, vo.getConfig());
                }
                if(vo.getStartDate() != null){
                    queryWrapper.eq(FissionTool::getStartDate, vo.getStartDate());
                }
                if(vo.getEndDate() != null){
                    queryWrapper.eq(FissionTool::getEndDate, vo.getEndDate());
                }
                return ResultUtils.success(fissionToolService.list(queryWrapper));
            }


            @SensitiveFilter
            public BaseResponse add(  FissionToolVO vo) {
                FissionTool entity =  FissionTool.builder()
                    .toolType(vo.getToolType())
                    .title(vo.getTitle())
                    .config(vo.getConfig())
                    .startDate(vo.getStartDate())
                    .endDate(vo.getEndDate())
                .build();








                boolean flag =this.fissionToolService.save(entity);






                if(flag
                        ){
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("fission_tool").data(entity).build());
                }

                return
flag

                ? ResultUtils.success(entity.getFissionToolId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }


            @SensitiveFilter
            public BaseResponse update( FissionToolVO vo) {
                FissionTool entity =  FissionTool.builder()
                    .fissionToolId(vo.getFissionToolId())
                    .toolType(vo.getToolType())
                    .title(vo.getTitle())
                    .config(vo.getConfig())
                    .startDate(vo.getStartDate())
                    .endDate(vo.getEndDate())
                .build();



                        boolean flag = this.fissionToolService.saveOrUpdate(entity);








                return  flag
                         ? ResultUtils.success(entity.getFissionToolId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse get( FissionToolVO vo ) {
                FissionTool entity = this.fissionToolService.getById(vo.getFissionToolId());
                return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }



            public BaseResponse delete( FissionToolVO vo) {
                boolean flag = this.fissionToolService.removeById(vo.getFissionToolId());





                return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
            }

        }

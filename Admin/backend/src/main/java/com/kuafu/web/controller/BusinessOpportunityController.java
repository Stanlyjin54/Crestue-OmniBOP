package com.kuafuweb.web.controller;

import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;
import com.kuafuweb.common.util.excel.ExcelProvider;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.web.bind.annotation.RequestPart;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.SelectVO;
import com.kuafuweb.common.util.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;
import com.kuafuweb.common.sensitive.SensitiveFilter;
import com.kuafuweb.common.domin.StaticResource;
import com.kuafuweb.common.resource.service.IStaticResourceService;

import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import com.kuafuweb.common.domin.BaseResponse;
import com.kuafuweb.common.domin.ErrorCode;
import com.kuafuweb.common.domin.ResultUtils;
import com.kuafuweb.common.util.StringUtils;
import com.kuafuweb.web.entity.BusinessOpportunity;
import com.kuafuweb.web.service.IBusinessOpportunityService;
import com.kuafuweb.web.vo.BusinessOpportunityPageVO;
import com.kuafuweb.web.vo.BusinessOpportunityVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 商机线索 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/businessOpportunity")
@Api(tags = {"商机线索"})
public class BusinessOpportunityController  {

    private final IBusinessOpportunityService businessOpportunityService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody BusinessOpportunityPageVO pageVO){
        IPage<BusinessOpportunity> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getCustomerInfoCustomerInfoId1() != null){
            queryWrapper.eq(BusinessOpportunity::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
        }
            if(StringUtils.isNotEmpty(pageVO.getOpportunityName())) {
            queryWrapper.like(BusinessOpportunity::getOpportunityName, pageVO.getOpportunityName());
        }
            if(StringUtils.isNotEmpty(pageVO.getStatus())) {
            queryWrapper.eq(BusinessOpportunity::getStatus, pageVO.getStatus());
        }
            if(StringUtils.isNotEmpty(pageVO.getFollowUpSuggestion())) {
            queryWrapper.eq(BusinessOpportunity::getFollowUpSuggestion, pageVO.getFollowUpSuggestion());
        }
        return ResultUtils.success(businessOpportunityService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody BusinessOpportunityVO vo){
        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getCustomerInfoCustomerInfoId1() != null){
            queryWrapper.eq(BusinessOpportunity::getCustomerInfoCustomerInfoId1, vo.getCustomerInfoCustomerInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getOpportunityName())) {
            queryWrapper.like(BusinessOpportunity::getOpportunityName, vo.getOpportunityName());
        }
            if(StringUtils.isNotEmpty(vo.getStatus())) {
            queryWrapper.eq(BusinessOpportunity::getStatus, vo.getStatus());
        }
            if(StringUtils.isNotEmpty(vo.getFollowUpSuggestion())) {
            queryWrapper.eq(BusinessOpportunity::getFollowUpSuggestion, vo.getFollowUpSuggestion());
        }
        return ResultUtils.success(businessOpportunityService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(BusinessOpportunity::getBusinessOpportunityId);

        List<BusinessOpportunity> list =businessOpportunityService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getBusinessOpportunityId(), l.getBusinessOpportunityId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody BusinessOpportunityVO vo) {
        BusinessOpportunity entity =  BusinessOpportunity.builder()
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .opportunityName(vo.getOpportunityName())
                    .status(vo.getStatus())
                    .followUpSuggestion(vo.getFollowUpSuggestion())
                .build();
        boolean flag =this.businessOpportunityService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("businessOpportunity").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody BusinessOpportunityVO vo) {
        BusinessOpportunity entity =  BusinessOpportunity.builder()
                    .businessOpportunityId(vo.getBusinessOpportunityId())
                    .customerInfoCustomerInfoId1(vo.getCustomerInfoCustomerInfoId1())
                    .opportunityName(vo.getOpportunityName())
                    .status(vo.getStatus())
                    .followUpSuggestion(vo.getFollowUpSuggestion())
                .build();
        boolean flag = this.businessOpportunityService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("businessOpportunity").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer businessOpportunityId) {
        BusinessOpportunity entity = this.businessOpportunityService.getById(businessOpportunityId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer businessOpportunityId) {
        boolean flag = this.businessOpportunityService.removeById(businessOpportunityId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("businessOpportunity").data( BusinessOpportunity.builder().businessOpportunityId(businessOpportunityId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody BusinessOpportunityPageVO pageVO) {

        LambdaQueryWrapper<BusinessOpportunity> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getCustomerInfoCustomerInfoId1() != null){
                            queryWrapper.eq(BusinessOpportunity::getCustomerInfoCustomerInfoId1, pageVO.getCustomerInfoCustomerInfoId1());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getOpportunityName())) {
                                    queryWrapper.like(BusinessOpportunity::getOpportunityName, pageVO.getOpportunityName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getStatus())) {
                            queryWrapper.eq(BusinessOpportunity::getStatus, pageVO.getStatus());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getFollowUpSuggestion())) {
                            queryWrapper.eq(BusinessOpportunity::getFollowUpSuggestion, pageVO.getFollowUpSuggestion());
                        }
            List<BusinessOpportunity> list = businessOpportunityService.list(queryWrapper);
            ExcelUtil<BusinessOpportunity> util = new ExcelUtil<>(BusinessOpportunity.class);
            util.exportExcel(response, list, "数据");
        }

    /**
     * 导入excel
     * @param file
     * @return
     */
    @PostMapping("/import")
    public BaseResponse importExcel(@RequestPart(name = "file") MultipartFile file)
    {
            excelProvider.importData(file, BusinessOpportunity.class, businessOpportunityService::saveBatch);
            return ResultUtils.success("导入成功");
    }

    /**
     * 下载 excel 模版
     * @param response
     * @throws IOException
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
                    try {
                        Workbook workbook = excelProvider.downloadExcelTemplate(BusinessOpportunity.class);
                        // 返回文件流
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setCharacterEncoding("utf-8");
                        workbook.write(response.getOutputStream());
                        workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
    }


            }






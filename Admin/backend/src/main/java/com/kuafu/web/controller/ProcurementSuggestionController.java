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
import com.kuafuweb.web.entity.ProcurementSuggestion;
import com.kuafuweb.web.service.IProcurementSuggestionService;
import com.kuafuweb.web.vo.ProcurementSuggestionPageVO;
import com.kuafuweb.web.vo.ProcurementSuggestionVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 采购建议 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/procurementSuggestion")
@Api(tags = {"采购建议"})
public class ProcurementSuggestionController  {

    private final IProcurementSuggestionService procurementSuggestionService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody ProcurementSuggestionPageVO pageVO){
        IPage<ProcurementSuggestion> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = new LambdaQueryWrapper<>();

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

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody ProcurementSuggestionVO vo){
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


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(ProcurementSuggestion::getProcurementSuggestionId);

        List<ProcurementSuggestion> list =procurementSuggestionService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getProcurementSuggestionId(), l.getProcurementSuggestionId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody ProcurementSuggestionVO vo) {
        ProcurementSuggestion entity =  ProcurementSuggestion.builder()
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .suggestQuantity(vo.getSuggestQuantity())
                    .reason(vo.getReason())
                    .suggestDate(vo.getSuggestDate())
                .build();
        boolean flag =this.procurementSuggestionService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("procurementSuggestion").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody ProcurementSuggestionVO vo) {
        ProcurementSuggestion entity =  ProcurementSuggestion.builder()
                    .procurementSuggestionId(vo.getProcurementSuggestionId())
                    .productInfoProductInfoId1(vo.getProductInfoProductInfoId1())
                    .suggestQuantity(vo.getSuggestQuantity())
                    .reason(vo.getReason())
                    .suggestDate(vo.getSuggestDate())
                .build();
        boolean flag = this.procurementSuggestionService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("procurementSuggestion").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer procurementSuggestionId) {
        ProcurementSuggestion entity = this.procurementSuggestionService.getById(procurementSuggestionId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer procurementSuggestionId) {
        boolean flag = this.procurementSuggestionService.removeById(procurementSuggestionId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("procurementSuggestion").data( ProcurementSuggestion.builder().procurementSuggestionId(procurementSuggestionId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ProcurementSuggestionPageVO pageVO) {

        LambdaQueryWrapper<ProcurementSuggestion> queryWrapper = new LambdaQueryWrapper<>();

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
            List<ProcurementSuggestion> list = procurementSuggestionService.list(queryWrapper);
            ExcelUtil<ProcurementSuggestion> util = new ExcelUtil<>(ProcurementSuggestion.class);
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
            excelProvider.importData(file, ProcurementSuggestion.class, procurementSuggestionService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(ProcurementSuggestion.class);
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






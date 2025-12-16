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
import com.kuafuweb.web.entity.FissionTool;
import com.kuafuweb.web.service.IFissionToolService;
import com.kuafuweb.web.vo.FissionToolPageVO;
import com.kuafuweb.web.vo.FissionToolVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 裂变工具 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/fissionTool")
@Api(tags = {"裂变工具"})
public class FissionToolController  {

    private final IFissionToolService fissionToolService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody FissionToolPageVO pageVO){
        IPage<FissionTool> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

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

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody FissionToolVO vo){
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


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(FissionTool::getFissionToolId);

        List<FissionTool> list =fissionToolService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getFissionToolId(), l.getFissionToolId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody FissionToolVO vo) {
        FissionTool entity =  FissionTool.builder()
                    .toolType(vo.getToolType())
                    .title(vo.getTitle())
                    .config(vo.getConfig())
                    .startDate(vo.getStartDate())
                    .endDate(vo.getEndDate())
                .build();
        boolean flag =this.fissionToolService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("fissionTool").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody FissionToolVO vo) {
        FissionTool entity =  FissionTool.builder()
                    .fissionToolId(vo.getFissionToolId())
                    .toolType(vo.getToolType())
                    .title(vo.getTitle())
                    .config(vo.getConfig())
                    .startDate(vo.getStartDate())
                    .endDate(vo.getEndDate())
                .build();
        boolean flag = this.fissionToolService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("fissionTool").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer fissionToolId) {
        FissionTool entity = this.fissionToolService.getById(fissionToolId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer fissionToolId) {
        boolean flag = this.fissionToolService.removeById(fissionToolId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("fissionTool").data( FissionTool.builder().fissionToolId(fissionToolId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody FissionToolPageVO pageVO) {

        LambdaQueryWrapper<FissionTool> queryWrapper = new LambdaQueryWrapper<>();

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
            List<FissionTool> list = fissionToolService.list(queryWrapper);
            ExcelUtil<FissionTool> util = new ExcelUtil<>(FissionTool.class);
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
            excelProvider.importData(file, FissionTool.class, fissionToolService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(FissionTool.class);
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






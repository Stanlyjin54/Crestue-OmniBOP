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
import com.kuafuweb.web.entity.AiAssistantLog;
import com.kuafuweb.web.service.IAiAssistantLogService;
import com.kuafuweb.web.vo.AiAssistantLogPageVO;
import com.kuafuweb.web.vo.AiAssistantLogVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> AI助手日志 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/aiAssistantLog")
@Api(tags = {"AI助手日志"})
public class AiAssistantLogController  {

    private final IAiAssistantLogService aiAssistantLogService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody AiAssistantLogPageVO pageVO){
        IPage<AiAssistantLog> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(AiAssistantLog::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(pageVO.getQueryText())) {
            queryWrapper.eq(AiAssistantLog::getQueryText, pageVO.getQueryText());
        }
            if(StringUtils.isNotEmpty(pageVO.getResponseText())) {
            queryWrapper.eq(AiAssistantLog::getResponseText, pageVO.getResponseText());
        }
            if(pageVO.getQueryTime() != null){
            queryWrapper.eq(AiAssistantLog::getQueryTime, pageVO.getQueryTime());
        }
        return ResultUtils.success(aiAssistantLogService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody AiAssistantLogVO vo){
        LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoUserInfoId1() != null){
            queryWrapper.eq(AiAssistantLog::getUserInfoUserInfoId1, vo.getUserInfoUserInfoId1());
        }
            if(StringUtils.isNotEmpty(vo.getQueryText())) {
            queryWrapper.eq(AiAssistantLog::getQueryText, vo.getQueryText());
        }
            if(StringUtils.isNotEmpty(vo.getResponseText())) {
            queryWrapper.eq(AiAssistantLog::getResponseText, vo.getResponseText());
        }
                if(vo.getQueryTime() != null){
            queryWrapper.eq(AiAssistantLog::getQueryTime, vo.getQueryTime());
        }
        return ResultUtils.success(aiAssistantLogService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(AiAssistantLog::getAiAssistantLogId);

        List<AiAssistantLog> list =aiAssistantLogService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getAiAssistantLogId(), l.getAiAssistantLogId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody AiAssistantLogVO vo) {
        AiAssistantLog entity =  AiAssistantLog.builder()
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .queryText(vo.getQueryText())
                    .responseText(vo.getResponseText())
                    .queryTime(vo.getQueryTime())
                .build();
        boolean flag =this.aiAssistantLogService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("aiAssistantLog").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody AiAssistantLogVO vo) {
        AiAssistantLog entity =  AiAssistantLog.builder()
                    .aiAssistantLogId(vo.getAiAssistantLogId())
                    .userInfoUserInfoId1(vo.getUserInfoUserInfoId1())
                    .queryText(vo.getQueryText())
                    .responseText(vo.getResponseText())
                    .queryTime(vo.getQueryTime())
                .build();
        boolean flag = this.aiAssistantLogService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("aiAssistantLog").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer aiAssistantLogId) {
        AiAssistantLog entity = this.aiAssistantLogService.getById(aiAssistantLogId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer aiAssistantLogId) {
        boolean flag = this.aiAssistantLogService.removeById(aiAssistantLogId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("aiAssistantLog").data( AiAssistantLog.builder().aiAssistantLogId(aiAssistantLogId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody AiAssistantLogPageVO pageVO) {

        LambdaQueryWrapper<AiAssistantLog> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getUserInfoUserInfoId1() != null){
                            queryWrapper.eq(AiAssistantLog::getUserInfoUserInfoId1, pageVO.getUserInfoUserInfoId1());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getQueryText())) {
                            queryWrapper.eq(AiAssistantLog::getQueryText, pageVO.getQueryText());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getResponseText())) {
                            queryWrapper.eq(AiAssistantLog::getResponseText, pageVO.getResponseText());
                        }
                        if(pageVO.getQueryTime() != null){
                            queryWrapper.eq(AiAssistantLog::getQueryTime, pageVO.getQueryTime());
                        }
            List<AiAssistantLog> list = aiAssistantLogService.list(queryWrapper);
            ExcelUtil<AiAssistantLog> util = new ExcelUtil<>(AiAssistantLog.class);
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
            excelProvider.importData(file, AiAssistantLog.class, aiAssistantLogService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(AiAssistantLog.class);
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






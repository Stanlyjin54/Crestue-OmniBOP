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
import com.kuafuweb.web.entity.DelayedTasks;
import com.kuafuweb.web.service.IDelayedTasksService;
import com.kuafuweb.web.vo.DelayedTasksPageVO;
import com.kuafuweb.web.vo.DelayedTasksVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 延迟任务 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/delayedTasks")
@Api(tags = {"延迟任务"})
public class DelayedTasksController  {

    private final IDelayedTasksService delayedTasksService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody DelayedTasksPageVO pageVO){
        IPage<DelayedTasks> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<DelayedTasks> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getTaskType())) {
            queryWrapper.eq(DelayedTasks::getTaskType, pageVO.getTaskType());
        }
            if(StringUtils.isNotEmpty(pageVO.getTaskData())) {
            queryWrapper.eq(DelayedTasks::getTaskData, pageVO.getTaskData());
        }
            if(StringUtils.isNotEmpty(pageVO.getStatus())) {
            queryWrapper.eq(DelayedTasks::getStatus, pageVO.getStatus());
        }
            if(pageVO.getExecuteTime() != null){
            queryWrapper.eq(DelayedTasks::getExecuteTime, pageVO.getExecuteTime());
        }
            if(pageVO.getCreateTime() != null){
            queryWrapper.eq(DelayedTasks::getCreateTime, pageVO.getCreateTime());
        }
            if(pageVO.getUpdateTime() != null){
            queryWrapper.eq(DelayedTasks::getUpdateTime, pageVO.getUpdateTime());
        }
        return ResultUtils.success(delayedTasksService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody DelayedTasksVO vo){
        LambdaQueryWrapper<DelayedTasks> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getTaskType())) {
            queryWrapper.eq(DelayedTasks::getTaskType, vo.getTaskType());
        }
            if(StringUtils.isNotEmpty(vo.getTaskData())) {
            queryWrapper.eq(DelayedTasks::getTaskData, vo.getTaskData());
        }
            if(StringUtils.isNotEmpty(vo.getStatus())) {
            queryWrapper.eq(DelayedTasks::getStatus, vo.getStatus());
        }
                if(vo.getExecuteTime() != null){
            queryWrapper.eq(DelayedTasks::getExecuteTime, vo.getExecuteTime());
        }
                if(vo.getCreateTime() != null){
            queryWrapper.eq(DelayedTasks::getCreateTime, vo.getCreateTime());
        }
                if(vo.getUpdateTime() != null){
            queryWrapper.eq(DelayedTasks::getUpdateTime, vo.getUpdateTime());
        }
        return ResultUtils.success(delayedTasksService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<DelayedTasks> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(DelayedTasks::getId);

        List<DelayedTasks> list =delayedTasksService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getId(), l.getId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody DelayedTasksVO vo) {
        DelayedTasks entity =  DelayedTasks.builder()
                    .taskType(vo.getTaskType())
                    .taskData(vo.getTaskData())
                    .status(vo.getStatus())
                    .executeTime(vo.getExecuteTime())
                .build();
        boolean flag =this.delayedTasksService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("delayedTasks").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody DelayedTasksVO vo) {
        DelayedTasks entity =  DelayedTasks.builder()
                    .id(vo.getId())
                    .taskType(vo.getTaskType())
                    .taskData(vo.getTaskData())
                    .status(vo.getStatus())
                    .executeTime(vo.getExecuteTime())
                .build();
        boolean flag = this.delayedTasksService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("delayedTasks").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer id) {
        DelayedTasks entity = this.delayedTasksService.getById(id);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer id) {
        boolean flag = this.delayedTasksService.removeById(id);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("delayedTasks").data( DelayedTasks.builder().id(id).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody DelayedTasksPageVO pageVO) {

        LambdaQueryWrapper<DelayedTasks> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getTaskType())) {
                            queryWrapper.eq(DelayedTasks::getTaskType, pageVO.getTaskType());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getTaskData())) {
                            queryWrapper.eq(DelayedTasks::getTaskData, pageVO.getTaskData());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getStatus())) {
                            queryWrapper.eq(DelayedTasks::getStatus, pageVO.getStatus());
                        }
                        if(pageVO.getExecuteTime() != null){
                            queryWrapper.eq(DelayedTasks::getExecuteTime, pageVO.getExecuteTime());
                        }
                        if(pageVO.getCreateTime() != null){
                            queryWrapper.eq(DelayedTasks::getCreateTime, pageVO.getCreateTime());
                        }
                        if(pageVO.getUpdateTime() != null){
                            queryWrapper.eq(DelayedTasks::getUpdateTime, pageVO.getUpdateTime());
                        }
            List<DelayedTasks> list = delayedTasksService.list(queryWrapper);
            ExcelUtil<DelayedTasks> util = new ExcelUtil<>(DelayedTasks.class);
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
            excelProvider.importData(file, DelayedTasks.class, delayedTasksService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(DelayedTasks.class);
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






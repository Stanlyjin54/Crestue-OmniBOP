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
import com.kuafuweb.web.entity.PermissionLog;
import com.kuafuweb.web.service.IPermissionLogService;
import com.kuafuweb.web.vo.PermissionLogPageVO;
import com.kuafuweb.web.vo.PermissionLogVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 权限变更日志 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/permissionLog")
@Api(tags = {"权限变更日志"})
public class PermissionLogController  {

    private final IPermissionLogService permissionLogService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody PermissionLogPageVO pageVO){
        IPage<PermissionLog> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoId() != null){
            queryWrapper.eq(PermissionLog::getUserInfoId, pageVO.getUserInfoId());
        }
            if(StringUtils.isNotEmpty(pageVO.getActionType())) {
            queryWrapper.eq(PermissionLog::getActionType, pageVO.getActionType());
        }
            if(StringUtils.isNotEmpty(pageVO.getTargetObject())) {
            queryWrapper.eq(PermissionLog::getTargetObject, pageVO.getTargetObject());
        }
            if(StringUtils.isNotEmpty(pageVO.getChangeContent())) {
            queryWrapper.eq(PermissionLog::getChangeContent, pageVO.getChangeContent());
        }
            if(pageVO.getActionTime() != null){
            queryWrapper.eq(PermissionLog::getActionTime, pageVO.getActionTime());
        }
        return ResultUtils.success(permissionLogService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody PermissionLogVO vo){
        LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoId() != null){
            queryWrapper.eq(PermissionLog::getUserInfoId, vo.getUserInfoId());
        }
            if(StringUtils.isNotEmpty(vo.getActionType())) {
            queryWrapper.eq(PermissionLog::getActionType, vo.getActionType());
        }
            if(StringUtils.isNotEmpty(vo.getTargetObject())) {
            queryWrapper.eq(PermissionLog::getTargetObject, vo.getTargetObject());
        }
            if(StringUtils.isNotEmpty(vo.getChangeContent())) {
            queryWrapper.eq(PermissionLog::getChangeContent, vo.getChangeContent());
        }
                if(vo.getActionTime() != null){
            queryWrapper.eq(PermissionLog::getActionTime, vo.getActionTime());
        }
        return ResultUtils.success(permissionLogService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(PermissionLog::getPermissionLogId);

        List<PermissionLog> list =permissionLogService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getPermissionLogId(), l.getPermissionLogId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody PermissionLogVO vo) {
        PermissionLog entity =  PermissionLog.builder()
                    .userInfoId(vo.getUserInfoId())
                    .actionType(vo.getActionType())
                    .targetObject(vo.getTargetObject())
                    .changeContent(vo.getChangeContent())
                    .actionTime(vo.getActionTime())
                .build();
        boolean flag =this.permissionLogService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("permissionLog").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody PermissionLogVO vo) {
        PermissionLog entity =  PermissionLog.builder()
                    .permissionLogId(vo.getPermissionLogId())
                    .userInfoId(vo.getUserInfoId())
                    .actionType(vo.getActionType())
                    .targetObject(vo.getTargetObject())
                    .changeContent(vo.getChangeContent())
                    .actionTime(vo.getActionTime())
                .build();
        boolean flag = this.permissionLogService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("permissionLog").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer permissionLogId) {
        PermissionLog entity = this.permissionLogService.getById(permissionLogId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer permissionLogId) {
        boolean flag = this.permissionLogService.removeById(permissionLogId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("permissionLog").data( PermissionLog.builder().permissionLogId(permissionLogId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody PermissionLogPageVO pageVO) {

        LambdaQueryWrapper<PermissionLog> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getUserInfoId() != null){
                            queryWrapper.eq(PermissionLog::getUserInfoId, pageVO.getUserInfoId());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getActionType())) {
                            queryWrapper.eq(PermissionLog::getActionType, pageVO.getActionType());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getTargetObject())) {
                            queryWrapper.eq(PermissionLog::getTargetObject, pageVO.getTargetObject());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getChangeContent())) {
                            queryWrapper.eq(PermissionLog::getChangeContent, pageVO.getChangeContent());
                        }
                        if(pageVO.getActionTime() != null){
                            queryWrapper.eq(PermissionLog::getActionTime, pageVO.getActionTime());
                        }
            List<PermissionLog> list = permissionLogService.list(queryWrapper);
            ExcelUtil<PermissionLog> util = new ExcelUtil<>(PermissionLog.class);
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
            excelProvider.importData(file, PermissionLog.class, permissionLogService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(PermissionLog.class);
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






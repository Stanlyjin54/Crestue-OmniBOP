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
import com.kuafuweb.web.entity.Permission;
import com.kuafuweb.web.service.IPermissionService;
import com.kuafuweb.web.vo.PermissionPageVO;
import com.kuafuweb.web.vo.PermissionVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 权限 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
@Api(tags = {"权限"})
public class PermissionController  {

    private final IPermissionService permissionService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody PermissionPageVO pageVO){
        IPage<Permission> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getPermissionName())) {
            queryWrapper.like(Permission::getPermissionName, pageVO.getPermissionName());
        }
            if(StringUtils.isNotEmpty(pageVO.getPermissionDesc())) {
            queryWrapper.eq(Permission::getPermissionDesc, pageVO.getPermissionDesc());
        }
            if(StringUtils.isNotEmpty(pageVO.getFeatureCode())) {
            queryWrapper.eq(Permission::getFeatureCode, pageVO.getFeatureCode());
        }
        return ResultUtils.success(permissionService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody PermissionVO vo){
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPermissionName())) {
            queryWrapper.like(Permission::getPermissionName, vo.getPermissionName());
        }
            if(StringUtils.isNotEmpty(vo.getPermissionDesc())) {
            queryWrapper.eq(Permission::getPermissionDesc, vo.getPermissionDesc());
        }
            if(StringUtils.isNotEmpty(vo.getFeatureCode())) {
            queryWrapper.eq(Permission::getFeatureCode, vo.getFeatureCode());
        }
        return ResultUtils.success(permissionService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(Permission::getPermissionId);

        List<Permission> list =permissionService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getPermissionId(), l.getPermissionId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody PermissionVO vo) {
        Permission entity =  Permission.builder()
                    .permissionName(vo.getPermissionName())
                    .permissionDesc(vo.getPermissionDesc())
                    .featureCode(vo.getFeatureCode())
                .build();
        boolean flag =this.permissionService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("permission").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody PermissionVO vo) {
        Permission entity =  Permission.builder()
                    .permissionId(vo.getPermissionId())
                    .permissionName(vo.getPermissionName())
                    .permissionDesc(vo.getPermissionDesc())
                    .featureCode(vo.getFeatureCode())
                .build();
        boolean flag = this.permissionService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("permission").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer permissionId) {
        Permission entity = this.permissionService.getById(permissionId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer permissionId) {
        boolean flag = this.permissionService.removeById(permissionId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("permission").data( Permission.builder().permissionId(permissionId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody PermissionPageVO pageVO) {

        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getPermissionName())) {
                                    queryWrapper.like(Permission::getPermissionName, pageVO.getPermissionName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getPermissionDesc())) {
                            queryWrapper.eq(Permission::getPermissionDesc, pageVO.getPermissionDesc());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getFeatureCode())) {
                            queryWrapper.eq(Permission::getFeatureCode, pageVO.getFeatureCode());
                        }
            List<Permission> list = permissionService.list(queryWrapper);
            ExcelUtil<Permission> util = new ExcelUtil<>(Permission.class);
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
            excelProvider.importData(file, Permission.class, permissionService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(Permission.class);
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






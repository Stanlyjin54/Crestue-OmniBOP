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
import com.kuafuweb.web.entity.RolePermission;
import com.kuafuweb.web.service.IRolePermissionService;
import com.kuafuweb.web.vo.RolePermissionPageVO;
import com.kuafuweb.web.vo.RolePermissionVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 角色权限关联 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rolePermission")
@Api(tags = {"角色权限关联"})
public class RolePermissionController  {

    private final IRolePermissionService rolePermissionService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody RolePermissionPageVO pageVO){
        IPage<RolePermission> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getRoleId() != null){
            queryWrapper.eq(RolePermission::getRoleId, pageVO.getRoleId());
        }
            if(pageVO.getPermissionId() != null){
            queryWrapper.eq(RolePermission::getPermissionId, pageVO.getPermissionId());
        }
        return ResultUtils.success(rolePermissionService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody RolePermissionVO vo){
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getRoleId() != null){
            queryWrapper.eq(RolePermission::getRoleId, vo.getRoleId());
        }
                if(vo.getPermissionId() != null){
            queryWrapper.eq(RolePermission::getPermissionId, vo.getPermissionId());
        }
        return ResultUtils.success(rolePermissionService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(RolePermission::getRolePermissionId);

        List<RolePermission> list =rolePermissionService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getRolePermissionId(), l.getRolePermissionId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody RolePermissionVO vo) {
        RolePermission entity =  RolePermission.builder()
                    .roleId(vo.getRoleId())
                    .permissionId(vo.getPermissionId())
                .build();
        boolean flag =this.rolePermissionService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("rolePermission").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody RolePermissionVO vo) {
        RolePermission entity =  RolePermission.builder()
                    .rolePermissionId(vo.getRolePermissionId())
                    .roleId(vo.getRoleId())
                    .permissionId(vo.getPermissionId())
                .build();
        boolean flag = this.rolePermissionService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("rolePermission").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer rolePermissionId) {
        RolePermission entity = this.rolePermissionService.getById(rolePermissionId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer rolePermissionId) {
        boolean flag = this.rolePermissionService.removeById(rolePermissionId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("rolePermission").data( RolePermission.builder().rolePermissionId(rolePermissionId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody RolePermissionPageVO pageVO) {

        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getRoleId() != null){
                            queryWrapper.eq(RolePermission::getRoleId, pageVO.getRoleId());
                        }
                        if(pageVO.getPermissionId() != null){
                            queryWrapper.eq(RolePermission::getPermissionId, pageVO.getPermissionId());
                        }
            List<RolePermission> list = rolePermissionService.list(queryWrapper);
            ExcelUtil<RolePermission> util = new ExcelUtil<>(RolePermission.class);
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
            excelProvider.importData(file, RolePermission.class, rolePermissionService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(RolePermission.class);
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






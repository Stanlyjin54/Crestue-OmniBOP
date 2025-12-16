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
import com.kuafuweb.web.entity.Role;
import com.kuafuweb.web.service.IRoleService;
import com.kuafuweb.web.vo.RolePageVO;
import com.kuafuweb.web.vo.RoleVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 角色 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@Api(tags = {"角色"})
public class RoleController  {

    private final IRoleService roleService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody RolePageVO pageVO){
        IPage<Role> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getRoleName())) {
            queryWrapper.like(Role::getRoleName, pageVO.getRoleName());
        }
            if(StringUtils.isNotEmpty(pageVO.getRoleDesc())) {
            queryWrapper.eq(Role::getRoleDesc, pageVO.getRoleDesc());
        }
            if(pageVO.getCreateTime() != null){
            queryWrapper.eq(Role::getCreateTime, pageVO.getCreateTime());
        }
        return ResultUtils.success(roleService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody RoleVO vo){
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getRoleName())) {
            queryWrapper.like(Role::getRoleName, vo.getRoleName());
        }
            if(StringUtils.isNotEmpty(vo.getRoleDesc())) {
            queryWrapper.eq(Role::getRoleDesc, vo.getRoleDesc());
        }
                if(vo.getCreateTime() != null){
            queryWrapper.eq(Role::getCreateTime, vo.getCreateTime());
        }
        return ResultUtils.success(roleService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(Role::getRoleId);

        List<Role> list =roleService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getRoleId(), l.getRoleId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody RoleVO vo) {
        Role entity =  Role.builder()
                    .roleName(vo.getRoleName())
                    .roleDesc(vo.getRoleDesc())
                .build();
        boolean flag =this.roleService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("role").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody RoleVO vo) {
        Role entity =  Role.builder()
                    .roleId(vo.getRoleId())
                    .roleName(vo.getRoleName())
                    .roleDesc(vo.getRoleDesc())
                .build();
        boolean flag = this.roleService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("role").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer roleId) {
        Role entity = this.roleService.getById(roleId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer roleId) {
        boolean flag = this.roleService.removeById(roleId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("role").data( Role.builder().roleId(roleId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody RolePageVO pageVO) {

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getRoleName())) {
                                    queryWrapper.like(Role::getRoleName, pageVO.getRoleName());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getRoleDesc())) {
                            queryWrapper.eq(Role::getRoleDesc, pageVO.getRoleDesc());
                        }
                        if(pageVO.getCreateTime() != null){
                            queryWrapper.eq(Role::getCreateTime, pageVO.getCreateTime());
                        }
            List<Role> list = roleService.list(queryWrapper);
            ExcelUtil<Role> util = new ExcelUtil<>(Role.class);
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
            excelProvider.importData(file, Role.class, roleService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(Role.class);
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






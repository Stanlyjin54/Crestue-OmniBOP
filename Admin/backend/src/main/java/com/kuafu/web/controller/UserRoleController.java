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
import com.kuafuweb.web.entity.UserRole;
import com.kuafuweb.web.service.IUserRoleService;
import com.kuafuweb.web.vo.UserRolePageVO;
import com.kuafuweb.web.vo.UserRoleVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 用户角色关联 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/userRole")
@Api(tags = {"用户角色关联"})
public class UserRoleController  {

    private final IUserRoleService userRoleService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody UserRolePageVO pageVO){
        IPage<UserRole> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();

            if(pageVO.getUserInfoId() != null){
            queryWrapper.eq(UserRole::getUserInfoId, pageVO.getUserInfoId());
        }
            if(pageVO.getRoleId() != null){
            queryWrapper.eq(UserRole::getRoleId, pageVO.getRoleId());
        }
        return ResultUtils.success(userRoleService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody UserRoleVO vo){
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();

                if(vo.getUserInfoId() != null){
            queryWrapper.eq(UserRole::getUserInfoId, vo.getUserInfoId());
        }
                if(vo.getRoleId() != null){
            queryWrapper.eq(UserRole::getRoleId, vo.getRoleId());
        }
        return ResultUtils.success(userRoleService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(UserRole::getUserRoleId);

        List<UserRole> list =userRoleService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getUserRoleId(), l.getUserRoleId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody UserRoleVO vo) {
        UserRole entity =  UserRole.builder()
                    .userInfoId(vo.getUserInfoId())
                    .roleId(vo.getRoleId())
                .build();
        boolean flag =this.userRoleService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("userRole").data(entity).build());
                }


        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody UserRoleVO vo) {
        UserRole entity =  UserRole.builder()
                    .userRoleId(vo.getUserRoleId())
                    .userInfoId(vo.getUserInfoId())
                    .roleId(vo.getRoleId())
                .build();
        boolean flag = this.userRoleService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("userRole").data(entity).build());
                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer userRoleId) {
        UserRole entity = this.userRoleService.getById(userRoleId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer userRoleId) {
        boolean flag = this.userRoleService.removeById(userRoleId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("userRole").data( UserRole.builder().userRoleId(userRoleId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody UserRolePageVO pageVO) {

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();

                        if(pageVO.getUserInfoId() != null){
                            queryWrapper.eq(UserRole::getUserInfoId, pageVO.getUserInfoId());
                        }
                        if(pageVO.getRoleId() != null){
                            queryWrapper.eq(UserRole::getRoleId, pageVO.getRoleId());
                        }
            List<UserRole> list = userRoleService.list(queryWrapper);
            ExcelUtil<UserRole> util = new ExcelUtil<>(UserRole.class);
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
            excelProvider.importData(file, UserRole.class, userRoleService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(UserRole.class);
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






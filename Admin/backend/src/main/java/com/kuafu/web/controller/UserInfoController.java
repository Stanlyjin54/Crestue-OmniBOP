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
import com.kuafuweb.web.entity.UserInfo;
import com.kuafuweb.web.service.IUserInfoService;
import com.kuafuweb.web.vo.UserInfoPageVO;
import com.kuafuweb.web.vo.UserInfoVO;
import com.kuafuweb.web.event.MyEventService;
import com.kuafuweb.web.event.EventVo;
/**
 * <p> 用户信息 接口 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/userInfo")
@Api(tags = {"用户信息"})
public class UserInfoController  {

    private final IUserInfoService userInfoService;

    private final MyEventService myEventService;

    private final ExcelProvider excelProvider;
    private final IStaticResourceService staticResourceService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page( @RequestBody UserInfoPageVO pageVO){
        IPage<UserInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
            queryWrapper.eq(UserInfo::getPhoneNumber, pageVO.getPhoneNumber());
        }
            if(StringUtils.isNotEmpty(pageVO.getUsername())) {
            queryWrapper.like(UserInfo::getUsername, pageVO.getUsername());
        }
            if(StringUtils.isNotEmpty(pageVO.getPassword())) {
            queryWrapper.eq(UserInfo::getPassword, pageVO.getPassword());
        }
            if(StringUtils.isNotEmpty(pageVO.getAvatarResourceKey())) {
            queryWrapper.eq(UserInfo::getAvatarResourceKey, pageVO.getAvatarResourceKey());
        }
            if(pageVO.getRegistrationDate() != null){
            queryWrapper.eq(UserInfo::getRegistrationDate, pageVO.getRegistrationDate());
        }
        return ResultUtils.success(userInfoService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list( @RequestBody UserInfoVO vo){
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

            if(StringUtils.isNotEmpty(vo.getPhoneNumber())) {
            queryWrapper.eq(UserInfo::getPhoneNumber, vo.getPhoneNumber());
        }
            if(StringUtils.isNotEmpty(vo.getUsername())) {
            queryWrapper.like(UserInfo::getUsername, vo.getUsername());
        }
            if(StringUtils.isNotEmpty(vo.getPassword())) {
            queryWrapper.eq(UserInfo::getPassword, vo.getPassword());
        }
            if(StringUtils.isNotEmpty(vo.getAvatarResourceKey())) {
            queryWrapper.eq(UserInfo::getAvatarResourceKey, vo.getAvatarResourceKey());
        }
                if(vo.getRegistrationDate() != null){
            queryWrapper.eq(UserInfo::getRegistrationDate, vo.getRegistrationDate());
        }
        return ResultUtils.success(userInfoService.list(queryWrapper));
    }


            @PostMapping("get_select_list")
            @ApiOperation("获取所有的下拉列表")
            public BaseResponse get_select_list( ){
                LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(UserInfo::getUserInfoId);

        List<UserInfo> list =userInfoService.list(queryWrapper);
        final List<SelectVO> selectVOS = list.stream().map(l -> new SelectVO(l.getUserInfoId(), l.getUserInfoId().toString())).collect(Collectors.toList());
        return ResultUtils.success(selectVOS);

                }

    @PostMapping("add")
    @ApiOperation("新增")
    @SensitiveFilter
    public BaseResponse add( @RequestBody UserInfoVO vo) {
        UserInfo entity =  UserInfo.builder()
                    .phoneNumber(vo.getPhoneNumber())
                    .username(vo.getUsername())
                    .password(vo.getPassword())
                  .avatarResourceKey(vo.getAvatarResourceKey() == null ? "default_resource_key" : vo.getAvatarResourceKey())
                    .registrationDate(vo.getRegistrationDate())
                .build();
        boolean flag =this.userInfoService.save(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("add").tableName("userInfo").data(entity).build());
                }

                final List<StaticResource> AvatarImage = vo.getAvatar();
                if (AvatarImage  != null && !AvatarImage.isEmpty()) {
                    AvatarImage.forEach(i -> {
                        i.setRelateTableColumnName("avatar");
                        i.setRelatedTableName("user_info");
                        i.setRelatedTableKey(entity.getUserInfoId());
                        i.setResourcePath(i.getResourceName());
                    });

                    staticResourceService.saveBatch(AvatarImage);

                }

        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    @SensitiveFilter
    public BaseResponse update( @RequestBody UserInfoVO vo) {
        UserInfo entity =  UserInfo.builder()
                    .userInfoId(vo.getUserInfoId())
                    .phoneNumber(vo.getPhoneNumber())
                    .username(vo.getUsername())
                    .password(vo.getPassword())
                    .avatarResourceKey(vo.getAvatarResourceKey())
                    .registrationDate(vo.getRegistrationDate())
                .build();
        boolean flag = this.userInfoService.updateById(entity);
                if (flag) {
                    myEventService.publishEvent(EventVo.builder().model("update").tableName("userInfo").data(entity).build());
                }

                final LambdaQueryWrapper<StaticResource> avatarqueryWrapper = new LambdaQueryWrapper<>();
                avatarqueryWrapper
                        .eq(StaticResource::getRelatedTableName,"user_info")
                        .eq(StaticResource::getRelateTableColumnName,"avatar")
                        .eq(StaticResource::getRelatedTableKey,entity.getUserInfoId());
                staticResourceService.remove(avatarqueryWrapper);


                final List<StaticResource> AvatarImage = vo.getAvatar();
                if (AvatarImage  != null && !AvatarImage.isEmpty()) {
                    AvatarImage.forEach(i -> {
                        i.setRelateTableColumnName("avatar");
                        i.setRelatedTableName("user_info");
                        i.setRelatedTableKey(entity.getUserInfoId());
                        if(StringUtils.isNotEmpty(i.getResourceName())) {
                            i.setResourcePath(i.getResourceName());
                        }
                    });

                    staticResourceService.saveBatch(AvatarImage);

                }



        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value="id") Integer userInfoId) {
        UserInfo entity = this.userInfoService.getById(userInfoId);
        return entity!=null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @DeleteMapping("delete/{id}")
    @ApiOperation("删除")
    public BaseResponse delete(@PathVariable(value="id") Integer userInfoId) {
        boolean flag = this.userInfoService.removeById(userInfoId);
        if (flag) {
            myEventService.publishEvent(EventVo.builder().model("delete").tableName("userInfo").data( UserInfo.builder().userInfoId(userInfoId).build() ).build());
        }
        return flag ? ResultUtils.success() : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 导出excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody UserInfoPageVO pageVO) {

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

                    if(StringUtils.isNotEmpty(pageVO.getPhoneNumber())) {
                            queryWrapper.eq(UserInfo::getPhoneNumber, pageVO.getPhoneNumber());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getUsername())) {
                                    queryWrapper.like(UserInfo::getUsername, pageVO.getUsername());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getPassword())) {
                            queryWrapper.eq(UserInfo::getPassword, pageVO.getPassword());
                        }
                    if(StringUtils.isNotEmpty(pageVO.getAvatarResourceKey())) {
                            queryWrapper.eq(UserInfo::getAvatarResourceKey, pageVO.getAvatarResourceKey());
                        }
                        if(pageVO.getRegistrationDate() != null){
                            queryWrapper.eq(UserInfo::getRegistrationDate, pageVO.getRegistrationDate());
                        }
            List<UserInfo> list = userInfoService.list(queryWrapper);
            ExcelUtil<UserInfo> util = new ExcelUtil<>(UserInfo.class);
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
            excelProvider.importData(file, UserInfo.class, userInfoService::saveBatch);
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
                        Workbook workbook = excelProvider.downloadExcelTemplate(UserInfo.class);
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






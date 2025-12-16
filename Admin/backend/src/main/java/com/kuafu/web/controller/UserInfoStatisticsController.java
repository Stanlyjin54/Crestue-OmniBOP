package com.kuafuweb.web.controller;

import java.util.List;
import java.util.stream.Collectors;
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.kuafuweb.common.domin.BaseResponse;
import com.kuafuweb.common.domin.ErrorCode;
import com.kuafuweb.common.domin.ResultUtils;
import com.kuafuweb.common.util.StringUtils;
import com.kuafuweb.web.entity.UserInfo;
import com.kuafuweb.web.service.IUserInfoStatisticsService;
import com.kuafuweb.web.entity.SelectVO;

import com.kuafuweb.web.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;





/**

 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/userInfoStatistics")

public class UserInfoStatisticsController  {

    private final IUserInfoStatisticsService userInfoStatisticsService;


    @PostMapping("user_info_registration_date_datetime_statistic_2ec47ef3_count")
    public BaseResponse user_info_registration_date_datetime_statistic_2ec47ef3_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_registration_date_datetime_statistic_2ec47ef3_count(queryWrapper));
    }
    @PostMapping("user_info_registration_date_datetime_statistic_2de39a8d_count")
    public BaseResponse user_info_registration_date_datetime_statistic_2de39a8d_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_registration_date_datetime_statistic_2de39a8d_count(queryWrapper));
    }
    @PostMapping("user_info_registration_date_datetime_statistic_2d54c52e_count")
    public BaseResponse user_info_registration_date_datetime_statistic_2d54c52e_count(@RequestBody UserInfo statisticVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapperParams(queryWrapper, statisticVo);

                    
        return ResultUtils.success( userInfoStatisticsService.user_info_registration_date_datetime_statistic_2d54c52e_count(queryWrapper));
    }


    private void queryWrapperParams(LambdaQueryWrapper<UserInfo> queryWrapper, UserInfo statisticVo){
        if(StringUtils.isNotEmpty(statisticVo.getPhoneNumber())) {
                queryWrapper.eq(UserInfo::getPhoneNumber, statisticVo.getPhoneNumber());
            }
        if(StringUtils.isNotEmpty(statisticVo.getUsername())) {
                        queryWrapper.like(UserInfo::getUsername, statisticVo.getUsername());
            }
        if(StringUtils.isNotEmpty(statisticVo.getPassword())) {
                queryWrapper.eq(UserInfo::getPassword, statisticVo.getPassword());
            }
        if(StringUtils.isNotEmpty(statisticVo.getAvatarResourceKey())) {
                queryWrapper.eq(UserInfo::getAvatarResourceKey, statisticVo.getAvatarResourceKey());
            }
            if(statisticVo.getRegistrationDate() != null){
                queryWrapper.eq(UserInfo::getRegistrationDate, statisticVo.getRegistrationDate());
            }
    }



}






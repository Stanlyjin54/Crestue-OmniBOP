package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  user_info 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IUserInfoStatisticsService extends IService<UserInfo> {
        List<Object> user_info_registration_date_datetime_statistic_2ec47ef3_count(LambdaQueryWrapper queryWrapper);

        List<Object> user_info_registration_date_datetime_statistic_2de39a8d_count(LambdaQueryWrapper queryWrapper);

        List<Object> user_info_registration_date_datetime_statistic_2d54c52e_count(LambdaQueryWrapper queryWrapper);

}






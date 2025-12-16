package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.UserInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> user_info Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface UserInfoStatisticsMapper extends BaseMapper<UserInfo> {

                        List<Object> user_info_registration_date_datetime_statistic_2ec47ef3_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> user_info_registration_date_datetime_statistic_2de39a8d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> user_info_registration_date_datetime_statistic_2d54c52e_count(@Param("ew") Wrapper queryWrapper);

}






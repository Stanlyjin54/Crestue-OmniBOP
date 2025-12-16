package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.PermissionLog;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> permission_log Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface PermissionLogStatisticsMapper extends BaseMapper<PermissionLog> {

                        List<Object> permission_log_action_time_datetime_statistic_84170e10_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> permission_log_action_time_datetime_statistic_975db700_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> permission_log_action_time_datetime_statistic_8cf06955_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.Role;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> role Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface RoleStatisticsMapper extends BaseMapper<Role> {

                        List<Object> role_create_time_datetime_statistic_f1dec0e3_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> role_create_time_datetime_statistic_30529d9f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> role_create_time_datetime_statistic_99f55dc9_count(@Param("ew") Wrapper queryWrapper);

}






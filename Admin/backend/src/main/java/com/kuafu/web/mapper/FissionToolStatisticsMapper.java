package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.FissionTool;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> fission_tool Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface FissionToolStatisticsMapper extends BaseMapper<FissionTool> {

                        List<Object> fission_tool_start_date_datetime_statistic_79c789ea_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> fission_tool_start_date_datetime_statistic_a2b0c036_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> fission_tool_start_date_datetime_statistic_a11aa4e6_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> fission_tool_end_date_datetime_statistic_308b2d65_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> fission_tool_end_date_datetime_statistic_955257d7_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> fission_tool_end_date_datetime_statistic_700394af_count(@Param("ew") Wrapper queryWrapper);

}






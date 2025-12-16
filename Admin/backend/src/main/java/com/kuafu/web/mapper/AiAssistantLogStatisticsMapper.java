package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.AiAssistantLog;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> ai_assistant_log Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface AiAssistantLogStatisticsMapper extends BaseMapper<AiAssistantLog> {

                        List<Object> ai_assistant_log_query_time_datetime_statistic_9b682c72_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> ai_assistant_log_query_time_datetime_statistic_df5261a6_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> ai_assistant_log_query_time_datetime_statistic_d691e2ab_count(@Param("ew") Wrapper queryWrapper);

}






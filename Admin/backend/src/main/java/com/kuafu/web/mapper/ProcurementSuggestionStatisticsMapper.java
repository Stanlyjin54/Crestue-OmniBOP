package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.ProcurementSuggestion;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> procurement_suggestion Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface ProcurementSuggestionStatisticsMapper extends BaseMapper<ProcurementSuggestion> {

                        List<Object> procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> procurement_suggestion_suggest_date_datetime_statistic_00313900_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.ProfitBoard;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> profit_board Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface ProfitBoardStatisticsMapper extends BaseMapper<ProfitBoard> {

                        List<Object> profit_board_stat_date_datetime_statistic_17470edd_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> profit_board_stat_date_datetime_statistic_edf98856_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> profit_board_stat_date_datetime_statistic_f80cc477_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.RfmLevelEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> rfm_level_enum Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface RfmLevelEnumStatisticsMapper extends BaseMapper<RfmLevelEnum> {

                        List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count(@Param("ew") Wrapper queryWrapper);

}






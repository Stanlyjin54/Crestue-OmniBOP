package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.BusinessOpportunity;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> business_opportunity Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface BusinessOpportunityStatisticsMapper extends BaseMapper<BusinessOpportunity> {

                        List<Object> business_opportunity_status_status_statistic_7faf0bdd_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> business_opportunity_status_status_statistic_5ba2a19d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> business_opportunity_status_status_statistic_3d13a485_count(@Param("ew") Wrapper queryWrapper);

}






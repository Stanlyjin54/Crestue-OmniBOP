package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.CustomerInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> customer_info Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface CustomerInfoStatisticsMapper extends BaseMapper<CustomerInfo> {

                        List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_ef82b098_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_77cd48e4_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_b209e896_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_376f16a9_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_dc7b8cc0_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_c56bdc0c_count(@Param("ew") Wrapper queryWrapper);

}






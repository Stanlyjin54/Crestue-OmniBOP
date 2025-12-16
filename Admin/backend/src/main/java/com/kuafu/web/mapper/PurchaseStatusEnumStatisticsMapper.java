package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.PurchaseStatusEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> purchase_status_enum Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface PurchaseStatusEnumStatisticsMapper extends BaseMapper<PurchaseStatusEnum> {

                        List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_cac5b56b_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_4879421d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_d51abb7f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_status_enum_status_name_status_statistic_ae1dfb7a_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_status_enum_status_name_status_statistic_b11589b5_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_status_enum_status_name_status_statistic_4fce27d5_count(@Param("ew") Wrapper queryWrapper);

}






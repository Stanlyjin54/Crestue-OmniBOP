package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.ProductStatusEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> product_status_enum Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface ProductStatusEnumStatisticsMapper extends BaseMapper<ProductStatusEnum> {

                        List<Object> product_status_enum_product_status_enum_id_status_statistic_d77ca5ce_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_status_enum_product_status_enum_id_status_statistic_e288bf0e_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_status_enum_product_status_enum_id_status_statistic_3e0cb3c6_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_status_enum_status_name_status_statistic_fe3afe34_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_status_enum_status_name_status_statistic_1a658de2_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_status_enum_status_name_status_statistic_8130dea7_count(@Param("ew") Wrapper queryWrapper);

}






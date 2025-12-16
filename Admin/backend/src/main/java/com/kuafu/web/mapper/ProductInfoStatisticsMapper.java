package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.ProductInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> product_info Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface ProductInfoStatisticsMapper extends BaseMapper<ProductInfo> {

                        List<Object> product_info_shelf_life_datetime_statistic_13b09a91_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_info_shelf_life_datetime_statistic_fabe9b9f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_info_shelf_life_datetime_statistic_b986a1ea_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count(@Param("ew") Wrapper queryWrapper);

}






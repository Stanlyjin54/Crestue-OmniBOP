package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.OrderInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> order_info Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface OrderInfoStatisticsMapper extends BaseMapper<OrderInfo> {

                        List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_ad7a7387_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_b94f62d5_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_b8881331_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_info_order_date_datetime_statistic_4a683c7d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_info_order_date_datetime_statistic_d9005f45_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_info_order_date_datetime_statistic_8d28825b_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.OrderStatusEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> order_status_enum Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface OrderStatusEnumStatisticsMapper extends BaseMapper<OrderStatusEnum> {

                        List<Object> order_status_enum_order_status_enum_id_status_statistic_ed6cd955_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_status_enum_order_status_enum_id_status_statistic_dcd3b45b_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_status_enum_order_status_enum_id_status_statistic_d8e4b58d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_status_enum_status_name_status_statistic_cd290941_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_status_enum_status_name_status_statistic_962848ae_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> order_status_enum_status_name_status_statistic_dcb4f308_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.TransferInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> transfer_info Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface TransferInfoStatisticsMapper extends BaseMapper<TransferInfo> {

                        List<Object> transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_0b0d5ec1_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_3a04bd30_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_92a0f49d_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_apply_date_datetime_statistic_1ce09557_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_apply_date_datetime_statistic_747d5b62_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_apply_date_datetime_statistic_7f4dfcee_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_confirm_date_datetime_statistic_843e5a5b_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_confirm_date_datetime_statistic_c99d1d40_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_info_confirm_date_datetime_statistic_9e5bc169_count(@Param("ew") Wrapper queryWrapper);

}






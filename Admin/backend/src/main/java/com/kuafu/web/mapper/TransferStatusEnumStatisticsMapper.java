package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.TransferStatusEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> transfer_status_enum Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface TransferStatusEnumStatisticsMapper extends BaseMapper<TransferStatusEnum> {

                        List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_5bfcb35a_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_35b1149a_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_26443357_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_status_enum_status_name_status_statistic_29f93e61_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_status_enum_status_name_status_statistic_a78fbbbf_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> transfer_status_enum_status_name_status_statistic_58b07edd_count(@Param("ew") Wrapper queryWrapper);

}






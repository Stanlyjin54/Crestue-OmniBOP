package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.PurchaseInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> purchase_info Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface PurchaseInfoStatisticsMapper extends BaseMapper<PurchaseInfo> {

                        List<Object> purchase_info_purchase_date_datetime_statistic_2bd78ba1_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_info_purchase_date_datetime_statistic_11ee5598_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_info_purchase_date_datetime_statistic_8e593cc4_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_c751e7be_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_4d195a98_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_f7e55886_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.InventoryRecord;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> inventory_record Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface InventoryRecordStatisticsMapper extends BaseMapper<InventoryRecord> {

                        List<Object> inventory_record_expire_date_datetime_statistic_a38e9712_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> inventory_record_expire_date_datetime_statistic_00541631_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> inventory_record_expire_date_datetime_statistic_5296a1bd_count(@Param("ew") Wrapper queryWrapper);

}






package com.kuafuweb.web.mapper;

import java.util.List;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kuafuweb.web.entity.CustomerTagEnum;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
/**
 * <p> customer_tag_enum Mapper </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Mapper
public interface CustomerTagEnumStatisticsMapper extends BaseMapper<CustomerTagEnum> {

                        List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count(@Param("ew") Wrapper queryWrapper);


                        List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count(@Param("ew") Wrapper queryWrapper);

}






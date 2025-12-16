package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.CustomerInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  customer_info 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ICustomerInfoStatisticsService extends IService<CustomerInfo> {
        List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_ef82b098_count(LambdaQueryWrapper queryWrapper);

        List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_77cd48e4_count(LambdaQueryWrapper queryWrapper);

        List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_b209e896_count(LambdaQueryWrapper queryWrapper);

        List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_376f16a9_count(LambdaQueryWrapper queryWrapper);

        List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_dc7b8cc0_count(LambdaQueryWrapper queryWrapper);

        List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_c56bdc0c_count(LambdaQueryWrapper queryWrapper);

}






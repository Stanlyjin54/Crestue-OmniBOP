package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.OrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  order_info 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IOrderInfoStatisticsService extends IService<OrderInfo> {
        List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_ad7a7387_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_b94f62d5_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_b8881331_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_info_order_date_datetime_statistic_4a683c7d_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_info_order_date_datetime_statistic_d9005f45_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_info_order_date_datetime_statistic_8d28825b_count(LambdaQueryWrapper queryWrapper);

}






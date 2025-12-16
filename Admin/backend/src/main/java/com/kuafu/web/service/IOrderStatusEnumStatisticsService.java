package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.OrderStatusEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  order_status_enum 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IOrderStatusEnumStatisticsService extends IService<OrderStatusEnum> {
        List<Object> order_status_enum_order_status_enum_id_status_statistic_ed6cd955_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_status_enum_order_status_enum_id_status_statistic_dcd3b45b_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_status_enum_order_status_enum_id_status_statistic_d8e4b58d_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_status_enum_status_name_status_statistic_cd290941_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_status_enum_status_name_status_statistic_962848ae_count(LambdaQueryWrapper queryWrapper);

        List<Object> order_status_enum_status_name_status_statistic_dcb4f308_count(LambdaQueryWrapper queryWrapper);

}






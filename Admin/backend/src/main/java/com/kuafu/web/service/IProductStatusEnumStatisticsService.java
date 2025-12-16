package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.ProductStatusEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  product_status_enum 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IProductStatusEnumStatisticsService extends IService<ProductStatusEnum> {
        List<Object> product_status_enum_product_status_enum_id_status_statistic_d77ca5ce_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_status_enum_product_status_enum_id_status_statistic_e288bf0e_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_status_enum_product_status_enum_id_status_statistic_3e0cb3c6_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_status_enum_status_name_status_statistic_fe3afe34_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_status_enum_status_name_status_statistic_1a658de2_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_status_enum_status_name_status_statistic_8130dea7_count(LambdaQueryWrapper queryWrapper);

}






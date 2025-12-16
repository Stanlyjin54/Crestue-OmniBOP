package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.CustomerTagEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  customer_tag_enum 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ICustomerTagEnumStatisticsService extends IService<CustomerTagEnum> {
        List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count(LambdaQueryWrapper queryWrapper);

        List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count(LambdaQueryWrapper queryWrapper);

        List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count(LambdaQueryWrapper queryWrapper);

}






package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.PurchaseStatusEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  purchase_status_enum 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IPurchaseStatusEnumStatisticsService extends IService<PurchaseStatusEnum> {
        List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_cac5b56b_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_4879421d_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_d51abb7f_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_status_enum_status_name_status_statistic_ae1dfb7a_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_status_enum_status_name_status_statistic_b11589b5_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_status_enum_status_name_status_statistic_4fce27d5_count(LambdaQueryWrapper queryWrapper);

}






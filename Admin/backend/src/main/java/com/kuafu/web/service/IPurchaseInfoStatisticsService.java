package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.PurchaseInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  purchase_info 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IPurchaseInfoStatisticsService extends IService<PurchaseInfo> {
        List<Object> purchase_info_purchase_date_datetime_statistic_2bd78ba1_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_info_purchase_date_datetime_statistic_11ee5598_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_info_purchase_date_datetime_statistic_8e593cc4_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_c751e7be_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_4d195a98_count(LambdaQueryWrapper queryWrapper);

        List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_f7e55886_count(LambdaQueryWrapper queryWrapper);

}






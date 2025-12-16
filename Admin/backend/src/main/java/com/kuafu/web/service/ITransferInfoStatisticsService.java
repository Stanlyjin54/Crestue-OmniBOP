package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.TransferInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  transfer_info 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ITransferInfoStatisticsService extends IService<TransferInfo> {
        List<Object> transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_0b0d5ec1_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_3a04bd30_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_transfer_status_enum_transfer_status_enum_id_1_status_statistic_92a0f49d_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_apply_date_datetime_statistic_1ce09557_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_apply_date_datetime_statistic_747d5b62_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_apply_date_datetime_statistic_7f4dfcee_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_confirm_date_datetime_statistic_843e5a5b_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_confirm_date_datetime_statistic_c99d1d40_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_info_confirm_date_datetime_statistic_9e5bc169_count(LambdaQueryWrapper queryWrapper);

}






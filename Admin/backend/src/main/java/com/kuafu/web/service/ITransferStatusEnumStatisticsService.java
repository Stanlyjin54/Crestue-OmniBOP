package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.TransferStatusEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  transfer_status_enum 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface ITransferStatusEnumStatisticsService extends IService<TransferStatusEnum> {
        List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_5bfcb35a_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_35b1149a_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_status_enum_transfer_status_enum_id_status_statistic_26443357_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_status_enum_status_name_status_statistic_29f93e61_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_status_enum_status_name_status_statistic_a78fbbbf_count(LambdaQueryWrapper queryWrapper);

        List<Object> transfer_status_enum_status_name_status_statistic_58b07edd_count(LambdaQueryWrapper queryWrapper);

}






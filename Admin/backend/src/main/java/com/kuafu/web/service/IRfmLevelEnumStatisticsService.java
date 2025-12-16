package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.RfmLevelEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  rfm_level_enum 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IRfmLevelEnumStatisticsService extends IService<RfmLevelEnum> {
        List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count(LambdaQueryWrapper queryWrapper);

        List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count(LambdaQueryWrapper queryWrapper);

        List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count(LambdaQueryWrapper queryWrapper);

}






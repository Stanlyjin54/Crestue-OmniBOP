package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.PermissionLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  permission_log 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IPermissionLogStatisticsService extends IService<PermissionLog> {
        List<Object> permission_log_action_time_datetime_statistic_84170e10_count(LambdaQueryWrapper queryWrapper);

        List<Object> permission_log_action_time_datetime_statistic_975db700_count(LambdaQueryWrapper queryWrapper);

        List<Object> permission_log_action_time_datetime_statistic_8cf06955_count(LambdaQueryWrapper queryWrapper);

}






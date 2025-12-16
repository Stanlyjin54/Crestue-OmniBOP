package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.Role;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  role 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IRoleStatisticsService extends IService<Role> {
        List<Object> role_create_time_datetime_statistic_f1dec0e3_count(LambdaQueryWrapper queryWrapper);

        List<Object> role_create_time_datetime_statistic_30529d9f_count(LambdaQueryWrapper queryWrapper);

        List<Object> role_create_time_datetime_statistic_99f55dc9_count(LambdaQueryWrapper queryWrapper);

}






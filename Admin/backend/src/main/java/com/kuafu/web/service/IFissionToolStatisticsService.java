package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.FissionTool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  fission_tool 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IFissionToolStatisticsService extends IService<FissionTool> {
        List<Object> fission_tool_start_date_datetime_statistic_79c789ea_count(LambdaQueryWrapper queryWrapper);

        List<Object> fission_tool_start_date_datetime_statistic_a2b0c036_count(LambdaQueryWrapper queryWrapper);

        List<Object> fission_tool_start_date_datetime_statistic_a11aa4e6_count(LambdaQueryWrapper queryWrapper);

        List<Object> fission_tool_end_date_datetime_statistic_308b2d65_count(LambdaQueryWrapper queryWrapper);

        List<Object> fission_tool_end_date_datetime_statistic_955257d7_count(LambdaQueryWrapper queryWrapper);

        List<Object> fission_tool_end_date_datetime_statistic_700394af_count(LambdaQueryWrapper queryWrapper);

}






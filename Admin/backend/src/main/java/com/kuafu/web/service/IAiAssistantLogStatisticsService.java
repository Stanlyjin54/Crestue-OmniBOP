package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.AiAssistantLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  ai_assistant_log 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IAiAssistantLogStatisticsService extends IService<AiAssistantLog> {
        List<Object> ai_assistant_log_query_time_datetime_statistic_9b682c72_count(LambdaQueryWrapper queryWrapper);

        List<Object> ai_assistant_log_query_time_datetime_statistic_df5261a6_count(LambdaQueryWrapper queryWrapper);

        List<Object> ai_assistant_log_query_time_datetime_statistic_d691e2ab_count(LambdaQueryWrapper queryWrapper);

}






package com.kuafuweb.web.service.impl;

import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.kuafuweb.web.mapper.AiAssistantLogStatisticsMapper;
import com.kuafuweb.web.entity.AiAssistantLog;
import com.kuafuweb.web.service.IAiAssistantLogStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> ai_assistant_log 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("AiAssistantLogStatistics")
public class AiAssistantLogStatisticsServiceImpl extends ServiceImpl<AiAssistantLogStatisticsMapper, AiAssistantLog> implements IAiAssistantLogStatisticsService {
    @Resource
    private AiAssistantLogStatisticsMapper aiAssistantLogStaticMapper;

                        @Override
                        public List<Object> ai_assistant_log_query_time_datetime_statistic_9b682c72_count(LambdaQueryWrapper queryWrapper) {
                                return aiAssistantLogStaticMapper.ai_assistant_log_query_time_datetime_statistic_9b682c72_count(queryWrapper);
                                }


                        @Override
                        public List<Object> ai_assistant_log_query_time_datetime_statistic_df5261a6_count(LambdaQueryWrapper queryWrapper) {
                                return aiAssistantLogStaticMapper.ai_assistant_log_query_time_datetime_statistic_df5261a6_count(queryWrapper);
                                }


                        @Override
                        public List<Object> ai_assistant_log_query_time_datetime_statistic_d691e2ab_count(LambdaQueryWrapper queryWrapper) {
                                return aiAssistantLogStaticMapper.ai_assistant_log_query_time_datetime_statistic_d691e2ab_count(queryWrapper);
                                }


}






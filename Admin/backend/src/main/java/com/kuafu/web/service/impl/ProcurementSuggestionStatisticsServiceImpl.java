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

import com.kuafuweb.web.mapper.ProcurementSuggestionStatisticsMapper;
import com.kuafuweb.web.entity.ProcurementSuggestion;
import com.kuafuweb.web.service.IProcurementSuggestionStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> procurement_suggestion 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("ProcurementSuggestionStatistics")
public class ProcurementSuggestionStatisticsServiceImpl extends ServiceImpl<ProcurementSuggestionStatisticsMapper, ProcurementSuggestion> implements IProcurementSuggestionStatisticsService {
    @Resource
    private ProcurementSuggestionStatisticsMapper procurementSuggestionStaticMapper;

                        @Override
                        public List<Object> procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count(LambdaQueryWrapper queryWrapper) {
                                return procurementSuggestionStaticMapper.procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count(queryWrapper);
                                }


                        @Override
                        public List<Object> procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count(LambdaQueryWrapper queryWrapper) {
                                return procurementSuggestionStaticMapper.procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count(queryWrapper);
                                }


                        @Override
                        public List<Object> procurement_suggestion_suggest_date_datetime_statistic_00313900_count(LambdaQueryWrapper queryWrapper) {
                                return procurementSuggestionStaticMapper.procurement_suggestion_suggest_date_datetime_statistic_00313900_count(queryWrapper);
                                }


}






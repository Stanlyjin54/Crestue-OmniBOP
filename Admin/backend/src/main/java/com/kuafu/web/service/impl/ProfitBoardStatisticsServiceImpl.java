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

import com.kuafuweb.web.mapper.ProfitBoardStatisticsMapper;
import com.kuafuweb.web.entity.ProfitBoard;
import com.kuafuweb.web.service.IProfitBoardStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> profit_board 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("ProfitBoardStatistics")
public class ProfitBoardStatisticsServiceImpl extends ServiceImpl<ProfitBoardStatisticsMapper, ProfitBoard> implements IProfitBoardStatisticsService {
    @Resource
    private ProfitBoardStatisticsMapper profitBoardStaticMapper;

                        @Override
                        public List<Object> profit_board_stat_date_datetime_statistic_17470edd_count(LambdaQueryWrapper queryWrapper) {
                                return profitBoardStaticMapper.profit_board_stat_date_datetime_statistic_17470edd_count(queryWrapper);
                                }


                        @Override
                        public List<Object> profit_board_stat_date_datetime_statistic_edf98856_count(LambdaQueryWrapper queryWrapper) {
                                return profitBoardStaticMapper.profit_board_stat_date_datetime_statistic_edf98856_count(queryWrapper);
                                }


                        @Override
                        public List<Object> profit_board_stat_date_datetime_statistic_f80cc477_count(LambdaQueryWrapper queryWrapper) {
                                return profitBoardStaticMapper.profit_board_stat_date_datetime_statistic_f80cc477_count(queryWrapper);
                                }


}






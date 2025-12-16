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

import com.kuafuweb.web.mapper.FissionToolStatisticsMapper;
import com.kuafuweb.web.entity.FissionTool;
import com.kuafuweb.web.service.IFissionToolStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> fission_tool 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("FissionToolStatistics")
public class FissionToolStatisticsServiceImpl extends ServiceImpl<FissionToolStatisticsMapper, FissionTool> implements IFissionToolStatisticsService {
    @Resource
    private FissionToolStatisticsMapper fissionToolStaticMapper;

                        @Override
                        public List<Object> fission_tool_start_date_datetime_statistic_79c789ea_count(LambdaQueryWrapper queryWrapper) {
                                return fissionToolStaticMapper.fission_tool_start_date_datetime_statistic_79c789ea_count(queryWrapper);
                                }


                        @Override
                        public List<Object> fission_tool_start_date_datetime_statistic_a2b0c036_count(LambdaQueryWrapper queryWrapper) {
                                return fissionToolStaticMapper.fission_tool_start_date_datetime_statistic_a2b0c036_count(queryWrapper);
                                }


                        @Override
                        public List<Object> fission_tool_start_date_datetime_statistic_a11aa4e6_count(LambdaQueryWrapper queryWrapper) {
                                return fissionToolStaticMapper.fission_tool_start_date_datetime_statistic_a11aa4e6_count(queryWrapper);
                                }


                        @Override
                        public List<Object> fission_tool_end_date_datetime_statistic_308b2d65_count(LambdaQueryWrapper queryWrapper) {
                                return fissionToolStaticMapper.fission_tool_end_date_datetime_statistic_308b2d65_count(queryWrapper);
                                }


                        @Override
                        public List<Object> fission_tool_end_date_datetime_statistic_955257d7_count(LambdaQueryWrapper queryWrapper) {
                                return fissionToolStaticMapper.fission_tool_end_date_datetime_statistic_955257d7_count(queryWrapper);
                                }


                        @Override
                        public List<Object> fission_tool_end_date_datetime_statistic_700394af_count(LambdaQueryWrapper queryWrapper) {
                                return fissionToolStaticMapper.fission_tool_end_date_datetime_statistic_700394af_count(queryWrapper);
                                }


}






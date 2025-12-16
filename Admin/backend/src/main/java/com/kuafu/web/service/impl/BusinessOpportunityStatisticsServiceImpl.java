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

import com.kuafuweb.web.mapper.BusinessOpportunityStatisticsMapper;
import com.kuafuweb.web.entity.BusinessOpportunity;
import com.kuafuweb.web.service.IBusinessOpportunityStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> business_opportunity 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("BusinessOpportunityStatistics")
public class BusinessOpportunityStatisticsServiceImpl extends ServiceImpl<BusinessOpportunityStatisticsMapper, BusinessOpportunity> implements IBusinessOpportunityStatisticsService {
    @Resource
    private BusinessOpportunityStatisticsMapper businessOpportunityStaticMapper;

                        @Override
                        public List<Object> business_opportunity_status_status_statistic_7faf0bdd_count(LambdaQueryWrapper queryWrapper) {
                                return businessOpportunityStaticMapper.business_opportunity_status_status_statistic_7faf0bdd_count(queryWrapper);
                                }


                        @Override
                        public List<Object> business_opportunity_status_status_statistic_5ba2a19d_count(LambdaQueryWrapper queryWrapper) {
                                return businessOpportunityStaticMapper.business_opportunity_status_status_statistic_5ba2a19d_count(queryWrapper);
                                }


                        @Override
                        public List<Object> business_opportunity_status_status_statistic_3d13a485_count(LambdaQueryWrapper queryWrapper) {
                                return businessOpportunityStaticMapper.business_opportunity_status_status_statistic_3d13a485_count(queryWrapper);
                                }


}






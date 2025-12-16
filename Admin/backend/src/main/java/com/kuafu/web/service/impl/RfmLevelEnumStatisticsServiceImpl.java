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

import com.kuafuweb.web.mapper.RfmLevelEnumStatisticsMapper;
import com.kuafuweb.web.entity.RfmLevelEnum;
import com.kuafuweb.web.service.IRfmLevelEnumStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> rfm_level_enum 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("RfmLevelEnumStatistics")
public class RfmLevelEnumStatisticsServiceImpl extends ServiceImpl<RfmLevelEnumStatisticsMapper, RfmLevelEnum> implements IRfmLevelEnumStatisticsService {
    @Resource
    private RfmLevelEnumStatisticsMapper rfmLevelEnumStaticMapper;

                        @Override
                        public List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count(LambdaQueryWrapper queryWrapper) {
                                return rfmLevelEnumStaticMapper.rfm_level_enum_rfm_level_enum_id_status_statistic_37bad8f0_count(queryWrapper);
                                }


                        @Override
                        public List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count(LambdaQueryWrapper queryWrapper) {
                                return rfmLevelEnumStaticMapper.rfm_level_enum_rfm_level_enum_id_status_statistic_23d416cb_count(queryWrapper);
                                }


                        @Override
                        public List<Object> rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count(LambdaQueryWrapper queryWrapper) {
                                return rfmLevelEnumStaticMapper.rfm_level_enum_rfm_level_enum_id_status_statistic_bcb4c0b6_count(queryWrapper);
                                }


}






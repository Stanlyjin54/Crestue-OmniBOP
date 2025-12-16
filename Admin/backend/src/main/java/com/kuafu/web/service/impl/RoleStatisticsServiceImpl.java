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

import com.kuafuweb.web.mapper.RoleStatisticsMapper;
import com.kuafuweb.web.entity.Role;
import com.kuafuweb.web.service.IRoleStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> role 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("RoleStatistics")
public class RoleStatisticsServiceImpl extends ServiceImpl<RoleStatisticsMapper, Role> implements IRoleStatisticsService {
    @Resource
    private RoleStatisticsMapper roleStaticMapper;

                        @Override
                        public List<Object> role_create_time_datetime_statistic_f1dec0e3_count(LambdaQueryWrapper queryWrapper) {
                                return roleStaticMapper.role_create_time_datetime_statistic_f1dec0e3_count(queryWrapper);
                                }


                        @Override
                        public List<Object> role_create_time_datetime_statistic_30529d9f_count(LambdaQueryWrapper queryWrapper) {
                                return roleStaticMapper.role_create_time_datetime_statistic_30529d9f_count(queryWrapper);
                                }


                        @Override
                        public List<Object> role_create_time_datetime_statistic_99f55dc9_count(LambdaQueryWrapper queryWrapper) {
                                return roleStaticMapper.role_create_time_datetime_statistic_99f55dc9_count(queryWrapper);
                                }


}






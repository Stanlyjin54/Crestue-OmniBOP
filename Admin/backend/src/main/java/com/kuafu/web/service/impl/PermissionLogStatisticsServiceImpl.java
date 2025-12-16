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

import com.kuafuweb.web.mapper.PermissionLogStatisticsMapper;
import com.kuafuweb.web.entity.PermissionLog;
import com.kuafuweb.web.service.IPermissionLogStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> permission_log 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("PermissionLogStatistics")
public class PermissionLogStatisticsServiceImpl extends ServiceImpl<PermissionLogStatisticsMapper, PermissionLog> implements IPermissionLogStatisticsService {
    @Resource
    private PermissionLogStatisticsMapper permissionLogStaticMapper;

                        @Override
                        public List<Object> permission_log_action_time_datetime_statistic_84170e10_count(LambdaQueryWrapper queryWrapper) {
                                return permissionLogStaticMapper.permission_log_action_time_datetime_statistic_84170e10_count(queryWrapper);
                                }


                        @Override
                        public List<Object> permission_log_action_time_datetime_statistic_975db700_count(LambdaQueryWrapper queryWrapper) {
                                return permissionLogStaticMapper.permission_log_action_time_datetime_statistic_975db700_count(queryWrapper);
                                }


                        @Override
                        public List<Object> permission_log_action_time_datetime_statistic_8cf06955_count(LambdaQueryWrapper queryWrapper) {
                                return permissionLogStaticMapper.permission_log_action_time_datetime_statistic_8cf06955_count(queryWrapper);
                                }


}






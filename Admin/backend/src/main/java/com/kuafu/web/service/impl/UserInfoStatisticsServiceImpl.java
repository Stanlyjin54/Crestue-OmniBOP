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

import com.kuafuweb.web.mapper.UserInfoStatisticsMapper;
import com.kuafuweb.web.entity.UserInfo;
import com.kuafuweb.web.service.IUserInfoStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> user_info 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("UserInfoStatistics")
public class UserInfoStatisticsServiceImpl extends ServiceImpl<UserInfoStatisticsMapper, UserInfo> implements IUserInfoStatisticsService {
    @Resource
    private UserInfoStatisticsMapper userInfoStaticMapper;

                        @Override
                        public List<Object> user_info_registration_date_datetime_statistic_2ec47ef3_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_registration_date_datetime_statistic_2ec47ef3_count(queryWrapper);
                                }


                        @Override
                        public List<Object> user_info_registration_date_datetime_statistic_2de39a8d_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_registration_date_datetime_statistic_2de39a8d_count(queryWrapper);
                                }


                        @Override
                        public List<Object> user_info_registration_date_datetime_statistic_2d54c52e_count(LambdaQueryWrapper queryWrapper) {
                                return userInfoStaticMapper.user_info_registration_date_datetime_statistic_2d54c52e_count(queryWrapper);
                                }


}






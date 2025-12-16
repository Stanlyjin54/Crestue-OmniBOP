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

import com.kuafuweb.web.mapper.CustomerInfoStatisticsMapper;
import com.kuafuweb.web.entity.CustomerInfo;
import com.kuafuweb.web.service.ICustomerInfoStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> customer_info 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("CustomerInfoStatistics")
public class CustomerInfoStatisticsServiceImpl extends ServiceImpl<CustomerInfoStatisticsMapper, CustomerInfo> implements ICustomerInfoStatisticsService {
    @Resource
    private CustomerInfoStatisticsMapper customerInfoStaticMapper;

                        @Override
                        public List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_ef82b098_count(LambdaQueryWrapper queryWrapper) {
                                return customerInfoStaticMapper.customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_ef82b098_count(queryWrapper);
                                }


                        @Override
                        public List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_77cd48e4_count(LambdaQueryWrapper queryWrapper) {
                                return customerInfoStaticMapper.customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_77cd48e4_count(queryWrapper);
                                }


                        @Override
                        public List<Object> customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_b209e896_count(LambdaQueryWrapper queryWrapper) {
                                return customerInfoStaticMapper.customer_info_rfm_level_enum_rfm_level_enum_id_1_status_statistic_b209e896_count(queryWrapper);
                                }


                        @Override
                        public List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_376f16a9_count(LambdaQueryWrapper queryWrapper) {
                                return customerInfoStaticMapper.customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_376f16a9_count(queryWrapper);
                                }


                        @Override
                        public List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_dc7b8cc0_count(LambdaQueryWrapper queryWrapper) {
                                return customerInfoStaticMapper.customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_dc7b8cc0_count(queryWrapper);
                                }


                        @Override
                        public List<Object> customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_c56bdc0c_count(LambdaQueryWrapper queryWrapper) {
                                return customerInfoStaticMapper.customer_info_customer_tag_enum_customer_tag_enum_id_1_status_statistic_c56bdc0c_count(queryWrapper);
                                }


}






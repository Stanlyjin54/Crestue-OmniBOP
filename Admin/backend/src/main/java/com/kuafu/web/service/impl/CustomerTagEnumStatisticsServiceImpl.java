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

import com.kuafuweb.web.mapper.CustomerTagEnumStatisticsMapper;
import com.kuafuweb.web.entity.CustomerTagEnum;
import com.kuafuweb.web.service.ICustomerTagEnumStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> customer_tag_enum 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("CustomerTagEnumStatistics")
public class CustomerTagEnumStatisticsServiceImpl extends ServiceImpl<CustomerTagEnumStatisticsMapper, CustomerTagEnum> implements ICustomerTagEnumStatisticsService {
    @Resource
    private CustomerTagEnumStatisticsMapper customerTagEnumStaticMapper;

                        @Override
                        public List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count(LambdaQueryWrapper queryWrapper) {
                                return customerTagEnumStaticMapper.customer_tag_enum_customer_tag_enum_id_status_statistic_9083f377_count(queryWrapper);
                                }


                        @Override
                        public List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count(LambdaQueryWrapper queryWrapper) {
                                return customerTagEnumStaticMapper.customer_tag_enum_customer_tag_enum_id_status_statistic_027cbb5f_count(queryWrapper);
                                }


                        @Override
                        public List<Object> customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count(LambdaQueryWrapper queryWrapper) {
                                return customerTagEnumStaticMapper.customer_tag_enum_customer_tag_enum_id_status_statistic_73a04ddf_count(queryWrapper);
                                }


}






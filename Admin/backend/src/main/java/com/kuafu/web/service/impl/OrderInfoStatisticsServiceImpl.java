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

import com.kuafuweb.web.mapper.OrderInfoStatisticsMapper;
import com.kuafuweb.web.entity.OrderInfo;
import com.kuafuweb.web.service.IOrderInfoStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> order_info 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("OrderInfoStatistics")
public class OrderInfoStatisticsServiceImpl extends ServiceImpl<OrderInfoStatisticsMapper, OrderInfo> implements IOrderInfoStatisticsService {
    @Resource
    private OrderInfoStatisticsMapper orderInfoStaticMapper;

                        @Override
                        public List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_ad7a7387_count(LambdaQueryWrapper queryWrapper) {
                                return orderInfoStaticMapper.order_info_order_status_enum_order_status_enum_id_1_status_statistic_ad7a7387_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_b94f62d5_count(LambdaQueryWrapper queryWrapper) {
                                return orderInfoStaticMapper.order_info_order_status_enum_order_status_enum_id_1_status_statistic_b94f62d5_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_info_order_status_enum_order_status_enum_id_1_status_statistic_b8881331_count(LambdaQueryWrapper queryWrapper) {
                                return orderInfoStaticMapper.order_info_order_status_enum_order_status_enum_id_1_status_statistic_b8881331_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_info_order_date_datetime_statistic_4a683c7d_count(LambdaQueryWrapper queryWrapper) {
                                return orderInfoStaticMapper.order_info_order_date_datetime_statistic_4a683c7d_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_info_order_date_datetime_statistic_d9005f45_count(LambdaQueryWrapper queryWrapper) {
                                return orderInfoStaticMapper.order_info_order_date_datetime_statistic_d9005f45_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_info_order_date_datetime_statistic_8d28825b_count(LambdaQueryWrapper queryWrapper) {
                                return orderInfoStaticMapper.order_info_order_date_datetime_statistic_8d28825b_count(queryWrapper);
                                }


}






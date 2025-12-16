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

import com.kuafuweb.web.mapper.OrderStatusEnumStatisticsMapper;
import com.kuafuweb.web.entity.OrderStatusEnum;
import com.kuafuweb.web.service.IOrderStatusEnumStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> order_status_enum 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("OrderStatusEnumStatistics")
public class OrderStatusEnumStatisticsServiceImpl extends ServiceImpl<OrderStatusEnumStatisticsMapper, OrderStatusEnum> implements IOrderStatusEnumStatisticsService {
    @Resource
    private OrderStatusEnumStatisticsMapper orderStatusEnumStaticMapper;

                        @Override
                        public List<Object> order_status_enum_order_status_enum_id_status_statistic_ed6cd955_count(LambdaQueryWrapper queryWrapper) {
                                return orderStatusEnumStaticMapper.order_status_enum_order_status_enum_id_status_statistic_ed6cd955_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_status_enum_order_status_enum_id_status_statistic_dcd3b45b_count(LambdaQueryWrapper queryWrapper) {
                                return orderStatusEnumStaticMapper.order_status_enum_order_status_enum_id_status_statistic_dcd3b45b_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_status_enum_order_status_enum_id_status_statistic_d8e4b58d_count(LambdaQueryWrapper queryWrapper) {
                                return orderStatusEnumStaticMapper.order_status_enum_order_status_enum_id_status_statistic_d8e4b58d_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_status_enum_status_name_status_statistic_cd290941_count(LambdaQueryWrapper queryWrapper) {
                                return orderStatusEnumStaticMapper.order_status_enum_status_name_status_statistic_cd290941_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_status_enum_status_name_status_statistic_962848ae_count(LambdaQueryWrapper queryWrapper) {
                                return orderStatusEnumStaticMapper.order_status_enum_status_name_status_statistic_962848ae_count(queryWrapper);
                                }


                        @Override
                        public List<Object> order_status_enum_status_name_status_statistic_dcb4f308_count(LambdaQueryWrapper queryWrapper) {
                                return orderStatusEnumStaticMapper.order_status_enum_status_name_status_statistic_dcb4f308_count(queryWrapper);
                                }


}






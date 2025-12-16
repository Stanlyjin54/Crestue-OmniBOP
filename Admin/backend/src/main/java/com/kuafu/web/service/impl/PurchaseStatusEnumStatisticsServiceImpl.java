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

import com.kuafuweb.web.mapper.PurchaseStatusEnumStatisticsMapper;
import com.kuafuweb.web.entity.PurchaseStatusEnum;
import com.kuafuweb.web.service.IPurchaseStatusEnumStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> purchase_status_enum 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("PurchaseStatusEnumStatistics")
public class PurchaseStatusEnumStatisticsServiceImpl extends ServiceImpl<PurchaseStatusEnumStatisticsMapper, PurchaseStatusEnum> implements IPurchaseStatusEnumStatisticsService {
    @Resource
    private PurchaseStatusEnumStatisticsMapper purchaseStatusEnumStaticMapper;

                        @Override
                        public List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_cac5b56b_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseStatusEnumStaticMapper.purchase_status_enum_purchase_status_enum_id_status_statistic_cac5b56b_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_4879421d_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseStatusEnumStaticMapper.purchase_status_enum_purchase_status_enum_id_status_statistic_4879421d_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_status_enum_purchase_status_enum_id_status_statistic_d51abb7f_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseStatusEnumStaticMapper.purchase_status_enum_purchase_status_enum_id_status_statistic_d51abb7f_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_status_enum_status_name_status_statistic_ae1dfb7a_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseStatusEnumStaticMapper.purchase_status_enum_status_name_status_statistic_ae1dfb7a_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_status_enum_status_name_status_statistic_b11589b5_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseStatusEnumStaticMapper.purchase_status_enum_status_name_status_statistic_b11589b5_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_status_enum_status_name_status_statistic_4fce27d5_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseStatusEnumStaticMapper.purchase_status_enum_status_name_status_statistic_4fce27d5_count(queryWrapper);
                                }


}






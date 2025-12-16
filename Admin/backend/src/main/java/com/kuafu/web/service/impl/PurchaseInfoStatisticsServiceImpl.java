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

import com.kuafuweb.web.mapper.PurchaseInfoStatisticsMapper;
import com.kuafuweb.web.entity.PurchaseInfo;
import com.kuafuweb.web.service.IPurchaseInfoStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> purchase_info 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("PurchaseInfoStatistics")
public class PurchaseInfoStatisticsServiceImpl extends ServiceImpl<PurchaseInfoStatisticsMapper, PurchaseInfo> implements IPurchaseInfoStatisticsService {
    @Resource
    private PurchaseInfoStatisticsMapper purchaseInfoStaticMapper;

                        @Override
                        public List<Object> purchase_info_purchase_date_datetime_statistic_2bd78ba1_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseInfoStaticMapper.purchase_info_purchase_date_datetime_statistic_2bd78ba1_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_info_purchase_date_datetime_statistic_11ee5598_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseInfoStaticMapper.purchase_info_purchase_date_datetime_statistic_11ee5598_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_info_purchase_date_datetime_statistic_8e593cc4_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseInfoStaticMapper.purchase_info_purchase_date_datetime_statistic_8e593cc4_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_c751e7be_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseInfoStaticMapper.purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_c751e7be_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_4d195a98_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseInfoStaticMapper.purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_4d195a98_count(queryWrapper);
                                }


                        @Override
                        public List<Object> purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_f7e55886_count(LambdaQueryWrapper queryWrapper) {
                                return purchaseInfoStaticMapper.purchase_info_purchase_status_enum_purchase_status_enum_id_1_status_statistic_f7e55886_count(queryWrapper);
                                }


}






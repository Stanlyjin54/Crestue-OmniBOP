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

import com.kuafuweb.web.mapper.InventoryRecordStatisticsMapper;
import com.kuafuweb.web.entity.InventoryRecord;
import com.kuafuweb.web.service.IInventoryRecordStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> inventory_record 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("InventoryRecordStatistics")
public class InventoryRecordStatisticsServiceImpl extends ServiceImpl<InventoryRecordStatisticsMapper, InventoryRecord> implements IInventoryRecordStatisticsService {
    @Resource
    private InventoryRecordStatisticsMapper inventoryRecordStaticMapper;

                        @Override
                        public List<Object> inventory_record_expire_date_datetime_statistic_a38e9712_count(LambdaQueryWrapper queryWrapper) {
                                return inventoryRecordStaticMapper.inventory_record_expire_date_datetime_statistic_a38e9712_count(queryWrapper);
                                }


                        @Override
                        public List<Object> inventory_record_expire_date_datetime_statistic_00541631_count(LambdaQueryWrapper queryWrapper) {
                                return inventoryRecordStaticMapper.inventory_record_expire_date_datetime_statistic_00541631_count(queryWrapper);
                                }


                        @Override
                        public List<Object> inventory_record_expire_date_datetime_statistic_5296a1bd_count(LambdaQueryWrapper queryWrapper) {
                                return inventoryRecordStaticMapper.inventory_record_expire_date_datetime_statistic_5296a1bd_count(queryWrapper);
                                }


}






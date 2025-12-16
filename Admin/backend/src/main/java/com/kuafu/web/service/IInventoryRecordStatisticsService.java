package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.InventoryRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  inventory_record 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IInventoryRecordStatisticsService extends IService<InventoryRecord> {
        List<Object> inventory_record_expire_date_datetime_statistic_a38e9712_count(LambdaQueryWrapper queryWrapper);

        List<Object> inventory_record_expire_date_datetime_statistic_00541631_count(LambdaQueryWrapper queryWrapper);

        List<Object> inventory_record_expire_date_datetime_statistic_5296a1bd_count(LambdaQueryWrapper queryWrapper);

}






package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.ProcurementSuggestion;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  procurement_suggestion 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IProcurementSuggestionStatisticsService extends IService<ProcurementSuggestion> {
        List<Object> procurement_suggestion_suggest_date_datetime_statistic_ec8b49be_count(LambdaQueryWrapper queryWrapper);

        List<Object> procurement_suggestion_suggest_date_datetime_statistic_26e934d3_count(LambdaQueryWrapper queryWrapper);

        List<Object> procurement_suggestion_suggest_date_datetime_statistic_00313900_count(LambdaQueryWrapper queryWrapper);

}






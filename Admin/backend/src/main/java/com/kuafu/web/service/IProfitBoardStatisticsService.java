package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.ProfitBoard;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  profit_board 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IProfitBoardStatisticsService extends IService<ProfitBoard> {
        List<Object> profit_board_stat_date_datetime_statistic_17470edd_count(LambdaQueryWrapper queryWrapper);

        List<Object> profit_board_stat_date_datetime_statistic_edf98856_count(LambdaQueryWrapper queryWrapper);

        List<Object> profit_board_stat_date_datetime_statistic_f80cc477_count(LambdaQueryWrapper queryWrapper);

}






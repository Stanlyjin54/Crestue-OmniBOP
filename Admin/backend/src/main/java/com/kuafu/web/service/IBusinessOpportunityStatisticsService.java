package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.BusinessOpportunity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  business_opportunity 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IBusinessOpportunityStatisticsService extends IService<BusinessOpportunity> {
        List<Object> business_opportunity_status_status_statistic_7faf0bdd_count(LambdaQueryWrapper queryWrapper);

        List<Object> business_opportunity_status_status_statistic_5ba2a19d_count(LambdaQueryWrapper queryWrapper);

        List<Object> business_opportunity_status_status_statistic_3d13a485_count(LambdaQueryWrapper queryWrapper);

}






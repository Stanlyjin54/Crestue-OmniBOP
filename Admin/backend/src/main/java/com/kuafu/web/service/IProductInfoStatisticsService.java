package com.kuafuweb.web.service;

import java.util.List;
import com.kuafuweb.web.entity.ProductInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p>  product_info 服务类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
public interface IProductInfoStatisticsService extends IService<ProductInfo> {
        List<Object> product_info_shelf_life_datetime_statistic_13b09a91_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_info_shelf_life_datetime_statistic_fabe9b9f_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_info_shelf_life_datetime_statistic_b986a1ea_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count(LambdaQueryWrapper queryWrapper);

        List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count(LambdaQueryWrapper queryWrapper);

}






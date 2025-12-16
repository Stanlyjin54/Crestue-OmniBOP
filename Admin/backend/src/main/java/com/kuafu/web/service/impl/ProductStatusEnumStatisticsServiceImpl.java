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

import com.kuafuweb.web.mapper.ProductStatusEnumStatisticsMapper;
import com.kuafuweb.web.entity.ProductStatusEnum;
import com.kuafuweb.web.service.IProductStatusEnumStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> product_status_enum 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("ProductStatusEnumStatistics")
public class ProductStatusEnumStatisticsServiceImpl extends ServiceImpl<ProductStatusEnumStatisticsMapper, ProductStatusEnum> implements IProductStatusEnumStatisticsService {
    @Resource
    private ProductStatusEnumStatisticsMapper productStatusEnumStaticMapper;

                        @Override
                        public List<Object> product_status_enum_product_status_enum_id_status_statistic_d77ca5ce_count(LambdaQueryWrapper queryWrapper) {
                                return productStatusEnumStaticMapper.product_status_enum_product_status_enum_id_status_statistic_d77ca5ce_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_status_enum_product_status_enum_id_status_statistic_e288bf0e_count(LambdaQueryWrapper queryWrapper) {
                                return productStatusEnumStaticMapper.product_status_enum_product_status_enum_id_status_statistic_e288bf0e_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_status_enum_product_status_enum_id_status_statistic_3e0cb3c6_count(LambdaQueryWrapper queryWrapper) {
                                return productStatusEnumStaticMapper.product_status_enum_product_status_enum_id_status_statistic_3e0cb3c6_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_status_enum_status_name_status_statistic_fe3afe34_count(LambdaQueryWrapper queryWrapper) {
                                return productStatusEnumStaticMapper.product_status_enum_status_name_status_statistic_fe3afe34_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_status_enum_status_name_status_statistic_1a658de2_count(LambdaQueryWrapper queryWrapper) {
                                return productStatusEnumStaticMapper.product_status_enum_status_name_status_statistic_1a658de2_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_status_enum_status_name_status_statistic_8130dea7_count(LambdaQueryWrapper queryWrapper) {
                                return productStatusEnumStaticMapper.product_status_enum_status_name_status_statistic_8130dea7_count(queryWrapper);
                                }


}






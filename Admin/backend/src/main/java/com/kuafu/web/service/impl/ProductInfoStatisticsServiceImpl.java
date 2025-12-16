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

import com.kuafuweb.web.mapper.ProductInfoStatisticsMapper;
import com.kuafuweb.web.entity.ProductInfo;
import com.kuafuweb.web.service.IProductInfoStatisticsService;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/**
 * <p> product_info 服务实现类 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Slf4j
@Service("ProductInfoStatistics")
public class ProductInfoStatisticsServiceImpl extends ServiceImpl<ProductInfoStatisticsMapper, ProductInfo> implements IProductInfoStatisticsService {
    @Resource
    private ProductInfoStatisticsMapper productInfoStaticMapper;

                        @Override
                        public List<Object> product_info_shelf_life_datetime_statistic_13b09a91_count(LambdaQueryWrapper queryWrapper) {
                                return productInfoStaticMapper.product_info_shelf_life_datetime_statistic_13b09a91_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_info_shelf_life_datetime_statistic_fabe9b9f_count(LambdaQueryWrapper queryWrapper) {
                                return productInfoStaticMapper.product_info_shelf_life_datetime_statistic_fabe9b9f_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_info_shelf_life_datetime_statistic_b986a1ea_count(LambdaQueryWrapper queryWrapper) {
                                return productInfoStaticMapper.product_info_shelf_life_datetime_statistic_b986a1ea_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count(LambdaQueryWrapper queryWrapper) {
                                return productInfoStaticMapper.product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count(LambdaQueryWrapper queryWrapper) {
                                return productInfoStaticMapper.product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count(queryWrapper);
                                }


                        @Override
                        public List<Object> product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count(LambdaQueryWrapper queryWrapper) {
                                return productInfoStaticMapper.product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count(queryWrapper);
                                }


}






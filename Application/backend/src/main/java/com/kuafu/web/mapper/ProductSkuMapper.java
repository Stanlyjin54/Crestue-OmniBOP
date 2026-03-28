package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品SKU数据访问层
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
}

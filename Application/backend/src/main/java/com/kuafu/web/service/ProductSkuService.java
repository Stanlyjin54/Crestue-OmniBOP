package com.kuafu.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuafu.web.entity.ProductSku;

import java.util.List;
import java.util.Map;

/**
 * 商品SKU服务接口
 */
public interface ProductSkuService extends IService<ProductSku> {

    /**
     * 获取商品的SKU列表
     */
    List<ProductSku> getSkusByProductId(Integer productId);

    /**
     * 解析SKU规格属性
     */
    List<Map<String, Object>> parseSpecs(List<ProductSku> skus);

    /**
     * 检查库存
     */
    boolean checkStock(Integer skuId, Integer quantity);

    /**
     * 扣减库存
     */
    boolean deductStock(Integer skuId, Integer quantity);
}

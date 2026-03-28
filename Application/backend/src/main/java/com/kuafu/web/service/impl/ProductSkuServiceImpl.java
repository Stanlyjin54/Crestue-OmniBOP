package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuafu.web.entity.ProductSku;
import com.kuafu.web.mapper.ProductSkuMapper;
import com.kuafu.web.service.ProductSkuService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

/**
 * 商品SKU服务实现
 */
@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements ProductSkuService {

    private final Gson gson = new Gson();
    private final Type specsType = new TypeToken<Map<String, String>>(){}.getType();

    @Override
    public List<ProductSku> getSkusByProductId(Integer productId) {
        LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSku::getProductInfoId, productId)
               .orderByAsc(ProductSku::getSkuId);
        return this.list(wrapper);
    }

    @Override
    public List<Map<String, Object>> parseSpecs(List<ProductSku> skus) {
        // 收集所有规格属性
        Map<String, Set<String>> specMap = new LinkedHashMap<>();
        
        for (ProductSku sku : skus) {
            if (sku.getSpecs() != null && !sku.getSpecs().isEmpty()) {
                try {
                    Map<String, String> specs = gson.fromJson(sku.getSpecs(), specsType);
                    specs.forEach((key, value) -> {
                        specMap.computeIfAbsent(key, k -> new LinkedHashSet<>()).add(value);
                    });
                } catch (Exception e) {
                    // 解析失败，跳过
                }
            }
        }

        // 转换为前端需要的格式
        List<Map<String, Object>> result = new ArrayList<>();
        int id = 1;
        for (Map.Entry<String, Set<String>> entry : specMap.entrySet()) {
            Map<String, Object> spec = new HashMap<>();
            spec.put("id", id++);
            spec.put("name", entry.getKey());
            spec.put("values", new ArrayList<>(entry.getValue()));
            result.add(spec);
        }
        
        return result;
    }

    @Override
    public boolean checkStock(Integer skuId, Integer quantity) {
        ProductSku sku = this.getById(skuId);
        if (sku == null || sku.getStock() == null) {
            return false;
        }
        return sku.getStock() >= quantity;
    }

    @Override
    public boolean deductStock(Integer skuId, Integer quantity) {
        ProductSku sku = this.getById(skuId);
        if (sku == null || sku.getStock() == null || sku.getStock() < quantity) {
            return false;
        }
        sku.setStock(sku.getStock() - quantity);
        return this.updateById(sku);
    }
}

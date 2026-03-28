package com.kuafu.web.controller;

import com.kuafu.common.domin.ResultUtils;
import com.kuafu.web.entity.ProductSku;
import com.kuafu.web.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品SKU管理接口
 */
@RestController
@RequestMapping("/product")
@Api(tags = "商品SKU管理")
@RequiredArgsConstructor
public class ProductSkuController {

    private final ProductSkuService productSkuService;

    /**
     * 获取商品的SKU列表
     */
    @GetMapping("/{productId}/skus")
    @ApiOperation("获取商品SKU列表")
    public Object getProductSkus(@PathVariable Integer productId) {
        List<ProductSku> skus = productSkuService.getSkusByProductId(productId);
        
        // 解析规格属性
        List<Map<String, Object>> specsList = productSkuService.parseSpecs(skus);
        
        return ResultUtils.success(Map.of(
            "skus", skus,
            "specs", specsList
        ));
    }

    /**
     * 获取SKU详情
     */
    @GetMapping("/sku/{skuId}")
    @ApiOperation("获取SKU详情")
    public Object getSkuDetail(@PathVariable Integer skuId) {
        ProductSku sku = productSkuService.getById(skuId);
        if (sku == null) {
            return ResultUtils.error("SKU不存在");
        }
        return ResultUtils.success(sku);
    }

    /**
     * 检查SKU库存
     */
    @GetMapping("/sku/{skuId}/stock")
    @ApiOperation("检查SKU库存")
    public Object checkStock(@PathVariable Integer skuId, @RequestParam Integer quantity) {
        boolean available = productSkuService.checkStock(skuId, quantity);
        return ResultUtils.success(Map.of(
            "available", available,
            "skuId", skuId,
            "requested", quantity
        ));
    }
}

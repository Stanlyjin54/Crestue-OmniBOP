package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>商品SKU</p>
 *
 * @author kuafuai
 * @description 商品规格SKU管理
 * @date 2025/06/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("product_sku")
public class ProductSku {

    @TableId(value = "sku_id", type = IdType.AUTO)
    @JsonProperty("sku_id")
    private Integer skuId;

    @JsonProperty("product_info_id")
    @TableField("product_info_id")
    private Integer productInfoId;

    @JsonProperty("sku_code")
    @TableField("sku_code")
    private String skuCode;

    @JsonProperty("sku_name")
    @TableField("sku_name")
    private String skuName;

    @JsonProperty("price")
    @TableField("price")
    private BigDecimal price;

    @JsonProperty("stock")
    @TableField("stock")
    private Integer stock;

    @JsonProperty("specs")
    @TableField("specs")
    private String specs;

    @JsonProperty("create_time")
    @TableField("create_time")
    private Date createTime;

    @JsonProperty("update_time")
    @TableField("update_time")
    private Date updateTime;
}

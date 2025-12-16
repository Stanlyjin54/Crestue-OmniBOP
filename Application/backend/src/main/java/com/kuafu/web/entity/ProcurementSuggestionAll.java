package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * <p>  采购建议 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcurementSuggestionAll {
    @TableField(value="ps.reason")
    @JsonProperty("reason")
    private String reason;
    @TableField(value="pi.product_status_enum_product_status_enum_id_1")
    @JsonProperty("product_status_enum_product_status_enum_id_1")
    private Integer productStatusEnumProductStatusEnumId1;
    @TableField(value="pi.specification")
    @JsonProperty("specification")
    private String specification;
    @TableField(value="pi.serial_number")
    @JsonProperty("serial_number")
    private String serialNumber;
    @TableField(value="ps.product_info_product_info_id_1")
    @JsonProperty("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @TableField(value="pi.product_name")
    @JsonProperty("product_name")
    private String productName;
    @TableField(value="ps.suggest_date")
    @JsonProperty("suggest_date")
    private Date suggestDate;
    @TableField(value="ps.suggest_quantity")
    @JsonProperty("suggest_quantity")
    private Integer suggestQuantity;
    @TableField(value="pi.shelf_life")
    @JsonProperty("shelf_life")
    private Date shelfLife;
    @TableField(value="pi.unit")
    @JsonProperty("unit")
    private String unit;
    @TableField(value="pi.min_stock")
    @JsonProperty("min_stock")
    private Integer minStock;
    @TableField(value="pi.category")
    @JsonProperty("category")
    private String category;
    @TableField(value="ps.procurement_suggestion_id")
    @JsonProperty("procurement_suggestion_id")
    private Integer procurementSuggestionId;
    @TableField(value="pi.barcode")
    @JsonProperty("barcode")
    private String barcode;
    @TableField(value="pi.brand")
    @JsonProperty("brand")
    private String brand;









}

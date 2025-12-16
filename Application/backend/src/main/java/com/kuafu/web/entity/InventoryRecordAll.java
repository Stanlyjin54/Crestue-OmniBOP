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
 * <p>  库存记录 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRecordAll {
    @TableField(value="ir.quantity")
    @JsonProperty("quantity")
    private Integer quantity;
    @TableField(value="ir.batch_number")
    @JsonProperty("batch_number")
    private String batchNumber;
    @TableField(value="pi.product_status_enum_product_status_enum_id_1")
    @JsonProperty("product_status_enum_product_status_enum_id_1")
    private Integer productStatusEnumProductStatusEnumId1;
    @TableField(value="ir.inventory_record_id")
    @JsonProperty("inventory_record_id")
    private Integer inventoryRecordId;
    @TableField(value="pi.specification")
    @JsonProperty("specification")
    private String specification;
    @TableField(value="ir.serial_number")
    @JsonProperty("serial_number")
    private String serialNumber;
    @TableField(value="ir.product_info_product_info_id_1")
    @JsonProperty("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @TableField(value="ir.warehouse_info_warehouse_info_id_1")
    @JsonProperty("warehouse_info_warehouse_info_id_1")
    private Integer warehouseInfoWarehouseInfoId1;
    @TableField(value="pi.product_name")
    @JsonProperty("product_name")
    private String productName;
    @TableField(value="pi.shelf_life")
    @JsonProperty("shelf_life")
    private Date shelfLife;
    @TableField(value="pi.unit")
    @JsonProperty("unit")
    private String unit;
    @TableField(value="ir.expire_date")
    @JsonProperty("expire_date")
    private Date expireDate;
    @TableField(value="wi.warehouse_name")
    @JsonProperty("warehouse_name")
    private String warehouseName;
    @TableField(value="pi.min_stock")
    @JsonProperty("min_stock")
    private Integer minStock;
    @TableField(value="wi.location")
    @JsonProperty("location")
    private String location;
    @TableField(value="pi.category")
    @JsonProperty("category")
    private String category;
    @TableField(value="pi.barcode")
    @JsonProperty("barcode")
    private String barcode;
    @TableField(value="pi.brand")
    @JsonProperty("brand")
    private String brand;









}

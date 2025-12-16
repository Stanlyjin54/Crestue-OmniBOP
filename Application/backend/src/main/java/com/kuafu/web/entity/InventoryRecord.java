package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
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
@TableName("inventory_record")
public class InventoryRecord  {
    @TableId(value = "inventory_record_id", type = IdType.AUTO)
    @JsonProperty("inventory_record_id")
    
    
    
    private Integer inventoryRecordId;
    @JsonProperty("product_info_product_info_id_1")
    @IsNotNullField(description = "商品ID")
    
    
    @TableField("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("warehouse_info_warehouse_info_id_1")
    @IsNotNullField(description = "仓库ID")
    
    
    @TableField("warehouse_info_warehouse_info_id_1")
    private Integer warehouseInfoWarehouseInfoId1;
    @JsonProperty("quantity")
    @IsNotNullField(description = "库存数量")
    
    
    @TableField("quantity")
    private Integer quantity;
    @JsonProperty("batch_number")
    
    
    @TableField("batch_number")
    private String batchNumber;
    @JsonProperty("expire_date")
    
    
    @TableField("expire_date")
    private Date expireDate;
    @JsonProperty("serial_number")
    
    
    @TableField("serial_number")
    private String serialNumber;





}

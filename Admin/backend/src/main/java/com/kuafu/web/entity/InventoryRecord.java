package com.kuafuweb.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafuweb.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;
import com.kuafuweb.common.annotation.Excel.*;



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
public class InventoryRecord    {
    @TableId(value = "inventory_record_id", type = IdType.AUTO)
    @JsonProperty(value = "inventoryRecordId")
    @Excel(name = "主键")

    private Integer inventoryRecordId;
    @JsonProperty(value = "productInfoProductInfoId1")
    @Excel(name = "商品ID")
    @ExcelProperty(value = "商品ID")
    @TableField(value = "product_info_product_info_id_1")

    private Integer productInfoProductInfoId1;
    @JsonProperty(value = "warehouseInfoWarehouseInfoId1")
    @Excel(name = "仓库ID")
    @ExcelProperty(value = "仓库ID")
    @TableField(value = "warehouse_info_warehouse_info_id_1")

    private Integer warehouseInfoWarehouseInfoId1;
    @JsonProperty(value = "quantity")
    @Excel(name = "库存数量")
    @ExcelProperty(value = "库存数量")
    @TableField(value = "quantity")

    private Integer quantity;
    @JsonProperty(value = "batchNumber")
    @Excel(name = "批次号")
    @ExcelProperty(value = "批次号")
    @TableField(value = "batch_number")

    private String batchNumber;
    @JsonProperty(value = "expireDate")
    @Excel(name = "效期")
    @ExcelProperty(value = "效期")
    @TableField(value = "expire_date")

    private Date expireDate;
    @JsonProperty(value = "serialNumber")
    @Excel(name = "序列号")
    @ExcelProperty(value = "序列号")
    @TableField(value = "serial_number")

    private String serialNumber;


}






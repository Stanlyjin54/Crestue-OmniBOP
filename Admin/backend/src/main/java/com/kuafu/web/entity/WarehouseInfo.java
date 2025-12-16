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
 * <p>  仓库信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("warehouse_info")
public class WarehouseInfo    {
    @TableId(value = "warehouse_info_id", type = IdType.AUTO)
    @JsonProperty(value = "warehouseInfoId")
    @Excel(name = "主键")

    private Integer warehouseInfoId;
    @JsonProperty(value = "warehouseName")
    @Excel(name = "仓库名称")
    @ExcelProperty(value = "仓库名称")
    @TableField(value = "warehouse_name")

    private String warehouseName;
    @JsonProperty(value = "location")
    @Excel(name = "仓库位置")
    @ExcelProperty(value = "仓库位置")
    @TableField(value = "location")

    private String location;


}






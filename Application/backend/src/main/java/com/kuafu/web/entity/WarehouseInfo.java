package com.kuafuweb.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafuweb.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafuweb.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
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
public class WarehouseInfo  {
    @TableId(value = "warehouse_info_id", type = IdType.AUTO)
    @JsonProperty("warehouse_info_id")
    
    
    
    private Integer warehouseInfoId;
    @JsonProperty("warehouse_name")
    @IsNotNullField(description = "仓库名称")
    
    
    @TableField("warehouse_name")
    private String warehouseName;
    @JsonProperty("location")
    
    
    @TableField("location")
    private String location;





}

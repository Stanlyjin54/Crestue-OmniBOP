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
 * <p>  商品信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("product_info")
public class ProductInfo  {
    @TableId(value = "product_info_id", type = IdType.AUTO)
    @JsonProperty("product_info_id")
    
    
    
    private Integer productInfoId;
    @JsonProperty("product_name")
    @IsNotNullField(description = "商品名称")
    
    
    @TableField("product_name")
    private String productName;
    @JsonProperty("barcode")
    
    
    @TableField("barcode")
    private String barcode;
    @JsonProperty("category")
    
    
    @TableField("category")
    private String category;
    @JsonProperty("brand")
    
    
    @TableField("brand")
    private String brand;
    @JsonProperty("specification")
    
    
    @TableField("specification")
    private String specification;
    @JsonProperty("unit")
    
    
    @TableField("unit")
    private String unit;
    @JsonProperty("serial_number")
    
    
    @TableField("serial_number")
    private String serialNumber;
    @JsonProperty("shelf_life")
    
    
    @TableField("shelf_life")
    private Date shelfLife;
    @JsonProperty("min_stock")
    
    
    @TableField("min_stock")
    private Integer minStock;
    @JsonProperty("product_status_enum_product_status_enum_id_1")
    @IsNotNullField(description = "商品状态")
    
    
    @TableField("product_status_enum_product_status_enum_id_1")
    private Integer productStatusEnumProductStatusEnumId1;



@TableField(exist = false)
@JsonProperty("images")
private List<StaticResource> images;



}

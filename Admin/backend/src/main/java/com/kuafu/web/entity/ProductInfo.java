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
public class ProductInfo    {
    @TableId(value = "product_info_id", type = IdType.AUTO)
    @JsonProperty(value = "productInfoId")
    @Excel(name = "主键")

    private Integer productInfoId;
    @JsonProperty(value = "productName")
    @Excel(name = "商品名称")
    @ExcelProperty(value = "商品名称")
    @TableField(value = "product_name")

    private String productName;
    @JsonProperty(value = "barcode")
    @Excel(name = "条形码")
    @ExcelProperty(value = "条形码")
    @TableField(value = "barcode")

    private String barcode;
    @JsonProperty(value = "category")
    @Excel(name = "商品分类")
    @ExcelProperty(value = "商品分类")
    @TableField(value = "category")

    private String category;
    @JsonProperty(value = "brand")
    @Excel(name = "品牌")
    @ExcelProperty(value = "品牌")
    @TableField(value = "brand")

    private String brand;
    @JsonProperty(value = "specification")
    @Excel(name = "规格")
    @ExcelProperty(value = "规格")
    @TableField(value = "specification")

    private String specification;
    @JsonProperty(value = "unit")
    @Excel(name = "单位")
    @ExcelProperty(value = "单位")
    @TableField(value = "unit")

    private String unit;
    @JsonProperty(value = "imagesResourceKey")
    @Excel(cellType = ColumnType.IMAGE, name = "商品图片")
    @ExcelProperty(value = "商品图片")
    @TableField(value = "images_resource_key")

    private String imagesResourceKey;
    @JsonProperty(value = "serialNumber")
    @Excel(name = "序列号")
    @ExcelProperty(value = "序列号")
    @TableField(value = "serial_number")

    private String serialNumber;
    @JsonProperty(value = "shelfLife")
    @Excel(name = "保质期")
    @ExcelProperty(value = "保质期")
    @TableField(value = "shelf_life")

    private Date shelfLife;
    @JsonProperty(value = "minStock")
    @Excel(name = "安全库存")
    @ExcelProperty(value = "安全库存")
    @TableField(value = "min_stock")

    private Integer minStock;
    @JsonProperty(value = "productStatusEnumProductStatusEnumId1")
    @Excel(name = "商品状态")
    @ExcelProperty(value = "商品状态")
    @TableField(value = "product_status_enum_product_status_enum_id_1")

    private Integer productStatusEnumProductStatusEnumId1;

    @TableField(exist = false)
    private List<StaticResource> images;

}






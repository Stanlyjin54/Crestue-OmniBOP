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
 * <p>  订单商品明细 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_item")
public class OrderItem    {
    @TableId(value = "order_item_id", type = IdType.AUTO)
    @JsonProperty(value = "orderItemId")
    @Excel(name = "主键")

    private Integer orderItemId;
    @JsonProperty(value = "orderInfoOrderInfoId1")
    @Excel(name = "订单ID")
    @ExcelProperty(value = "订单ID")
    @TableField(value = "order_info_order_info_id_1")

    private Integer orderInfoOrderInfoId1;
    @JsonProperty(value = "productInfoProductInfoId1")
    @Excel(name = "商品ID")
    @ExcelProperty(value = "商品ID")
    @TableField(value = "product_info_product_info_id_1")

    private Integer productInfoProductInfoId1;
    @JsonProperty(value = "quantity")
    @Excel(name = "数量")
    @ExcelProperty(value = "数量")
    @TableField(value = "quantity")

    private Integer quantity;
    @JsonProperty(value = "unitPrice")
    @Excel(name = "单价")
    @ExcelProperty(value = "单价")
    @TableField(value = "unit_price")

    private Double unitPrice;
    @JsonProperty(value = "totalPrice")
    @Excel(name = "总价")
    @ExcelProperty(value = "总价")
    @TableField(value = "total_price")

    private Double totalPrice;


}






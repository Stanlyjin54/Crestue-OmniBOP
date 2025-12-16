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
public class OrderItem  {
    @TableId(value = "order_item_id", type = IdType.AUTO)
    @JsonProperty("order_item_id")
    
    
    
    private Integer orderItemId;
    @JsonProperty("order_info_order_info_id_1")
    @IsNotNullField(description = "订单ID")
    
    
    @TableField("order_info_order_info_id_1")
    private Integer orderInfoOrderInfoId1;
    @JsonProperty("product_info_product_info_id_1")
    @IsNotNullField(description = "商品ID")
    
    
    @TableField("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("quantity")
    @IsNotNullField(description = "数量")
    
    
    @TableField("quantity")
    private Integer quantity;
    @JsonProperty("unit_price")
    @IsNotNullField(description = "单价")
    
    
    @TableField("unit_price")
    private Double unitPrice;
    @JsonProperty("total_price")
    @IsNotNullField(description = "总价")
    
    
    @TableField("total_price")
    private Double totalPrice;





}

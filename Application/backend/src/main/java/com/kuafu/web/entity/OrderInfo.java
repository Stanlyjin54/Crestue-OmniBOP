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
 * <p>  订单信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_info")
public class OrderInfo  {
    @TableId(value = "order_info_id", type = IdType.AUTO)
    @JsonProperty("order_info_id")
    
    
    
    private Integer orderInfoId;
    @JsonProperty("order_no")
    @IsNotNullField(description = "订单编号")
    
    
    @TableField("order_no")
    private String orderNo;
    @JsonProperty("customer_info_customer_info_id_1")
    @IsNotNullField(description = "客户ID")
    
    
    @TableField("customer_info_customer_info_id_1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty("user_info_user_info_id_1")
    @IsNotNullField(description = "下单用户ID")
    
    
    @TableField("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("order_status_enum_order_status_enum_id_1")
    @IsNotNullField(description = "订单状态")
    
    
    @TableField("order_status_enum_order_status_enum_id_1")
    private Integer orderStatusEnumOrderStatusEnumId1;
    @JsonProperty("total_amount")
    @IsNotNullField(description = "订单总金额")
    
    
    @TableField("total_amount")
    private Double totalAmount;
    @JsonProperty("profit_amount")
    
    
    @TableField("profit_amount")
    private Double profitAmount;
    @JsonProperty("commission")
    
    
    @TableField("commission")
    private Double commission;
    @JsonProperty("shipping_fee")
    
    
    @TableField("shipping_fee")
    private Double shippingFee;
    @JsonProperty("order_date")
    @IsNotNullField(description = "下单时间")
    
    
    @TableField("order_date")
    private Date orderDate;
    @JsonProperty("general_orders")
    
    
    @TableField("general_orders")
    private String generalOrders;





}

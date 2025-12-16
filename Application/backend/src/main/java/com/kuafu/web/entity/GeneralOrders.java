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
 * <p>  系统订单信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_orders")
public class GeneralOrders  {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonProperty("id")
    
    
    
    private Integer id;
    @JsonProperty("order_no")
    @IsNotNullField(description = "订单编号")
    
    
    @TableField("order_no")
    private String orderNo;
    @JsonProperty("user_id")
    @IsNotNullField(description = "用户ID")
    
    
    @TableField("user_id")
    private Integer userId;
    @JsonProperty("op_id")
    
    
    @TableField("op_id")
    private String opId;
    @JsonProperty("order_type")
    
    
    @TableField("order_type")
    private String orderType;
    @JsonProperty("order_status")
    @IsNotNullField(description = "订单状态")
    
    
    @TableField("order_status")
    private String orderStatus;
    @JsonProperty("pay_status")
    @IsNotNullField(description = "支付状态")
    
    
    @TableField("pay_status")
    private String payStatus;
    @JsonProperty("delivery_status")
    
    
    @TableField("delivery_status")
    private String deliveryStatus;
    @JsonProperty("products_id")
    @IsNotNullField(description = "商品ID")
    
    
    @TableField("products_id")
    private Integer productsId;
    @JsonProperty("quality")
    @IsNotNullField(description = "商品数量")
    
    
    @TableField("quality")
    private Integer quality;
    @JsonProperty("total_amount")
    @IsNotNullField(description = "订单总额")
    
    
    @TableField("total_amount")
    private Double totalAmount;
    @JsonProperty("discount_amount")
    
    
    @TableField("discount_amount")
    private Double discountAmount;
    @JsonProperty("shipping_fee")
    
    
    @TableField("shipping_fee")
    private Double shippingFee;
    @JsonProperty("actual_amount")
    @IsNotNullField(description = "实付金额")
    
    
    @TableField("actual_amount")
    private Double actualAmount;
    @JsonProperty("payment_channel")
    
    
    @TableField("payment_channel")
    private String paymentChannel;
    @JsonProperty("payment_order_id")
    
    
    @TableField("payment_order_id")
    private String paymentOrderId;
    @JsonProperty("payment_time")
    
    
    @TableField("payment_time")
    private Date paymentTime;
    @JsonProperty("payment_amount")
    
    
    @TableField("payment_amount")
    private Double paymentAmount;
    @JsonProperty("shipping_method")
    
    
    @TableField("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    
    
    @TableField("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_company")
    
    
    @TableField("shipping_company")
    private String shippingCompany;
    @JsonProperty("tracking_number")
    
    
    @TableField("tracking_number")
    private String trackingNumber;
    @JsonProperty("tel")
    
    
    @TableField("tel")
    private String tel;
    @JsonProperty("recipient")
    
    
    @TableField("recipient")
    private String recipient;
    @JsonProperty("create_time")
    @IsNotNullField(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    
    
    private Date createTime;
    @JsonProperty("update_time")
    @IsNotNullField(description = "更新时间")
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    
    private Date updateTime;
    @JsonProperty("cancel_time")
    
    
    @TableField("cancel_time")
    private Date cancelTime;
    @JsonProperty("complete_time")
    
    
    @TableField("complete_time")
    private Date completeTime;
    @JsonProperty("deliver_time")
    
    
    @TableField("deliver_time")
    private Date deliverTime;
    @JsonProperty("refund_id")
    
    
    @TableField("refund_id")
    private String refundId;
    @JsonProperty("refund_no")
    
    
    @TableField("refund_no")
    private String refundNo;
    @JsonProperty("refund_type")
    
    
    @TableField("refund_type")
    private String refundType;
    @JsonProperty("refund_amount")
    
    
    @TableField("refund_amount")
    private Double refundAmount;
    @JsonProperty("refund_reason")
    
    
    @TableField("refund_reason")
    private String refundReason;
    @JsonProperty("refund_status")
    
    
    @TableField("refund_status")
    private String refundStatus;
    @JsonProperty("remark")
    
    
    @TableField("remark")
    private String remark;
    @JsonProperty("is_deleted")
    
    
    @TableField("is_deleted")
    private Integer isDeleted;
    @JsonProperty("table_name")
    
    
    @TableField("table_name")
    private String tableName;
    @JsonProperty("field_name")
    
    
    @TableField("field_name")
    private String fieldName;





}

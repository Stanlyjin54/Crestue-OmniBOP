package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * <p>  订单商品明细 </p>
 *
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAll {
    @TableField(value="oi1.order_no")
    @JsonProperty("order_no")
    private String orderNo;
    @TableField(value="oi1.order_status_enum_order_status_enum_id_1")
    @JsonProperty("order_status_enum_order_status_enum_id_1")
    private Integer orderStatusEnumOrderStatusEnumId1;
    @TableField(value="pi.shelf_life")
    @JsonProperty("shelf_life")
    private Date shelfLife;
    @TableField(value="oi1.customer_info_customer_info_id_1")
    @JsonProperty("customer_info_customer_info_id_1")
    private Integer customerInfoCustomerInfoId1;
    @TableField(value="oi1.commission")
    @JsonProperty("commission")
    private Double commission;
    @TableField(value="pi.barcode")
    @JsonProperty("barcode")
    private String barcode;
    @TableField(value="pi.brand")
    @JsonProperty("brand")
    private String brand;
    @TableField(value="oi.quantity")
    @JsonProperty("quantity")
    private Integer quantity;
    @TableField(value="oi1.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="oi.total_price")
    @JsonProperty("total_price")
    private Double totalPrice;
    @TableField(value="pi.product_status_enum_product_status_enum_id_1")
    @JsonProperty("product_status_enum_product_status_enum_id_1")
    private Integer productStatusEnumProductStatusEnumId1;
    @TableField(value="pi.specification")
    @JsonProperty("specification")
    private String specification;
    @TableField(value="pi.serial_number")
    @JsonProperty("serial_number")
    private String serialNumber;
    @TableField(value="oi.product_info_product_info_id_1")
    @JsonProperty("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @TableField(value="oi.unit_price")
    @JsonProperty("unit_price")
    private Double unitPrice;
    @TableField(value="oi1.profit_amount")
    @JsonProperty("profit_amount")
    private Double profitAmount;
    @TableField(value="pi.product_name")
    @JsonProperty("product_name")
    private String productName;
    @TableField(value="oi.order_info_order_info_id_1")
    @JsonProperty("order_info_order_info_id_1")
    private Integer orderInfoOrderInfoId1;
    @TableField(value="oi.order_item_id")
    @JsonProperty("order_item_id")
    private Integer orderItemId;
    @TableField(value="oi1.shipping_fee")
    @JsonProperty("shipping_fee")
    private Double shippingFee;
    @TableField(value="oi1.order_date")
    @JsonProperty("order_date")
    private Date orderDate;
    @TableField(value="oi1.general_orders")
    @JsonProperty("general_orders")
    private String generalOrders;
    @TableField(value="pi.unit")
    @JsonProperty("unit")
    private String unit;
    @TableField(value="oi1.total_amount")
    @JsonProperty("total_amount")
    private Double totalAmount;
    @TableField(value="pi.min_stock")
    @JsonProperty("min_stock")
    private Integer minStock;
    @TableField(value="pi.category")
    @JsonProperty("category")
    private String category;









}

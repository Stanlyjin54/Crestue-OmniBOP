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
public class OrderInfo    {
    @TableId(value = "order_info_id", type = IdType.AUTO)
    @JsonProperty(value = "orderInfoId")
    @Excel(name = "主键")

    private Integer orderInfoId;
    @JsonProperty(value = "orderNo")
    @Excel(name = "订单编号")
    @ExcelProperty(value = "订单编号")
    @TableField(value = "order_no")

    private String orderNo;
    @JsonProperty(value = "customerInfoCustomerInfoId1")
    @Excel(name = "客户ID")
    @ExcelProperty(value = "客户ID")
    @TableField(value = "customer_info_customer_info_id_1")

    private Integer customerInfoCustomerInfoId1;
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "下单用户ID")
    @ExcelProperty(value = "下单用户ID")
    @TableField(value = "user_info_user_info_id_1")

    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "orderStatusEnumOrderStatusEnumId1")
    @Excel(name = "订单状态")
    @ExcelProperty(value = "订单状态")
    @TableField(value = "order_status_enum_order_status_enum_id_1")

    private Integer orderStatusEnumOrderStatusEnumId1;
    @JsonProperty(value = "totalAmount")
    @Excel(name = "订单总金额")
    @ExcelProperty(value = "订单总金额")
    @TableField(value = "total_amount")

    private Double totalAmount;
    @JsonProperty(value = "profitAmount")
    @Excel(name = "利润金额")
    @ExcelProperty(value = "利润金额")
    @TableField(value = "profit_amount")

    private Double profitAmount;
    @JsonProperty(value = "commission")
    @Excel(name = "平台佣金")
    @ExcelProperty(value = "平台佣金")
    @TableField(value = "commission")

    private Double commission;
    @JsonProperty(value = "shippingFee")
    @Excel(name = "运费")
    @ExcelProperty(value = "运费")
    @TableField(value = "shipping_fee")

    private Double shippingFee;
    @JsonProperty(value = "orderDate")
    @Excel(name = "下单时间")
    @ExcelProperty(value = "下单时间")
    @TableField(value = "order_date")

    private Date orderDate;
    @JsonProperty(value = "generalOrders")
    @Excel(name = "支付订单号")
    @ExcelProperty(value = "支付订单号")
    @TableField(value = "general_orders")

    private String generalOrders;


}






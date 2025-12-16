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
public class GeneralOrders    {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonProperty(value = "id")
    @Excel(name = "主键")

    private Integer id;
    @JsonProperty(value = "orderNo")
    @Excel(name = "订单编号")
    @ExcelProperty(value = "订单编号")
    @TableField(value = "order_no")

    private String orderNo;
    @JsonProperty(value = "userId")
    @Excel(name = "用户ID")
    @ExcelProperty(value = "用户ID")
    @TableField(value = "user_id")

    private Integer userId;
    @JsonProperty(value = "opId")
    @Excel(name = "操作ID")
    @ExcelProperty(value = "操作ID")
    @TableField(value = "op_id")

    private String opId;
    @JsonProperty(value = "orderType")
    @Excel(name = "订单类型")
    @ExcelProperty(value = "订单类型")
    @TableField(value = "order_type")

    private String orderType;
    @JsonProperty(value = "orderStatus")
    @Excel(name = "订单状态")
    @ExcelProperty(value = "订单状态")
    @TableField(value = "order_status")

    private String orderStatus;
    @JsonProperty(value = "payStatus")
    @Excel(name = "支付状态")
    @ExcelProperty(value = "支付状态")
    @TableField(value = "pay_status")

    private String payStatus;
    @JsonProperty(value = "deliveryStatus")
    @Excel(name = "发货状态")
    @ExcelProperty(value = "发货状态")
    @TableField(value = "delivery_status")

    private String deliveryStatus;
    @JsonProperty(value = "productsId")
    @Excel(name = "商品ID")
    @ExcelProperty(value = "商品ID")
    @TableField(value = "products_id")

    private Integer productsId;
    @JsonProperty(value = "quality")
    @Excel(name = "商品数量")
    @ExcelProperty(value = "商品数量")
    @TableField(value = "quality")

    private Integer quality;
    @JsonProperty(value = "totalAmount")
    @Excel(name = "订单总额")
    @ExcelProperty(value = "订单总额")
    @TableField(value = "total_amount")

    private Double totalAmount;
    @JsonProperty(value = "discountAmount")
    @Excel(name = "优惠金额")
    @ExcelProperty(value = "优惠金额")
    @TableField(value = "discount_amount")

    private Double discountAmount;
    @JsonProperty(value = "shippingFee")
    @Excel(name = "运费")
    @ExcelProperty(value = "运费")
    @TableField(value = "shipping_fee")

    private Double shippingFee;
    @JsonProperty(value = "actualAmount")
    @Excel(name = "实付金额")
    @ExcelProperty(value = "实付金额")
    @TableField(value = "actual_amount")

    private Double actualAmount;
    @JsonProperty(value = "paymentChannel")
    @Excel(name = "支付渠道")
    @ExcelProperty(value = "支付渠道")
    @TableField(value = "payment_channel")

    private String paymentChannel;
    @JsonProperty(value = "paymentOrderId")
    @Excel(name = "支付订单号")
    @ExcelProperty(value = "支付订单号")
    @TableField(value = "payment_order_id")

    private String paymentOrderId;
    @JsonProperty(value = "paymentTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "支付时间")
    @ExcelProperty(value = "支付时间")
    @TableField(value = "payment_time")

    private Date paymentTime;
    @JsonProperty(value = "paymentAmount")
    @Excel(name = "支付金额")
    @ExcelProperty(value = "支付金额")
    @TableField(value = "payment_amount")

    private Double paymentAmount;
    @JsonProperty(value = "shippingMethod")
    @Excel(name = "配送方式")
    @ExcelProperty(value = "配送方式")
    @TableField(value = "shipping_method")

    private String shippingMethod;
    @JsonProperty(value = "shippingAddress")
    @Excel(name = "配送地址")
    @ExcelProperty(value = "配送地址")
    @TableField(value = "shipping_address")

    private String shippingAddress;
    @JsonProperty(value = "shippingCompany")
    @Excel(name = "物流公司")
    @ExcelProperty(value = "物流公司")
    @TableField(value = "shipping_company")

    private String shippingCompany;
    @JsonProperty(value = "trackingNumber")
    @Excel(name = "运单号")
    @ExcelProperty(value = "运单号")
    @TableField(value = "tracking_number")

    private String trackingNumber;
    @JsonProperty(value = "tel")
    @Excel(name = "收件人电话")
    @ExcelProperty(value = "收件人电话")
    @TableField(value = "tel")

    private String tel;
    @JsonProperty(value = "recipient")
    @Excel(name = "收件人")
    @ExcelProperty(value = "收件人")
    @TableField(value = "recipient")

    private String recipient;
    @JsonProperty(value = "createTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "create_time")

    private Date createTime;
    @JsonProperty(value = "updateTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "更新时间")
    @ExcelProperty(value = "更新时间")
    @TableField(value = "update_time")

    private Date updateTime;
    @JsonProperty(value = "cancelTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "取消时间")
    @ExcelProperty(value = "取消时间")
    @TableField(value = "cancel_time")

    private Date cancelTime;
    @JsonProperty(value = "completeTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "完成时间")
    @ExcelProperty(value = "完成时间")
    @TableField(value = "complete_time")

    private Date completeTime;
    @JsonProperty(value = "deliverTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "发货时间")
    @ExcelProperty(value = "发货时间")
    @TableField(value = "deliver_time")

    private Date deliverTime;
    @JsonProperty(value = "refundId")
    @Excel(name = "支付系统退款单号")
    @ExcelProperty(value = "支付系统退款单号")
    @TableField(value = "refund_id")

    private String refundId;
    @JsonProperty(value = "refundNo")
    @Excel(name = "业务系统")
    @ExcelProperty(value = "业务系统")
    @TableField(value = "refund_no")

    private String refundNo;
    @JsonProperty(value = "refundType")
    @Excel(name = "退款类型")
    @ExcelProperty(value = "退款类型")
    @TableField(value = "refund_type")

    private String refundType;
    @JsonProperty(value = "refundAmount")
    @Excel(name = "退款金额")
    @ExcelProperty(value = "退款金额")
    @TableField(value = "refund_amount")

    private Double refundAmount;
    @JsonProperty(value = "refundReason")
    @Excel(name = "退款原因")
    @ExcelProperty(value = "退款原因")
    @TableField(value = "refund_reason")

    private String refundReason;
    @JsonProperty(value = "refundStatus")
    @Excel(name = "退款状态")
    @ExcelProperty(value = "退款状态")
    @TableField(value = "refund_status")

    private String refundStatus;
    @JsonProperty(value = "remark")
    @Excel(name = "备注")
    @ExcelProperty(value = "备注")
    @TableField(value = "remark")

    private String remark;
    @JsonProperty(value = "isDeleted")
    @Excel(name = "是否删除")
    @ExcelProperty(value = "是否删除")
    @TableField(value = "is_deleted")

    private Integer isDeleted;
    @JsonProperty(value = "tableName")
    @Excel(name = "表名")
    @ExcelProperty(value = "表名")
    @TableField(value = "table_name")

    private String tableName;
    @JsonProperty(value = "fieldName")
    @Excel(name = "字段名")
    @ExcelProperty(value = "字段名")
    @TableField(value = "field_name")

    private String fieldName;


}






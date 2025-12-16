package com.kuafuweb.web.vo;

import com.kuafuweb.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * <p>订单信息-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderInfoAllPageVO extends PageRequest {

    @JsonProperty(value = "productsId")
    private Integer productsId;
    @JsonProperty(value = "orderNo")
    private String orderNo;
    @JsonProperty(value = "orderStatus")
    private String orderStatus;
    @JsonProperty(value = "orderStatusEnumOrderStatusEnumId1")
    private Integer orderStatusEnumOrderStatusEnumId1;
    @JsonProperty(value = "profitAmount")
    private Double profitAmount;
    @JsonProperty(value = "opId")
    private String opId;
    @JsonProperty(value = "userId")
    private Integer userId;
    @JsonProperty(value = "deliverTime")
    private Date deliverTime;
    @JsonProperty(value = "isDeleted")
    private Integer isDeleted;
    @JsonProperty(value = "paymentAmount")
    private Double paymentAmount;
    @JsonProperty(value = "generalOrders")
    private String generalOrders;
    @JsonProperty(value = "completeTime")
    private Date completeTime;
    @JsonProperty(value = "actualAmount")
    private Double actualAmount;
    @JsonProperty(value = "tel")
    private String tel;
    @JsonProperty(value = "refundAmount")
    private Double refundAmount;
    @JsonProperty(value = "orderNoGo")
    private String orderNoGo;
    @JsonProperty(value = "refundType")
    private String refundType;
    @JsonProperty(value = "shippingMethod")
    private String shippingMethod;
    @JsonProperty(value = "quality")
    private Integer quality;
    @JsonProperty(value = "totalAmount")
    private Double totalAmount;
    @JsonProperty(value = "refundStatus")
    private String refundStatus;
    @JsonProperty(value = "deliveryStatus")
    private String deliveryStatus;
    @JsonProperty(value = "orderType")
    private String orderType;
    @JsonProperty(value = "paymentChannel")
    private String paymentChannel;
    @JsonProperty(value = "remark")
    private String remark;
    @JsonProperty(value = "orderDate")
    private Date orderDate;
    @JsonProperty(value = "shippingFee")
    private Double shippingFee;
    @JsonProperty(value = "discountAmount")
    private Double discountAmount;
    @JsonProperty(value = "paymentOrderId")
    private String paymentOrderId;
    @JsonProperty(value = "trackingNumber")
    private String trackingNumber;
    @JsonProperty(value = "commission")
    private Double commission;
    @JsonProperty(value = "tableName")
    private String tableName;
    @JsonProperty(value = "refundId")
    private String refundId;
    @JsonProperty(value = "shippingAddress")
    private String shippingAddress;
    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "createTime")
    private Date createTime;
    @JsonProperty(value = "cancelTime")
    private Date cancelTime;
    @JsonProperty(value = "refundReason")
    private String refundReason;
    @JsonProperty(value = "paymentTime")
    private Date paymentTime;
    @JsonProperty(value = "fieldName")
    private String fieldName;
    @JsonProperty(value = "customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty(value = "refundNo")
    private String refundNo;
    @JsonProperty(value = "orderInfoId")
    private Integer orderInfoId;
    @JsonProperty(value = "payStatus")
    private String payStatus;
    @JsonProperty(value = "recipient")
    private String recipient;
    @JsonProperty(value = "shippingFeeGo")
    private Double shippingFeeGo;
    @JsonProperty(value = "shippingCompany")
    private String shippingCompany;
    @JsonProperty(value = "totalAmountGo")
    private Double totalAmountGo;
    @JsonProperty(value = "updateTime")
    private Date updateTime;

}






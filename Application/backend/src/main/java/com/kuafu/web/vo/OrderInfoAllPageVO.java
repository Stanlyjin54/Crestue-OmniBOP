package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;

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
public class OrderInfoAllPageVO extends BaseEntity {

@JsonProperty("discountAmount")
    private Double discountAmount;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("paymentTime")
    private Date paymentTime;
@JsonProperty("refundNo")
    private String refundNo;
@JsonProperty("actualAmount")
    private Double actualAmount;
@JsonProperty("source")
    private String source;
@JsonProperty("tableName")
    private String tableName;
@JsonProperty("orderStatusEnumOrderStatusEnumId1")
    private Integer orderStatusEnumOrderStatusEnumId1;
@JsonProperty("orderStatus")
    private String orderStatus;
@JsonProperty("password")
    private String password;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("completeTime")
    private Date completeTime;
@JsonProperty("shippingMethod")
    private String shippingMethod;
@JsonProperty("tel")
    private String tel;
@JsonProperty("orderType")
    private String orderType;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("cancelTime")
    private Date cancelTime;
@JsonProperty("orderNoGo")
    private String orderNoGo;
@JsonProperty("refundType")
    private String refundType;
@JsonProperty("refundStatus")
    private String refundStatus;
@JsonProperty("shippingCompany")
    private String shippingCompany;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("createTime")
    private Date createTime;
@JsonProperty("paymentAmount")
    private Double paymentAmount;
@JsonProperty("profitAmount")
    private Double profitAmount;
@JsonProperty("refundId")
    private String refundId;
@JsonProperty("quality")
    private Integer quality;
@JsonProperty("shippingFee")
    private Double shippingFee;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDate")
    private Date registrationDate;
@JsonProperty("totalAmount")
    private Double totalAmount;
@JsonProperty("phone")
    private String phone;
@JsonProperty("userId")
    private Integer userId;
@JsonProperty("paymentChannel")
    private String paymentChannel;
@JsonProperty("phoneNumber")
    private String phoneNumber;
@JsonProperty("orderNo")
    private String orderNo;
@JsonProperty("remark")
    private String remark;
@JsonProperty("payStatus")
    private String payStatus;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("updateTime")
    private Date updateTime;
@JsonProperty("isDeleted")
    private Integer isDeleted;
@JsonProperty("customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
@JsonProperty("rfmLevelEnumRfmLevelEnumId1")
    private Integer rfmLevelEnumRfmLevelEnumId1;
@JsonProperty("trackingNumber")
    private String trackingNumber;
@JsonProperty("refundAmount")
    private Double refundAmount;
@JsonProperty("commission")
    private Double commission;
@JsonProperty("refundReason")
    private String refundReason;
@JsonProperty("shippingAddress")
    private String shippingAddress;
@JsonProperty("email")
    private String email;
@JsonProperty("wechatId")
    private String wechatId;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("deliverTime")
    private Date deliverTime;
@JsonProperty("orderInfoId")
    private Integer orderInfoId;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
@JsonProperty("address")
    private String address;
@JsonProperty("statusName")
    private String statusName;
@JsonProperty("opId")
    private String opId;
@JsonProperty("paymentOrderId")
    private String paymentOrderId;
@JsonProperty("fieldName")
    private String fieldName;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("orderDate")
    private Date orderDate;
@JsonProperty("generalOrders")
    private String generalOrders;
@JsonProperty("productsId")
    private Integer productsId;
@JsonProperty("wechatDataEncrypted")
    private String wechatDataEncrypted;
@JsonProperty("recipient")
    private String recipient;
@JsonProperty("shippingFeeGo")
    private Double shippingFeeGo;
@JsonProperty("customerTagEnumCustomerTagEnumId1")
    private Integer customerTagEnumCustomerTagEnumId1;
@JsonProperty("totalAmountGo")
    private Double totalAmountGo;
@JsonProperty("customerName")
    private String customerName;
@JsonProperty("deliveryStatus")
    private String deliveryStatus;
@JsonProperty("username")
    private String username;

}

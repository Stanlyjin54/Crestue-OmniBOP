package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
/**
 * <p>系统订单信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralOrdersVO  extends BaseEntity {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("opId")
    private String opId;
    @JsonProperty("orderType")
    private String orderType;
    @JsonProperty("orderStatus")
    private String orderStatus;
    @JsonProperty("payStatus")
    private String payStatus;
    @JsonProperty("deliveryStatus")
    private String deliveryStatus;
    @JsonProperty("productsId")
    private Integer productsId;
    @JsonProperty("quality")
    private Integer quality;
    @JsonProperty("totalAmount")
    private Double totalAmount;
    @JsonProperty("discountAmount")
    private Double discountAmount;
    @JsonProperty("shippingFee")
    private Double shippingFee;
    @JsonProperty("actualAmount")
    private Double actualAmount;
    @JsonProperty("paymentChannel")
    private String paymentChannel;
    @JsonProperty("paymentOrderId")
    private String paymentOrderId;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("paymentTime")
    private Date paymentTime;
    @JsonProperty("paymentAmount")
    private Double paymentAmount;
    @JsonProperty("shippingMethod")
    private String shippingMethod;
    @JsonProperty("shippingAddress")
    private String shippingAddress;
    @JsonProperty("shippingCompany")
    private String shippingCompany;
    @JsonProperty("trackingNumber")
    private String trackingNumber;
    @JsonProperty("tel")
    private String tel;
    @JsonProperty("recipient")
    private String recipient;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("createTime")
    private Date createTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("updateTime")
    private Date updateTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("cancelTime")
    private Date cancelTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("completeTime")
    private Date completeTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("deliverTime")
    private Date deliverTime;
    @JsonProperty("refundId")
    private String refundId;
    @JsonProperty("refundNo")
    private String refundNo;
    @JsonProperty("refundType")
    private String refundType;
    @JsonProperty("refundAmount")
    private Double refundAmount;
    @JsonProperty("refundReason")
    private String refundReason;
    @JsonProperty("refundStatus")
    private String refundStatus;
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("isDeleted")
    private Integer isDeleted;
    @JsonProperty("tableName")
    private String tableName;
    @JsonProperty("fieldName")
    private String fieldName;




}

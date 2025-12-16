package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

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
public class GeneralOrdersVO  {

     @JsonProperty(value = "id")
    private Integer id;
     @JsonProperty(value = "orderNo")
    private String orderNo;
     @JsonProperty(value = "userId")
    private Integer userId;
     @JsonProperty(value = "opId")
    private String opId;
     @JsonProperty(value = "orderType")
    private String orderType;
     @JsonProperty(value = "orderStatus")
    private String orderStatus;
     @JsonProperty(value = "payStatus")
    private String payStatus;
     @JsonProperty(value = "deliveryStatus")
    private String deliveryStatus;
     @JsonProperty(value = "productsId")
    private Integer productsId;
     @JsonProperty(value = "quality")
    private Integer quality;
     @JsonProperty(value = "totalAmount")
    private Double totalAmount;
     @JsonProperty(value = "discountAmount")
    private Double discountAmount;
     @JsonProperty(value = "shippingFee")
    private Double shippingFee;
     @JsonProperty(value = "actualAmount")
    private Double actualAmount;
     @JsonProperty(value = "paymentChannel")
    private String paymentChannel;
     @JsonProperty(value = "paymentOrderId")
    private String paymentOrderId;
     @JsonProperty(value = "paymentTime")
    private Date paymentTime;
     @JsonProperty(value = "paymentAmount")
    private Double paymentAmount;
     @JsonProperty(value = "shippingMethod")
    private String shippingMethod;
     @JsonProperty(value = "shippingAddress")
    private String shippingAddress;
     @JsonProperty(value = "shippingCompany")
    private String shippingCompany;
     @JsonProperty(value = "trackingNumber")
    private String trackingNumber;
     @JsonProperty(value = "tel")
    private String tel;
     @JsonProperty(value = "recipient")
    private String recipient;
     @JsonProperty(value = "createTime")
    private Date createTime;
     @JsonProperty(value = "updateTime")
    private Date updateTime;
     @JsonProperty(value = "cancelTime")
    private Date cancelTime;
     @JsonProperty(value = "completeTime")
    private Date completeTime;
     @JsonProperty(value = "deliverTime")
    private Date deliverTime;
     @JsonProperty(value = "refundId")
    private String refundId;
     @JsonProperty(value = "refundNo")
    private String refundNo;
     @JsonProperty(value = "refundType")
    private String refundType;
     @JsonProperty(value = "refundAmount")
    private Double refundAmount;
     @JsonProperty(value = "refundReason")
    private String refundReason;
     @JsonProperty(value = "refundStatus")
    private String refundStatus;
     @JsonProperty(value = "remark")
    private String remark;
     @JsonProperty(value = "isDeleted")
    private Integer isDeleted;
     @JsonProperty(value = "tableName")
    private String tableName;
     @JsonProperty(value = "fieldName")
    private String fieldName;


}






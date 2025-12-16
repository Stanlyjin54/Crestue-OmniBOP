package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class OrderInfoPageVO extends PageRequest {

    @JsonProperty(value = "orderInfoId")
    private Integer orderInfoId;
    @JsonProperty(value = "orderNo")
    private String orderNo;
    @JsonProperty(value = "customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "orderStatusEnumOrderStatusEnumId1")
    private Integer orderStatusEnumOrderStatusEnumId1;
    @JsonProperty(value = "totalAmount")
    private Double totalAmount;
    @JsonProperty(value = "profitAmount")
    private Double profitAmount;
    @JsonProperty(value = "commission")
    private Double commission;
    @JsonProperty(value = "shippingFee")
    private Double shippingFee;
    @JsonProperty(value = "orderDate")
    private Date orderDate;
    @JsonProperty(value = "generalOrders")
    private String generalOrders;

}






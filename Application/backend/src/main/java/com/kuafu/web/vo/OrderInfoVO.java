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
 * <p>订单信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoVO  extends BaseEntity {

    @JsonProperty("orderInfoId")
    private Integer orderInfoId;
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("orderStatusEnumOrderStatusEnumId1")
    private Integer orderStatusEnumOrderStatusEnumId1;
    @JsonProperty("totalAmount")
    private Double totalAmount;
    @JsonProperty("profitAmount")
    private Double profitAmount;
    @JsonProperty("commission")
    private Double commission;
    @JsonProperty("shippingFee")
    private Double shippingFee;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("orderDate")
    private Date orderDate;
    @JsonProperty("generalOrders")
    private String generalOrders;




}

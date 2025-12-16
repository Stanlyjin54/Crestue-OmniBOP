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
 * <p>直播带货同步-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LiveStreamSyncAllPageVO extends BaseEntity {

@JsonProperty("orderNo")
    private String orderNo;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("syncTime")
    private Date syncTime;
@JsonProperty("profitAmount")
    private Double profitAmount;
@JsonProperty("liveStreamSyncId")
    private Integer liveStreamSyncId;
@JsonProperty("platform")
    private String platform;
@JsonProperty("orderStatusEnumOrderStatusEnumId1")
    private Integer orderStatusEnumOrderStatusEnumId1;
@JsonProperty("orderInfoOrderInfoId1")
    private Integer orderInfoOrderInfoId1;
@JsonProperty("shippingFee")
    private Double shippingFee;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("orderDate")
    private Date orderDate;
@JsonProperty("generalOrders")
    private String generalOrders;
@JsonProperty("customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
@JsonProperty("totalAmount")
    private Double totalAmount;
@JsonProperty("commission")
    private Double commission;

}

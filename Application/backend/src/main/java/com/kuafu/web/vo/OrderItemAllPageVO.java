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
 * <p>订单商品明细-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderItemAllPageVO extends BaseEntity {

@JsonProperty("orderNo")
    private String orderNo;
@JsonProperty("orderStatusEnumOrderStatusEnumId1")
    private Integer orderStatusEnumOrderStatusEnumId1;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("shelfLife")
    private Date shelfLife;
@JsonProperty("customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
@JsonProperty("commission")
    private Double commission;
@JsonProperty("barcode")
    private String barcode;
@JsonProperty("brand")
    private String brand;
@JsonProperty("quantity")
    private Integer quantity;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
@JsonProperty("totalPrice")
    private Double totalPrice;
@JsonProperty("productStatusEnumProductStatusEnumId1")
    private Integer productStatusEnumProductStatusEnumId1;
@JsonProperty("specification")
    private String specification;
@JsonProperty("serialNumber")
    private String serialNumber;
@JsonProperty("productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
@JsonProperty("unitPrice")
    private Double unitPrice;
@JsonProperty("profitAmount")
    private Double profitAmount;
@JsonProperty("productName")
    private String productName;
@JsonProperty("orderInfoOrderInfoId1")
    private Integer orderInfoOrderInfoId1;
@JsonProperty("orderItemId")
    private Integer orderItemId;
@JsonProperty("shippingFee")
    private Double shippingFee;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("orderDate")
    private Date orderDate;
@JsonProperty("generalOrders")
    private String generalOrders;
@JsonProperty("unit")
    private String unit;
@JsonProperty("totalAmount")
    private Double totalAmount;
@JsonProperty("minStock")
    private Integer minStock;
@JsonProperty("category")
    private String category;

}

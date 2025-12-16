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
 * <p>订单商品明细</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVO  extends BaseEntity {

    @JsonProperty("orderItemId")
    private Integer orderItemId;
    @JsonProperty("orderInfoOrderInfoId1")
    private Integer orderInfoOrderInfoId1;
    @JsonProperty("productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("unitPrice")
    private Double unitPrice;
    @JsonProperty("totalPrice")
    private Double totalPrice;




}

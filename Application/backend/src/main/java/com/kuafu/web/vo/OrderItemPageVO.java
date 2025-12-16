package com.kuafu.web.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.entity.BaseEntity;
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
public class OrderItemPageVO extends BaseEntity {

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

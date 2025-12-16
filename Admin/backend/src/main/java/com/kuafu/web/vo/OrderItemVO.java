package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

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
public class OrderItemVO  {

     @JsonProperty(value = "orderItemId")
    private Integer orderItemId;
     @JsonProperty(value = "orderInfoOrderInfoId1")
    private Integer orderInfoOrderInfoId1;
     @JsonProperty(value = "productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
     @JsonProperty(value = "quantity")
    private Integer quantity;
     @JsonProperty(value = "unitPrice")
    private Double unitPrice;
     @JsonProperty(value = "totalPrice")
    private Double totalPrice;


}






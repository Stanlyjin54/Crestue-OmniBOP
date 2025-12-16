package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>订单状态枚举</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusEnumVO  {

     @JsonProperty(value = "orderStatusEnumId")
    private Integer orderStatusEnumId;
     @JsonProperty(value = "statusName")
    private String statusName;


}






package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>利润看板</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitBoardVO  {

     @JsonProperty(value = "profitBoardId")
    private Integer profitBoardId;
     @JsonProperty(value = "targetType")
    private String targetType;
     @JsonProperty(value = "targetId")
    private Integer targetId;
     @JsonProperty(value = "profitAmount")
    private Double profitAmount;
     @JsonProperty(value = "commission")
    private Double commission;
     @JsonProperty(value = "shippingFee")
    private Double shippingFee;
     @JsonProperty(value = "statDate")
    private Date statDate;


}






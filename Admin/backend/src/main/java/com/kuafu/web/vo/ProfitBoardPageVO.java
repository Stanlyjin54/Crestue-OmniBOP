package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>利润看板-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfitBoardPageVO extends PageRequest {

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






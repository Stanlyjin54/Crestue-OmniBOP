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
public class ProfitBoardPageVO extends BaseEntity {

@JsonProperty("profitBoardId")
    private Integer profitBoardId;
@JsonProperty("targetType")
    private String targetType;
@JsonProperty("targetId")
    private Integer targetId;
@JsonProperty("profitAmount")
    private Double profitAmount;
@JsonProperty("commission")
    private Double commission;
@JsonProperty("shippingFee")
    private Double shippingFee;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("statDate")
    private Date statDate;


}

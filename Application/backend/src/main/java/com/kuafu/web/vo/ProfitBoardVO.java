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
 * <p>利润看板</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitBoardVO  extends BaseEntity {

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

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
 * <p>采购建议</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcurementSuggestionVO  extends BaseEntity {

    @JsonProperty("procurementSuggestionId")
    private Integer procurementSuggestionId;
    @JsonProperty("productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("suggestQuantity")
    private Integer suggestQuantity;
    @JsonProperty("reason")
    private String reason;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("suggestDate")
    private Date suggestDate;




}

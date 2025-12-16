package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>采购建议-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProcurementSuggestionPageVO extends PageRequest {

    @JsonProperty(value = "procurementSuggestionId")
    private Integer procurementSuggestionId;
    @JsonProperty(value = "productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
    @JsonProperty(value = "suggestQuantity")
    private Integer suggestQuantity;
    @JsonProperty(value = "reason")
    private String reason;
    @JsonProperty(value = "suggestDate")
    private Date suggestDate;

}






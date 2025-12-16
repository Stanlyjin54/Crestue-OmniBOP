package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>商机线索-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessOpportunityPageVO extends PageRequest {

    @JsonProperty(value = "businessOpportunityId")
    private Integer businessOpportunityId;
    @JsonProperty(value = "customerInfoCustomerInfoId1")
    private Integer customerInfoCustomerInfoId1;
    @JsonProperty(value = "opportunityName")
    private String opportunityName;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "followUpSuggestion")
    private String followUpSuggestion;

}






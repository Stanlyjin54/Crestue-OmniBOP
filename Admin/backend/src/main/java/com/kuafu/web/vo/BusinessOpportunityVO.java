package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>商机线索</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessOpportunityVO  {

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






package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

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
public class ProcurementSuggestionVO  {

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






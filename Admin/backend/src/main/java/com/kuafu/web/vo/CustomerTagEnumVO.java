package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>客户标签枚举</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagEnumVO  {

     @JsonProperty(value = "customerTagEnumId")
    private Integer customerTagEnumId;
     @JsonProperty(value = "tagName")
    private String tagName;
     @JsonProperty(value = "isAiGenerated")
    private Boolean isAiGenerated;


}






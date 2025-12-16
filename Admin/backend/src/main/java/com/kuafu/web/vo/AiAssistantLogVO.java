package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>AI助手日志</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAssistantLogVO  {

     @JsonProperty(value = "aiAssistantLogId")
    private Integer aiAssistantLogId;
     @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
     @JsonProperty(value = "queryText")
    private String queryText;
     @JsonProperty(value = "responseText")
    private String responseText;
     @JsonProperty(value = "queryTime")
    private Date queryTime;


}






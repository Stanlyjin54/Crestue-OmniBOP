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
 * <p>AI助手日志</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAssistantLogVO  extends BaseEntity {

    @JsonProperty("aiAssistantLogId")
    private Integer aiAssistantLogId;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("queryText")
    private String queryText;
    @JsonProperty("responseText")
    private String responseText;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("queryTime")
    private Date queryTime;




}

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
 * <p>AI助手日志-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AiAssistantLogPageVO extends BaseEntity {

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

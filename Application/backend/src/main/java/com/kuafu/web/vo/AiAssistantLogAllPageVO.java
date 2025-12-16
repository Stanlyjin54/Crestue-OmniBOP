package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;

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
public class AiAssistantLogAllPageVO extends BaseEntity {

@JsonProperty("password")
    private String password;
@JsonProperty("aiAssistantLogId")
    private Integer aiAssistantLogId;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDate")
    private Date registrationDate;
@JsonProperty("responseText")
    private String responseText;
@JsonProperty("phoneNumber")
    private String phoneNumber;
@JsonProperty("queryText")
    private String queryText;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("queryTime")
    private Date queryTime;
@JsonProperty("username")
    private String username;

}

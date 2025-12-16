package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * <p>  AI助手日志 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAssistantLogAll {
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="aal.ai_assistant_log_id")
    @JsonProperty("ai_assistant_log_id")
    private Integer aiAssistantLogId;
    @TableField(value="aal.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="aal.response_text")
    @JsonProperty("response_text")
    private String responseText;
    @TableField(value="ui.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="aal.query_text")
    @JsonProperty("query_text")
    private String queryText;
    @TableField(value="aal.query_time")
    @JsonProperty("query_time")
    private Date queryTime;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;









}

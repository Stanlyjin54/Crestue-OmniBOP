package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
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
@TableName("ai_assistant_log")
public class AiAssistantLog  {
    @TableId(value = "ai_assistant_log_id", type = IdType.AUTO)
    @JsonProperty("ai_assistant_log_id")
    
    
    
    private Integer aiAssistantLogId;
    @JsonProperty("user_info_user_info_id_1")
    @IsNotNullField(description = "用户ID")
    
    
    @TableField("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("query_text")
    @IsNotNullField(description = "查询内容")
    
    
    @TableField("query_text")
    private String queryText;
    @JsonProperty("response_text")
    @IsNotNullField(description = "AI回复内容")
    
    
    @TableField("response_text")
    private String responseText;
    @JsonProperty("query_time")
    @IsNotNullField(description = "查询时间")
    
    
    @TableField("query_time")
    private Date queryTime;





}

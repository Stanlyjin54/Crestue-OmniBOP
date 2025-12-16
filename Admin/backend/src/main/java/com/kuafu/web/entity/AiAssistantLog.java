package com.kuafuweb.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafuweb.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;
import com.kuafuweb.common.annotation.Excel.*;



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
public class AiAssistantLog    {
    @TableId(value = "ai_assistant_log_id", type = IdType.AUTO)
    @JsonProperty(value = "aiAssistantLogId")
    @Excel(name = "主键")

    private Integer aiAssistantLogId;
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    @ExcelProperty(value = "用户ID")
    @TableField(value = "user_info_user_info_id_1")

    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "queryText")
    @Excel(name = "查询内容")
    @ExcelProperty(value = "查询内容")
    @TableField(value = "query_text")

    private String queryText;
    @JsonProperty(value = "responseText")
    @Excel(name = "AI回复内容")
    @ExcelProperty(value = "AI回复内容")
    @TableField(value = "response_text")

    private String responseText;
    @JsonProperty(value = "queryTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "查询时间")
    @ExcelProperty(value = "查询时间")
    @TableField(value = "query_time")

    private Date queryTime;


}






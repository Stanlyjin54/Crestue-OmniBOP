package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p> 政府采购信息实体 </p>
 *
 * @author kuafuai
 * @description 存储采集的政府采购和招标信息
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("procurement_info")
public class ProcurementInfo {

    @TableId(value = "procurement_info_id", type = IdType.AUTO)
    @JsonProperty(value = "procurementInfoId")
    private Integer procurementInfoId;

    @JsonProperty(value = "title")
    @TableField(value = "title")
    private String title; // 采购标题

    @JsonProperty(value = "content")
    @TableField(value = "content")
    private String content; // 采购内容描述

    @JsonProperty(value = "procurementType")
    @TableField(value = "procurement_type")
    private String procurementType; // 采购类型（政府采购、招标等）

    @JsonProperty(value = "region")
    @TableField(value = "region")
    private String region; // 地区

    @JsonProperty(value = "budget")
    @TableField(value = "budget")
    private String budget; // 预算金额

    @JsonProperty(value = "publishDate")
    @TableField(value = "publish_date")
    private LocalDateTime publishDate; // 发布日期

    @JsonProperty(value = "deadline")
    @TableField(value = "deadline")
    private LocalDateTime deadline; // 截止日期

    @JsonProperty(value = "sourceUrl")
    @TableField(value = "source_url")
    private String sourceUrl; // 来源网址

    @JsonProperty(value = "sourceWebsite")
    @TableField(value = "source_website")
    private String sourceWebsite; // 来源网站名称

    @JsonProperty(value = "contactInfo")
    @TableField(value = "contact_info")
    private String contactInfo; // 联系方式

    @JsonProperty(value = "industry")
    @TableField(value = "industry")
    private String industry; // 行业分类

    @JsonProperty(value = "matchScore")
    @TableField(value = "match_score")
    private Double matchScore; // 匹配得分

    @JsonProperty(value = "status")
    @TableField(value = "status")
    private String status; // 状态（pending, approved, rejected, converted）

    @JsonProperty(value = "reviewedBy")
    @TableField(value = "reviewed_by")
    private Integer reviewedBy; // 审核人

    @JsonProperty(value = "reviewTime")
    @TableField(value = "review_time")
    private LocalDateTime reviewTime; // 审核时间

    @JsonProperty(value = "remark")
    @TableField(value = "remark")
    private String remark; // 备注

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
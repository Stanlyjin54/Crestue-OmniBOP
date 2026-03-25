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
 * <p>  商机线索 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_opportunity")
public class BusinessOpportunity  {
    @TableId(value = "business_opportunity_id", type = IdType.AUTO)
    @JsonProperty("business_opportunity_id")
    private Integer businessOpportunityId;
    
    @JsonProperty("customer_info_customer_info_id_1")
    @IsNotNullField(description = "客户ID")
    @TableField("customer_info_customer_info_id_1")
    private Integer customerInfoCustomerInfoId1;
    
    @JsonProperty("opportunity_name")
    @IsNotNullField(description = "商机名称")
    @TableField("opportunity_name")
    private String opportunityName;
    
    @JsonProperty("status")
    @IsNotNullField(description = "商机状态")
    @TableField("status")
    private String status;
    
    @JsonProperty("follow_up_suggestion")
    @TableField("follow_up_suggestion")
    private String followUpSuggestion;
    
    // 新增字段以支持服务实现中的方法调用
    @JsonProperty("title")
    @TableField("title")
    private String title;
    
    @JsonProperty("description")
    @TableField("description")
    private String description;
    
    @JsonProperty("category")
    @TableField("category")
    private String category;
    
    @JsonProperty("source")
    @TableField("source")
    private String source;
    
    @JsonProperty("source_id")
    @TableField("source_id")
    private String sourceId;
    
    @JsonProperty("source_url")
    @TableField("source_url")
    private String sourceUrl;
    
    @JsonProperty("contact_info")
    @TableField("contact_info")
    private String contactInfo;
    
    @JsonProperty("company")
    @TableField("company")
    private String company;
    
    @JsonProperty("priority")
    @TableField("priority")
    private String priority;
    
    @JsonProperty("estimated_value")
    @TableField("estimated_value")
    private Double estimatedValue;
    
    @JsonProperty("probability")
    @TableField("probability")
    private Integer probability;
    
    @JsonProperty("expected_close_date")
    @TableField("expected_close_date")
    private Date expectedCloseDate;
    
    @JsonProperty("assigned_to")
    @TableField("assigned_to")
    private Integer assignedTo;
    
    @JsonProperty("created_by")
    @TableField("created_by")
    private Integer createdBy;
    
    @JsonProperty("created_time")
    @TableField("created_time")
    private Date createdTime;
    
    @JsonProperty("updated_by")
    @TableField("updated_by")
    private Integer updatedBy;
    
    @JsonProperty("updated_time")
    @TableField("updated_time")
    private Date updatedTime;
    
    @JsonProperty("approved_by")
    @TableField("approved_by")
    private Long approvedBy;
    
    @JsonProperty("approved_time")
    @TableField("approved_time")
    private Date approvedTime;
    
    @JsonProperty("contact_person")
    @TableField("contact_person")
    private String contactPerson;
    
    @JsonProperty("contact_phone")
    @TableField("contact_phone")
    private String contactPhone;
    
    @JsonProperty("contact_email")
    @TableField("contact_email")
    private String contactEmail;
    
    @JsonProperty("company_name")
    @TableField("company_name")
    private String companyName;
    
    @JsonProperty("address")
    @TableField("address")
    private String address;
    
    @JsonProperty("region")
    @TableField("region")
    private String region;
    
    @JsonProperty("industry")
    @TableField("industry")
    private String industry;
    
    @JsonProperty("tags")
    @TableField("tags")
    private String tags;
    
    @JsonProperty("notes")
    @TableField("notes")
    private String notes;
    
    @JsonProperty("is_active")
    @TableField("is_active")
    private Boolean isActive;
    
    @JsonProperty("conversion_probability")
    @TableField("conversion_probability")
    private Integer conversionProbability;
    
    @JsonProperty("next_follow_up_date")
    @TableField("next_follow_up_date")
    private Date nextFollowUpDate;
    
    @JsonProperty("last_follow_up_date")
    @TableField("last_follow_up_date")
    private Date lastFollowUpDate;
    
    @JsonProperty("follow_up_count")
    @TableField("follow_up_count")
    private Integer followUpCount;
    
    @JsonProperty("score")
    @TableField("score")
    private Integer score;
    
    @JsonProperty("stage")
    @TableField("stage")
    private String stage;
    
    @JsonProperty("source_details")
    @TableField("source_details")
    private String sourceDetails;
    
    @JsonProperty("budget")
    @TableField("budget")
    private Double budget;
    
    @JsonProperty("decision_maker")
    @TableField("decision_maker")
    private String decisionMaker;
    
    @JsonProperty("competitor")
    @TableField("competitor")
    private String competitor;
    
    @JsonProperty("win_probability")
    @TableField("win_probability")
    private Integer winProbability;
    
    @JsonProperty("lost_reason")
    @TableField("lost_reason")
    private String lostReason;
    
    @JsonProperty("custom_fields")
    @TableField("custom_fields")
    private String customFields;
    
    @JsonProperty("attachments")
    @TableField("attachments")
    private String attachments;
    
    @JsonProperty("approval_status")
    @TableField("approval_status")
    private String approvalStatus;
    
    @JsonProperty("review_comments")
    @TableField("review_comments")
    private String reviewComments;
    
    @JsonProperty("reviewed_by")
    @TableField("reviewed_by")
    private Long reviewedBy;
    
    @JsonProperty("review_date")
    @TableField("review_date")
    private Date reviewDate;
}

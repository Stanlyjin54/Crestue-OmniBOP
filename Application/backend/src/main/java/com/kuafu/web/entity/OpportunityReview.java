package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商机审核记录实体类
 */
@Data
@TableName("opportunity_review")
public class OpportunityReview {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商机ID
     */
    private Long opportunityId;

    /**
     * 提交人ID
     */
    private Long submitterId;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核状态 (pending/approved/rejected)
     */
    private String status;

    /**
     * 审核决定
     */
    private String decision;

    /**
     * 审核意见
     */
    private String comments;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date submitTime;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /**
     * 优先级 (high/medium/low)
     */
    private String priority;

    /**
     * 审核期限
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    /**
     * 是否已通知
     */
    private Boolean notified;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Integer deleted;
}
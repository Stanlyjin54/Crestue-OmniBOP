package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 访问日志实体类
 * 记录爬虫访问日志，用于合规性检查
 */
@Data
@TableName("access_log")
public class AccessLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 访问的URL
     */
    private String url;

    /**
     * 域名
     */
    private String domain;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;

    /**
     * 响应状态码
     */
    private Integer responseStatus;

    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 访问类型（crawl/parse/match）
     */
    private String accessType;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 是否遵守robots.txt
     */
    private Boolean robotsCompliant;

    /**
     * 合规检查结果
     */
    private String complianceResult;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Integer deleted;
}
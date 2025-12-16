package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 社交媒体内容实体
 * 存储从抖音、微信、小红书等平台获取的内容信息
 */
@Data
@TableName("social_media_content")
public class SocialMediaContent {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 平台类型：douyin, wechat, xiaohongshu, weibo, zhihu, etc.
     */
    private String platform;

    /**
     * 内容类型：video, article, note, post, etc.
     */
    private String contentType;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容描述/正文
     */
    private String content;

    /**
     * 原始链接
     */
    private String originalUrl;

    /**
     * 作者ID
     */
    private String authorId;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 作者粉丝数
     */
    private Integer authorFollowers;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 分享数
     */
    private Integer shares;

    /**
     * 收藏数
     */
    private Integer favorites;

    /**
     * 观看数/阅读数
     */
    private Integer views;

    /**
     * 内容标签（JSON格式）
     */
    private String tags;

    /**
     * 关键词（逗号分隔）
     */
    private String keywords;

    /**
     * 情感倾向：positive, negative, neutral
     */
    private String sentiment;

    /**
     * 商业价值评分（0-100）
     */
    private Integer commercialScore;

    /**
     * 转化潜力：high, medium, low
     */
    private String conversionPotential;

    /**
     * 是否已处理
     */
    private Boolean processed;

    /**
     * 处理结果：opportunity_found, no_opportunity, needs_review
     */
    private String processResult;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date processTime;

    /**
     * 内容发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 采集时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 状态：active, deleted
     */
    private String status;
}
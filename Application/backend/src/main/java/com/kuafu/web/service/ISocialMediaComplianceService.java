package com.kuafuweb.web.service;

import java.util.List;
import java.util.Map;

/**
 * 社交媒体内容合规性检查服务接口
 * 确保从抖音、微信、小红书等平台获取的内容符合法律法规和平台政策
 */
public interface ISocialMediaComplianceService {

    /**
     * 检查社交媒体内容合规性
     * @param content 内容文本
     * @param platform 平台名称（douyin/wechat/xiaohongshu）
     * @param contentType 内容类型（video/article/note）
     * @return 合规检查结果
     */
    Map<String, Object> checkContentCompliance(String content, String platform, String contentType);

    /**
     * 基础内容检查
     * @param content 内容文本
     * @return 基础检查结果
     */
    Map<String, Object> checkBasicContent(String content);

    /**
     * 平台特定规则检查
     * @param content 内容文本
     * @param platform 平台名称
     * @param contentType 内容类型
     * @return 平台规则检查结果
     */
    Map<String, Object> checkPlatformSpecificRules(String content, String platform, String contentType);

    /**
     * 敏感内容检测
     * @param content 内容文本
     * @return 敏感内容检测结果
     */
    Map<String, Object> checkSensitiveContent(String content);

    /**
     * 广告合规检查
     * @param content 内容文本
     * @return 广告合规检查结果
     */
    Map<String, Object> checkAdvertisingCompliance(String content);

    /**
     * 版权检查
     * @param content 内容文本
     * @return 版权检查结果
     */
    Map<String, Object> checkCopyrightIssues(String content);

    /**
     * KOL/网红合规检查
     * @param content 内容文本
     * @param influencerInfo 网红信息（粉丝数、认证状态等）
     * @return KOL合规检查结果
     */
    Map<String, Object> checkInfluencerCompliance(String content, Map<String, Object> influencerInfo);

    /**
     * 生成合规报告
     * @param contentIds 内容ID列表
     * @param platform 平台名称
     * @return 合规报告
     */
    Map<String, Object> generateComplianceReport(List<String> contentIds, String platform);

    /**
     * 设置合规配置
     * @param config 合规配置参数
     * @return 设置是否成功
     */
    boolean setComplianceConfig(Map<String, Object> config);

    /**
     * 获取合规状态
     * @return 当前合规状态
     */
    Map<String, Object> getComplianceStatus();
}
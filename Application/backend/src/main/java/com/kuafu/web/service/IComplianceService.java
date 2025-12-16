package com.kuafuweb.web.service;

import java.util.List;
import java.util.Map;

/**
 * 合规性检查和访问控制接口
 * 确保爬虫服务符合法律法规要求
 */
public interface IComplianceService {

    /**
     * 检查URL是否可以爬取
     * @param url 目标URL
     * @return 是否允许爬取
     */
    boolean canCrawl(String url);

    /**
     * 检查robots.txt规则
     * @param url 目标URL
     * @return 是否遵守robots.txt
     */
    boolean checkRobotsTxt(String url);

    /**
     * 检查访问频率限制
     * @param domain 目标域名
     * @param userAgent 用户代理
     * @return 是否允许访问
     */
    boolean checkRateLimit(String domain, String userAgent);

    /**
     * 记录访问日志
     * @param url 访问的URL
     * @param userAgent 用户代理
     * @param success 是否成功
     * @param responseTime 响应时间（毫秒）
     */
    void logAccess(String url, String userAgent, boolean success, long responseTime);

    /**
     * 检查数据合法性
     * @param content 爬取的内容
     * @return 是否合法
     */
    boolean validateContent(String content);

    /**
     * 获取合规配置
     * @return 合规配置信息
     */
    Map<String, Object> getComplianceConfig();

    /**
     * 更新合规配置
     * @param config 新的合规配置
     */
    void updateComplianceConfig(Map<String, Object> config);

    /**
     * 获取访问统计信息
     * @param domain 域名（可选）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     @return 访问统计信息
     */
    Map<String, Object> getAccessStatistics(String domain, String startDate, String endDate);

    /**
     * 检查用户权限
     * @param userId 用户ID
     * @param permission 权限类型
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, String permission);

    /**
     * 添加域名到白名单
     * @param domain 域名
     * @param reason 添加原因
     * @return 是否添加成功
     */
    boolean addToWhitelist(String domain, String reason);

    /**
     * 添加域名到黑名单
     * @param domain 域名
     * @param reason 添加原因
     * @return 是否添加成功
     */
    boolean addToBlacklist(String domain, String reason);

    /**
     * 生成合规报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 合规报告
     */
    Map<String, Object> generateComplianceReport(String startDate, String endDate);

    /**
     * 检查是否需要人工审核
     * @param content 内容
     * @param source 来源
     * @return 是否需要人工审核
     */
    boolean requiresManualReview(String content, String source);

    /**
     * 获取法律声明模板
     * @return 法律声明模板
     */
    String getLegalDisclaimer();

    /**
     * 检查数据保护合规性
     * @param data 数据内容
     * @return 是否合规
     */
    boolean checkDataProtectionCompliance(String data);
}
package com.kuafuweb.web.service;

import com.kuafuweb.web.entity.SocialMediaContent;
import com.kuafuweb.web.entity.BusinessOpportunity;

import java.util.List;
import java.util.Map;

/**
 * 社交媒体商机获取服务接口
 * 支持抖音、微信、小红书等平台的种草内容分析和商机提取
 */
public interface ISocialMediaOpportunityService {

    /**
     * 分析抖音视频内容，提取商机信息
     * @param videoUrl 视频链接
     * @param videoTitle 视频标题
     * @param videoDescription 视频描述
     * @param authorInfo 作者信息
     * @return 提取的商机列表
     */
    List<BusinessOpportunity> analyzeDouyinContent(String videoUrl, String videoTitle, String videoDescription, Map<String, Object> authorInfo);

    /**
     * 分析微信公众号文章内容，提取商机信息
     * @param articleUrl 文章链接
     * @param articleTitle 文章标题
     * @param articleContent 文章内容
     * @param authorInfo 作者信息
     * @return 提取的商机列表
     */
    List<BusinessOpportunity> analyzeWechatArticle(String articleUrl, String articleTitle, String articleContent, Map<String, Object> authorInfo);

    /**
     * 分析小红书笔记内容，提取商机信息
     * @param noteUrl 笔记链接
     * @param noteTitle 笔记标题
     * @param noteContent 笔记内容
     * @param authorInfo 作者信息
     * @return 提取的商机列表
     */
    List<BusinessOpportunity> analyzeXiaohongshuNote(String noteUrl, String noteTitle, String noteContent, Map<String, Object> authorInfo);

    /**
     * 批量分析社交媒体内容
     * @param socialMediaContents 社交媒体内容列表
     * @return 商机提取结果映射
     */
    Map<String, List<BusinessOpportunity>> analyzeBatchContent(List<SocialMediaContent> socialMediaContents);

    /**
     * 智能识别内容中的产品需求
     * @param content 文本内容
     * @param platform 平台类型（douyin/wechat/xiaohongshu）
     * @return 识别到的产品需求列表
     */
    List<Map<String, Object>> extractProductRequirements(String content, String platform);

    /**
     * 分析内容中的品牌提及
     * @param content 文本内容
     * @return 品牌提及信息列表
     */
    List<Map<String, Object>> extractBrandMentions(String content);

    /**
     * 评估内容的商业价值和转化潜力
     * @param content 社交媒体内容
     * @return 商业价值评分和评估结果
     */
    Map<String, Object> evaluateCommercialValue(SocialMediaContent content);

    /**
     * 获取热门话题和趋势
     * @param platform 平台类型
     * @param category 内容分类
     * @param timeRange 时间范围（如：7d, 30d, 90d）
     * @return 热门话题列表
     */
    List<Map<String, Object>> getTrendingTopics(String platform, String category, String timeRange);

    /**
     * 监控指定关键词的社交媒体提及
     * @param keywords 关键词列表
     * @param platforms 平台列表
     * @param monitoringPeriod 监控周期（小时）
     * @return 监控结果
     */
    Map<String, Object> monitorKeywordMentions(List<String> keywords, List<String> platforms, int monitoringPeriod);

    /**
     * 生成社交媒体商机报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param platforms 平台列表
     * @return 商机分析报告
     */
    Map<String, Object> generateSocialMediaReport(String startDate, String endDate, List<String> platforms);

    /**
     * 设置社交媒体监控配置
     * @param config 监控配置参数
     * @return 配置结果
     */
    boolean setMonitoringConfig(Map<String, Object> config);

    /**
     * 获取社交媒体监控状态
     * @return 监控状态信息
     */
    Map<String, Object> getMonitoringStatus();

    /**
     * 分析竞争对手的社交媒体表现
     * @param competitors 竞争对手列表
     * @param platforms 平台列表
     * @param analysisPeriod 分析周期
     * @return 竞争对手分析报告
     */
    Map<String, Object> analyzeCompetitorPerformance(List<String> competitors, List<String> platforms, String analysisPeriod);
}
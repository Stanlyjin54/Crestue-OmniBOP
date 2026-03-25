package com.kuafu.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.common.domin.Result;
import com.kuafu.web.entity.SocialMediaContent;
import com.kuafu.web.entity.BusinessOpportunity;
import com.kuafu.web.mapper.SocialMediaContentMapper;
import com.kuafu.web.service.ISocialMediaOpportunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社交媒体商机获取控制器
 * 处理抖音、微信、小红书等平台的种草内容分析和商机提取
 */
@Slf4j
@RestController
@RequestMapping("/api/social-media")
public class SocialMediaOpportunityController {

    @Autowired
    private ISocialMediaOpportunityService socialMediaOpportunityService;

    @Autowired
    private SocialMediaContentMapper socialMediaContentMapper;

    /**
     * 分析抖音视频内容
     */
    @PostMapping("/douyin/analyze")
    public Result<List<BusinessOpportunity>> analyzeDouyinContent(
            @RequestParam String videoUrl,
            @RequestParam String videoTitle,
            @RequestParam(required = false) String videoDescription,
            @RequestBody Map<String, Object> authorInfo) {
        
        try {
            log.info("开始分析抖音视频: {}", videoTitle);
            
            if (StrUtil.isBlank(videoUrl) || StrUtil.isBlank(videoTitle)) {
                return Result.error("视频URL和标题不能为空");
            }
            
            List<BusinessOpportunity> opportunities = socialMediaOpportunityService.analyzeDouyinContent(
                videoUrl, videoTitle, videoDescription, authorInfo);
            
            log.info("抖音视频分析完成，找到 {} 个商机", opportunities.size());
            return Result.<List<BusinessOpportunity>>success(opportunities);
            
        } catch (Exception e) {
            log.error("分析抖音视频失败", e);
            return Result.error("分析抖音视频失败: " + e.getMessage());
        }
    }

    /**
     * 分析微信公众号文章
     */
    @PostMapping("/wechat/analyze")
    public Result<List<BusinessOpportunity>> analyzeWechatArticle(
            @RequestParam String articleUrl,
            @RequestParam String articleTitle,
            @RequestParam String articleContent,
            @RequestBody Map<String, Object> authorInfo) {
        
        try {
            log.info("开始分析微信公众号文章: {}", articleTitle);
            
            if (StrUtil.isBlank(articleUrl) || StrUtil.isBlank(articleTitle) || StrUtil.isBlank(articleContent)) {
                return Result.error("文章URL、标题和内容不能为空");
            }
            
            List<BusinessOpportunity> opportunities = socialMediaOpportunityService.analyzeWechatArticle(
                articleUrl, articleTitle, articleContent, authorInfo);
            
            log.info("微信公众号文章分析完成，找到 {} 个商机", opportunities.size());
            return Result.success(opportunities);
            
        } catch (Exception e) {
            log.error("分析微信公众号文章失败", e);
            return Result.error("分析微信公众号文章失败: " + e.getMessage());
        }
    }

    /**
     * 分析小红书笔记
     */
    @PostMapping("/xiaohongshu/analyze")
    public Result<List<BusinessOpportunity>> analyzeXiaohongshuNote(
            @RequestParam String noteUrl,
            @RequestParam String noteTitle,
            @RequestParam String noteContent,
            @RequestBody Map<String, Object> authorInfo) {
        
        try {
            log.info("开始分析小红书笔记: {}", noteTitle);
            
            if (StrUtil.isBlank(noteUrl) || StrUtil.isBlank(noteTitle) || StrUtil.isBlank(noteContent)) {
                return Result.error("笔记URL、标题和内容不能为空");
            }
            
            List<BusinessOpportunity> opportunities = socialMediaOpportunityService.analyzeXiaohongshuNote(
                noteUrl, noteTitle, noteContent, authorInfo);
            
            log.info("小红书笔记分析完成，找到 {} 个商机", opportunities.size());
            return Result.success(opportunities);
            
        } catch (Exception e) {
            log.error("分析小红书笔记失败", e);
            return Result.error("分析小红书笔记失败: " + e.getMessage());
        }
    }

    /**
     * 批量分析社交媒体内容
     */
    @PostMapping("/batch/analyze")
    public Result<Map<String, List<BusinessOpportunity>>> analyzeBatchContent(@RequestBody List<SocialMediaContent> socialMediaContents) {
        try {
            log.info("开始批量分析 {} 条社交媒体内容", socialMediaContents.size());
            
            if (socialMediaContents == null || socialMediaContents.isEmpty()) {
                return Result.error("社交媒体内容列表不能为空");
            }
            
            Map<String, List<BusinessOpportunity>> results = socialMediaOpportunityService.analyzeBatchContent(socialMediaContents);
            
            int totalOpportunities = results.values().stream().mapToInt(List::size).sum();
            log.info("批量分析完成，共找到 {} 个商机", totalOpportunities);
            
            return Result.success(results);
            
        } catch (Exception e) {
            log.error("批量分析社交媒体内容失败", e);
            return Result.error("批量分析失败: " + e.getMessage());
        }
    }

    /**
     * 提取产品需求
     */
    @PostMapping("/extract-requirements")
    public Result<List<Map<String, Object>>> extractProductRequirements(
            @RequestParam String content,
            @RequestParam String platform) {
        
        try {
            if (StrUtil.isBlank(content) || StrUtil.isBlank(platform)) {
                return Result.error("内容和平台不能为空");
            }
            
            List<Map<String, Object>> requirements = socialMediaOpportunityService.extractProductRequirements(content, platform);
            return Result.success(requirements);
            
        } catch (Exception e) {
            log.error("提取产品需求失败", e);
            return Result.error("提取产品需求失败: " + e.getMessage());
        }
    }

    /**
     * 提取品牌提及
     */
    @PostMapping("/extract-brands")
    public Result<List<Map<String, Object>>> extractBrandMentions(@RequestParam String content) {
        try {
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            List<Map<String, Object>> brandMentions = socialMediaOpportunityService.extractBrandMentions(content);
            return Result.success(brandMentions);
            
        } catch (Exception e) {
            log.error("提取品牌提及失败", e);
            return Result.error("提取品牌提及失败: " + e.getMessage());
        }
    }

    /**
     * 评估商业价值
     */
    @PostMapping("/evaluate-commercial-value")
    public Result<Map<String, Object>> evaluateCommercialValue(@RequestBody SocialMediaContent content) {
        try {
            if (content == null) {
                return Result.error("内容不能为空");
            }
            
            Map<String, Object> evaluation = socialMediaOpportunityService.evaluateCommercialValue(content);
            return Result.success(evaluation);
            
        } catch (Exception e) {
            log.error("评估商业价值失败", e);
            return Result.error("评估商业价值失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门话题
     */
    @GetMapping("/trending-topics")
    public Result<List<Map<String, Object>>> getTrendingTopics(
            @RequestParam(required = false, defaultValue = "all") String platform,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "7d") String timeRange) {
        
        try {
            List<Map<String, Object>> trendingTopics = socialMediaOpportunityService.getTrendingTopics(platform, category, timeRange);
            return Result.success(trendingTopics);
            
        } catch (Exception e) {
            log.error("获取热门话题失败", e);
            return Result.error("获取热门话题失败: " + e.getMessage());
        }
    }

    /**
     * 监控关键词提及
     */
    @PostMapping("/monitor-keywords")
    public Result<Map<String, Object>> monitorKeywordMentions(
            @RequestBody List<String> keywords,
            @RequestParam(required = false) List<String> platforms,
            @RequestParam(required = false, defaultValue = "24") int monitoringPeriod) {
        
        try {
            if (keywords == null || keywords.isEmpty()) {
                return Result.error("关键词列表不能为空");
            }
            
            if (platforms == null || platforms.isEmpty()) {
                platforms = Arrays.asList("douyin", "wechat", "xiaohongshu");
            }
            
            Map<String, Object> monitoringResult = socialMediaOpportunityService.monitorKeywordMentions(keywords, platforms, monitoringPeriod);
            return Result.success(monitoringResult);
            
        } catch (Exception e) {
            log.error("监控关键词提及失败", e);
            return Result.error("监控关键词提及失败: " + e.getMessage());
        }
    }

    /**
     * 生成社交媒体报告
     */
    @GetMapping("/report")
    public Result<Map<String, Object>> generateSocialMediaReport(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) List<String> platforms) {
        
        try {
            if (StrUtil.isBlank(startDate) || StrUtil.isBlank(endDate)) {
                return Result.error("开始日期和结束日期不能为空");
            }
            
            if (platforms == null || platforms.isEmpty()) {
                platforms = Arrays.asList("douyin", "wechat", "xiaohongshu");
            }
            
            Map<String, Object> report = socialMediaOpportunityService.generateSocialMediaReport(startDate, endDate, platforms);
            return Result.success(report);
            
        } catch (Exception e) {
            log.error("生成社交媒体报告失败", e);
            return Result.error("生成报告失败: " + e.getMessage());
        }
    }

    /**
     * 设置监控配置
     */
    @PostMapping("/monitoring-config")
    public Result<Boolean> setMonitoringConfig(@RequestBody Map<String, Object> config) {
        try {
            boolean success = socialMediaOpportunityService.setMonitoringConfig(config);
            return success ? Result.success(true) : Result.error("设置监控配置失败");
            
        } catch (Exception e) {
            log.error("设置监控配置失败", e);
            return Result.error("设置监控配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取监控状态
     */
    @GetMapping("/monitoring-status")
    public Result<Map<String, Object>> getMonitoringStatus() {
        try {
            Map<String, Object> status = socialMediaOpportunityService.getMonitoringStatus();
            return Result.success(status);
            
        } catch (Exception e) {
            log.error("获取监控状态失败", e);
            return Result.error("获取监控状态失败: " + e.getMessage());
        }
    }

    /**
     * 分析竞争对手表现
     */
    @PostMapping("/competitor-analysis")
    public Result<Map<String, Object>> analyzeCompetitorPerformance(
            @RequestBody List<String> competitors,
            @RequestParam(required = false) List<String> platforms,
            @RequestParam(required = false, defaultValue = "30d") String analysisPeriod) {
        
        try {
            if (competitors == null || competitors.isEmpty()) {
                return Result.error("竞争对手列表不能为空");
            }
            
            if (platforms == null || platforms.isEmpty()) {
                platforms = Arrays.asList("douyin", "wechat", "xiaohongshu");
            }
            
            Map<String, Object> analysis = socialMediaOpportunityService.analyzeCompetitorPerformance(competitors, platforms, analysisPeriod);
            return Result.success(analysis);
            
        } catch (Exception e) {
            log.error("分析竞争对手表现失败", e);
            return Result.error("分析竞争对手失败: " + e.getMessage());
        }
    }

    /**
     * 查询社交媒体内容列表
     */
    @GetMapping("/contents")
    public Result<List<SocialMediaContent>> getSocialMediaContents(
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String contentType,
            @RequestParam(required = false) Boolean processed,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyHighValue,
            @RequestParam(required = false, defaultValue = "50") int limit) {
        
        try {
            QueryWrapper<SocialMediaContent> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("collect_time");
            wrapper.last("LIMIT " + limit);
            
            if (StrUtil.isNotBlank(platform)) {
                wrapper.eq("platform", platform);
            }
            
            if (StrUtil.isNotBlank(contentType)) {
                wrapper.eq("content_type", contentType);
            }
            
            if (processed != null) {
                wrapper.eq("processed", processed);
            }
            
            if (onlyHighValue) {
                wrapper.ge("commercial_score", 70);
            }
            
            List<SocialMediaContent> contents = socialMediaContentMapper.selectList(wrapper);
            return Result.success(contents);
            
        } catch (Exception e) {
            log.error("查询社交媒体内容失败", e);
            return Result.error("查询内容失败: " + e.getMessage());
        }
    }

    /**
     * 获取内容统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总内容数
            long totalContent = socialMediaContentMapper.selectCount(null);
            statistics.put("totalContent", totalContent);
            
            // 各平台分布
            List<Map<String, Object>> platformDistribution = socialMediaContentMapper.countByPlatform();
            statistics.put("platformDistribution", platformDistribution);
            
            // 处理状态统计
            List<Map<String, Object>> conversionStats = socialMediaContentMapper.countOpportunityConversion();
            statistics.put("conversionStats", conversionStats);
            
            // 高价值内容数
            List<SocialMediaContent> highValueContent = socialMediaContentMapper.selectHighValueContent(70);
            statistics.put("highValueContentCount", highValueContent.size());
            
            return Result.success(statistics);
            
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return Result.error("获取统计信息失败: " + e.getMessage());
        }
    }
}
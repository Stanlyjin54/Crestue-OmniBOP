package com.kuafu.web.controller;

import cn.hutool.core.util.StrUtil;
import com.kuafu.web.entity.Result;
import com.kuafu.web.service.ISocialMediaComplianceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 社交媒体内容合规性检查控制器
 * 处理抖音、微信、小红书等平台的内容合规性检查
 */
@Slf4j
@RestController
@RequestMapping("/api/social-media-compliance")
public class SocialMediaComplianceController {

    @Autowired
    private ISocialMediaComplianceService socialMediaComplianceService;

    /**
     * 检查社交媒体内容合规性
     */
    @PostMapping("/check")
    public Result<Map<String, Object>> checkContentCompliance(
            @RequestParam String content,
            @RequestParam String platform,
            @RequestParam(required = false, defaultValue = "text") String contentType) {
        
        try {
            log.info("开始检查社交媒体内容合规性，平台: {}, 类型: {}", platform, contentType);
            
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            if (StrUtil.isBlank(platform)) {
                return Result.error("平台不能为空");
            }
            
            Map<String, Object> result = socialMediaComplianceService.checkContentCompliance(content, platform, contentType);
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("检查社交媒体内容合规性失败", e);
            return Result.error("合规性检查失败: " + e.getMessage());
        }
    }

    /**
     * 批量检查内容合规性
     */
    @PostMapping("/batch-check")
    public Result<List<Map<String, Object>>> batchCheckContentCompliance(@RequestBody List<Map<String, String>> contentList) {
        try {
            log.info("开始批量检查 {} 条社交媒体内容合规性", contentList.size());
            
            if (contentList == null || contentList.isEmpty()) {
                return Result.error("内容列表不能为空");
            }
            
            List<Map<String, Object>> results = new ArrayList<>();
            
            for (Map<String, String> contentInfo : contentList) {
                try {
                    String content = contentInfo.get("content");
                    String platform = contentInfo.get("platform");
                    String contentType = contentInfo.getOrDefault("contentType", "text");
                    String contentId = contentInfo.get("contentId");
                    
                    if (StrUtil.isBlank(content) || StrUtil.isBlank(platform)) {
                        Map<String, Object> errorResult = new HashMap<>();
                        errorResult.put("contentId", contentId);
                        errorResult.put("error", "内容或平台信息不完整");
                        results.add(errorResult);
                        continue;
                    }
                    
                    Map<String, Object> checkResult = socialMediaComplianceService.checkContentCompliance(content, platform, contentType);
                    checkResult.put("contentId", contentId);
                    results.add(checkResult);
                    
                } catch (Exception e) {
                    log.error("检查单条内容失败: {}", contentInfo, e);
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("contentId", contentInfo.get("contentId"));
                    errorResult.put("error", "检查失败: " + e.getMessage());
                    results.add(errorResult);
                }
            }
            
            log.info("批量合规性检查完成");
            return Result.success(results);
            
        } catch (Exception e) {
            log.error("批量检查社交媒体内容合规性失败", e);
            return Result.error("批量检查失败: " + e.getMessage());
        }
    }

    /**
     * 基础内容检查
     */
    @PostMapping("/basic-check")
    public Result<Map<String, Object>> checkBasicContent(@RequestParam String content) {
        try {
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            Map<String, Object> result = socialMediaComplianceService.checkBasicContent(content);
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("基础内容检查失败", e);
            return Result.error("基础检查失败: " + e.getMessage());
        }
    }

    /**
     * 平台特定规则检查
     */
    @PostMapping("/platform-check")
    public Result<Map<String, Object>> checkPlatformSpecificRules(
            @RequestParam String content,
            @RequestParam String platform,
            @RequestParam(required = false, defaultValue = "text") String contentType) {
        
        try {
            if (StrUtil.isBlank(content) || StrUtil.isBlank(platform)) {
                return Result.error("内容和平台不能为空");
            }
            
            Map<String, Object> result = socialMediaComplianceService.checkPlatformSpecificRules(content, platform, contentType);
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("平台特定规则检查失败", e);
            return Result.error("平台检查失败: " + e.getMessage());
        }
    }

    /**
     * 敏感内容检测
     */
    @PostMapping("/sensitive-check")
    public Result<Map<String, Object>> checkSensitiveContent(@RequestParam String content) {
        try {
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            Map<String, Object> result = socialMediaComplianceService.checkSensitiveContent(content);
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("敏感内容检测失败", e);
            return Result.error("敏感内容检测失败: " + e.getMessage());
        }
    }

    /**
     * 广告合规检查
     */
    @PostMapping("/advertising-check")
    public Result<Map<String, Object>> checkAdvertisingCompliance(@RequestParam String content) {
        try {
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            Map<String, Object> result = socialMediaComplianceService.checkAdvertisingCompliance(content);
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("广告合规检查失败", e);
            return Result.error("广告检查失败: " + e.getMessage());
        }
    }

    /**
     * 版权检查
     */
    @PostMapping("/copyright-check")
    public Result<Map<String, Object>> checkCopyrightIssues(@RequestParam String content) {
        try {
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            Map<String, Object> result = socialMediaComplianceService.checkCopyrightIssues(content);
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("版权检查失败", e);
            return Result.error("版权检查失败: " + e.getMessage());
        }
    }

    /**
     * KOL/网红合规检查
     */
    @PostMapping("/influencer-check")
    public Result<Map<String, Object>> checkInfluencerCompliance(
            @RequestParam String content,
            @RequestBody Map<String, Object> influencerInfo) {
        
        try {
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            Map<String, Object> result = socialMediaComplianceService.checkInfluencerCompliance(content, influencerInfo);
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("KOL合规检查失败", e);
            return Result.error("KOL检查失败: " + e.getMessage());
        }
    }

    /**
     * 生成合规报告
     */
    @PostMapping("/compliance-report")
    public Result<Map<String, Object>> generateComplianceReport(
            @RequestBody List<String> contentIds,
            @RequestParam(required = false) String platform) {
        
        try {
            if (contentIds == null || contentIds.isEmpty()) {
                return Result.error("内容ID列表不能为空");
            }
            
            Map<String, Object> report = socialMediaComplianceService.generateComplianceReport(contentIds, platform);
            return Result.success(report);
            
        } catch (Exception e) {
            log.error("生成合规报告失败", e);
            return Result.error("生成报告失败: " + e.getMessage());
        }
    }

    /**
     * 设置合规配置
     */
    @PostMapping("/compliance-config")
    public Result<Boolean> setComplianceConfig(@RequestBody Map<String, Object> config) {
        try {
            if (config == null || config.isEmpty()) {
                return Result.error("配置不能为空");
            }
            
            boolean success = socialMediaComplianceService.setComplianceConfig(config);
            return success ? Result.success(true) : Result.error("设置合规配置失败");
            
        } catch (Exception e) {
            log.error("设置合规配置失败", e);
            return Result.error("设置配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取合规状态
     */
    @GetMapping("/compliance-status")
    public Result<Map<String, Object>> getComplianceStatus() {
        try {
            Map<String, Object> status = socialMediaComplianceService.getComplianceStatus();
            return Result.success(status);
            
        } catch (Exception e) {
            log.error("获取合规状态失败", e);
            return Result.error("获取状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取平台合规规则
     */
    @GetMapping("/platform-rules")
    public Result<Map<String, Object>> getPlatformRules(@RequestParam String platform) {
        try {
            if (StrUtil.isBlank(platform)) {
                return Result.error("平台不能为空");
            }
            
            // 返回平台特定的合规规则
            Map<String, Object> rules = new HashMap<>();
            
            switch (platform.toLowerCase()) {
                case "douyin":
                    rules.put("maxVideoDuration", 600);
                    rules.put("minFollowersForCommercial", 1000);
                    rules.put("prohibitedCategories", Arrays.asList("医疗", "药品", "保健品", "金融投资", "烟草", "酒精"));
                    rules.put("requiredDisclaimers", Arrays.asList("广告", "推广"));
                    break;
                    
                case "wechat":
                    rules.put("minArticleLength", 300);
                    rules.put("maxAdvertisingRatio", 0.3);
                    rules.put("prohibitedCategories", Arrays.asList("色情", "赌博", "毒品", "枪支", "政治"));
                    rules.put("requiredTags", Arrays.asList("原创", "转载"));
                    break;
                    
                case "xiaohongshu":
                    rules.put("minNoteLength", 50);
                    rules.put("maxHashtags", 10);
                    rules.put("prohibitedCategories", Arrays.asList("医疗", "药品", "烟草", "酒精", "成人用品"));
                    rules.put("requiredLabels", Arrays.asList("种草", "测评", "分享"));
                    break;
                    
                default:
                    return Result.error("未知平台: " + platform);
            }
            
            rules.put("platform", platform);
            rules.put("updateTime", new Date());
            
            return Result.success(rules);
            
        } catch (Exception e) {
            log.error("获取平台规则失败", e);
            return Result.error("获取规则失败: " + e.getMessage());
        }
    }

    /**
     * 实时内容监控
     */
    @PostMapping("/realtime-monitor")
    public Result<Map<String, Object>> realtimeContentMonitor(
            @RequestBody List<Map<String, String>> contentList,
            @RequestParam(required = false, defaultValue = "strict") String monitoringLevel) {
        
        try {
            if (contentList == null || contentList.isEmpty()) {
                return Result.error("监控内容列表不能为空");
            }
            
            Map<String, Object> monitoringResult = new HashMap<>();
            List<Map<String, Object>> checkResults = new ArrayList<>();
            int totalViolations = 0;
            int totalWarnings = 0;
            
            for (Map<String, String> contentInfo : contentList) {
                try {
                    String content = contentInfo.get("content");
                    String platform = contentInfo.get("platform");
                    String contentId = contentInfo.get("contentId");
                    
                    if (StrUtil.isBlank(content) || StrUtil.isBlank(platform)) {
                        continue;
                    }
                    
                    Map<String, Object> checkResult = socialMediaComplianceService.checkContentCompliance(content, platform, "text");
                    checkResult.put("contentId", contentId);
                    checkResults.add(checkResult);
                    
                    if (!(Boolean) checkResult.get("isCompliant")) {
                        totalViolations++;
                    }
                    
                    List<String> warnings = (List<String>) checkResult.get("warnings");
                    if (warnings != null && !warnings.isEmpty()) {
                        totalWarnings++;
                    }
                    
                } catch (Exception e) {
                    log.error("实时监控单条内容失败", e);
                }
            }
            
            monitoringResult.put("totalChecked", contentList.size());
            monitoringResult.put("totalViolations", totalViolations);
            monitoringResult.put("totalWarnings", totalWarnings);
            monitoringResult.put("violationRate", String.format("%.1f%%", (double) totalViolations / contentList.size() * 100));
            monitoringResult.put("warningRate", String.format("%.1f%%", (double) totalWarnings / contentList.size() * 100));
            monitoringResult.put("monitoringLevel", monitoringLevel);
            monitoringResult.put("checkResults", checkResults);
            monitoringResult.put("monitorTime", new Date());
            
            return Result.success(monitoringResult);
            
        } catch (Exception e) {
            log.error("实时内容监控失败", e);
            return Result.error("实时监控失败: " + e.getMessage());
        }
    }
}
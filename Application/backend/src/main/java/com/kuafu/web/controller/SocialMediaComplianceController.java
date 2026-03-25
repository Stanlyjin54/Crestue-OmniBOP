package com.kuafu.web.controller;

import cn.hutool.core.util.StrUtil;
import com.kuafu.common.domin.Result;
import com.kuafu.web.service.ISocialMediaComplianceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社交媒体内容合规性检查控制器
 * 提供对抖音、微信、小红书等平台内容的合规性检查功能
 */
@RestController
@RequestMapping("/api/social-media/compliance")
@Slf4j
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
            log.info("开始批量检查{}条社交媒体内容合规性", contentList.size());
            
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
            log.error("平台规则检查失败", e);
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
            @RequestBody(required = false) Map<String, Object> influencerInfo) {
        try {
            if (StrUtil.isBlank(content)) {
                return Result.error("内容不能为空");
            }
            
            if (influencerInfo == null) {
                influencerInfo = new HashMap<>();
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
    @PostMapping("/generate-report")
    public Result<Map<String, Object>> generateComplianceReport(
            @RequestBody List<String> contentIds,
            @RequestParam String platform) {
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
    @PostMapping("/config")
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
    @GetMapping("/status")
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
     * 获取平台规则
     */
    @GetMapping("/platform-rules")
    public Result<Map<String, Object>> getPlatformRules(@RequestParam String platform) {
        try {
            if (StrUtil.isBlank(platform)) {
                return Result.error("平台不能为空");
            }
            
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
                    rules.put("requiredDisclaimers", Arrays.asList("广告", "推广"));
                    break;
                    
                case "xiaohongshu":
                    rules.put("minNoteLength", 50);
                    rules.put("maxImageCount", 9);
                    rules.put("prohibitedCategories", Arrays.asList("医疗", "药品", "保健品", "金融投资"));
                    rules.put("requiredDisclaimers", Arrays.asList("广告", "赞助", "合作"));
                    break;
                    
                default:
                    return Result.error("未知平台: " + platform);
            }
            
            return Result.success(rules);
            
        } catch (Exception e) {
            log.error("获取平台规则失败", e);
            return Result.error("获取规则失败: " + e.getMessage());
        }
    }

    /**
     * 实时监控内容
     */
    @PostMapping("/monitor")
    public Result<Map<String, Object>> monitorContent(@RequestBody List<Map<String, Object>> contentList) {
        try {
            if (contentList == null || contentList.isEmpty()) {
                return Result.error("监控内容列表不能为空");
            }
            
            Map<String, Object> monitoringResult = new HashMap<>();
            List<Map<String, Object>> violations = new ArrayList<>();
            
            for (Map<String, Object> contentInfo : contentList) {
                String content = (String) contentInfo.get("content");
                String platform = (String) contentInfo.get("platform");
                String contentType = (String) contentInfo.getOrDefault("contentType", "text");
                
                Map<String, Object> complianceResult = socialMediaComplianceService.checkContentCompliance(content, platform, contentType);
                
                if ((Boolean) complianceResult.getOrDefault("isCompliant", true)) {
                    Map<String, Object> violation = new HashMap<>();
                    violation.put("contentId", contentInfo.get("contentId"));
                    violation.put("platform", platform);
                    violation.put("violations", complianceResult.get("violations"));
                    violations.add(violation);
                }
            }
            
            monitoringResult.put("totalChecked", contentList.size());
            monitoringResult.put("violations", violations);
            monitoringResult.put("complianceRate", (contentList.size() - violations.size()) * 100.0 / contentList.size());
            
            return Result.success(monitoringResult);
            
        } catch (Exception e) {
            log.error("实时监控失败", e);
            return Result.error("实时监控失败: " + e.getMessage());
        }
    }
}
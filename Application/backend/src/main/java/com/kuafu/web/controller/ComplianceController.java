package com.kuafu.web.controller;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.web.entity.AccessLog;
import com.kuafu.web.service.IComplianceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 合规性管理控制器
 * 提供合规性检查和访问控制相关接口
 */
@Api(tags = "合规性管理")
@Slf4j
@RestController
@RequestMapping("/api/compliance")
public class ComplianceController {

    @Autowired
    private IComplianceService complianceService;

    @ApiOperation("检查URL是否可以爬取")
    @GetMapping("/check-crawl")
    public BaseResponse<Boolean> checkCrawl(
            @ApiParam("待检查的URL") @RequestParam String url,
            @ApiParam("用户代理") @RequestParam(required = false, defaultValue = "QiusCRM-Bot/1.0") String userAgent) {
        try {
            boolean canCrawl = complianceService.canCrawl(url);
            return ResultUtils.success(canCrawl);
        } catch (Exception e) {
            log.error("检查URL爬取权限失败: {}", url, e);
            return ResultUtils.error("检查失败: " + e.getMessage());
        }
    }

    @ApiOperation("检查robots.txt规则")
    @GetMapping("/check-robots")
    public BaseResponse<Boolean> checkRobotsTxt(
            @ApiParam("待检查的URL") @RequestParam String url) {
        try {
            boolean isCompliant = complianceService.checkRobotsTxt(url);
            return ResultUtils.success(isCompliant);
        } catch (Exception e) {
            log.error("检查robots.txt规则失败: {}", url, e);
            return ResultUtils.error("检查失败: " + e.getMessage());
        }
    }

    @ApiOperation("检查访问频率限制")
    @GetMapping("/check-rate-limit")
    public BaseResponse<Map<String, Object>> checkRateLimit(
            @ApiParam("域名") @RequestParam String domain,
            @ApiParam("用户代理") @RequestParam(required = false, defaultValue = "QiusCRM-Bot/1.0") String userAgent) {
        try {
            boolean withinLimit = complianceService.checkRateLimit(domain, userAgent);
            Map<String, Object> result = new HashMap<>();
            result.put("withinLimit", withinLimit);
            result.put("domain", domain);
            result.put("message", withinLimit ? "访问频率在限制范围内" : "访问频率超限");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("检查访问频率限制失败: {}", domain, e);
            return ResultUtils.error("检查失败: " + e.getMessage());
        }
    }

    @ApiOperation("记录访问日志")
    @PostMapping("/log-access")
    public BaseResponse<Void> logAccess(@RequestBody AccessLog accessLog) {
        try {
            complianceService.logAccess(
                accessLog.getUrl(),
                accessLog.getUserAgent(),
                accessLog.getSuccess(),
                accessLog.getResponseTime()
            );
            return ResultUtils.success();
        } catch (Exception e) {
            log.error("记录访问日志失败", e);
            return ResultUtils.error("记录日志失败: " + e.getMessage());
        }
    }

    @ApiOperation("验证内容合规性")
    @PostMapping("/validate-content")
    public BaseResponse<Map<String, Object>> validateContent(
            @ApiParam("待验证的内容") @RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            boolean isValid = complianceService.validateContent(content);
            Map<String, Object> result = new HashMap<>();
            result.put("isValid", isValid);
            result.put("message", isValid ? "内容合规" : "内容包含敏感信息或需要人工审核");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("验证内容合规性失败", e);
            return ResultUtils.error("验证失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取合规配置")
    @GetMapping("/config")
    public BaseResponse<Map<String, Object>> getComplianceConfig() {
        try {
            Map<String, Object> config = complianceService.getComplianceConfig();
            return ResultUtils.success(config);
        } catch (Exception e) {
            log.error("获取合规配置失败", e);
            return ResultUtils.error("获取配置失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新合规配置")
    @PutMapping("/config")
    public BaseResponse<Map<String, Object>> updateComplianceConfig(
            @ApiParam("新的配置项") @RequestBody Map<String, Object> config) {
        try {
            complianceService.updateComplianceConfig(config);
            Map<String, Object> updatedConfig = complianceService.getComplianceConfig();
            return ResultUtils.success(updatedConfig);
        } catch (Exception e) {
            log.error("更新合规配置失败", e);
            return ResultUtils.error("更新配置失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取访问统计信息")
    @GetMapping("/statistics")
    public BaseResponse<Map<String, Object>> getAccessStatistics(
            @ApiParam("域名，可选") @RequestParam(required = false) String domain,
            @ApiParam("开始日期，格式: yyyy-MM-dd") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期，格式: yyyy-MM-dd") @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> statistics = complianceService.getAccessStatistics(domain, startDate, endDate);
            return ResultUtils.success(statistics);
        } catch (Exception e) {
            log.error("获取访问统计信息失败", e);
            return ResultUtils.error("获取统计信息失败: " + e.getMessage());
        }
    }

    @ApiOperation("检查用户权限")
    @GetMapping("/check-permission")
    public BaseResponse<Map<String, Object>> checkUserPermission(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("权限标识") @RequestParam String permission) {
        try {
            boolean hasPermission = complianceService.hasPermission(userId, permission);
            Map<String, Object> result = new HashMap<>();
            result.put("hasPermission", hasPermission);
            result.put("userId", userId);
            result.put("permission", permission);
            result.put("message", hasPermission ? "用户具有该权限" : "用户不具有该权限");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("检查用户权限失败", e);
            return ResultUtils.error("检查权限失败: " + e.getMessage());
        }
    }

    @ApiOperation("添加域名到白名单")
    @PostMapping("/whitelist")
    public BaseResponse<Map<String, Object>> addToWhitelist(
            @ApiParam("域名") @RequestParam String domain,
            @ApiParam("添加原因") @RequestParam String reason) {
        try {
            boolean success = complianceService.addToWhitelist(domain, reason);
            Map<String, Object> result = new HashMap<>();
            result.put("success", success);
            result.put("domain", domain);
            result.put("message", success ? "域名已添加到白名单" : "添加域名到白名单失败");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("添加域名到白名单失败", e);
            return ResultUtils.error("添加失败: " + e.getMessage());
        }
    }

    @ApiOperation("添加域名到黑名单")
    @PostMapping("/blacklist")
    public BaseResponse<Map<String, Object>> addToBlacklist(
            @ApiParam("域名") @RequestParam String domain,
            @ApiParam("添加原因") @RequestParam String reason) {
        try {
            boolean success = complianceService.addToBlacklist(domain, reason);
            Map<String, Object> result = new HashMap<>();
            result.put("success", success);
            result.put("domain", domain);
            result.put("message", success ? "域名已添加到黑名单" : "添加域名到黑名单失败");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("添加域名到黑名单失败", e);
            return ResultUtils.error("添加失败: " + e.getMessage());
        }
    }

    @ApiOperation("生成合规报告")
    @GetMapping("/report")
    public BaseResponse<Map<String, Object>> generateComplianceReport(
            @ApiParam("开始日期，格式: yyyy-MM-dd") @RequestParam String startDate,
            @ApiParam("结束日期，格式: yyyy-MM-dd") @RequestParam String endDate) {
        try {
            Map<String, Object> report = complianceService.generateComplianceReport(startDate, endDate);
            return ResultUtils.success(report);
        } catch (Exception e) {
            log.error("生成合规报告失败", e);
            return ResultUtils.error("生成报告失败: " + e.getMessage());
        }
    }

    @ApiOperation("检查是否需要人工审核")
    @PostMapping("/check-manual-review")
    public BaseResponse<Map<String, Object>> checkManualReview(
            @ApiParam("待检查的内容") @RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            String source = request.getOrDefault("source", "auto_crawler");
            boolean requiresReview = complianceService.requiresManualReview(content, source);
            Map<String, Object> result = new HashMap<>();
            result.put("requiresReview", requiresReview);
            result.put("message", requiresReview ? "内容需要人工审核" : "内容无需人工审核");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("检查人工审核需求失败", e);
            return ResultUtils.error("检查失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取法律免责声明")
    @GetMapping("/legal-disclaimer")
    public BaseResponse<Map<String, Object>> getLegalDisclaimer() {
        try {
            String disclaimer = complianceService.getLegalDisclaimer();
            Map<String, Object> result = new HashMap<>();
            result.put("disclaimer", disclaimer);
            result.put("message", "请确保在使用本系统时遵守相关法律法规");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("获取法律免责声明失败", e);
            return ResultUtils.error("获取免责声明失败: " + e.getMessage());
        }
    }

    @ApiOperation("检查数据保护合规性")
    @PostMapping("/check-data-protection")
    public BaseResponse<Map<String, Object>> checkDataProtectionCompliance(
            @ApiParam("待检查的数据") @RequestBody Map<String, String> request) {
        try {
            String data = request.get("data");
            boolean isCompliant = complianceService.checkDataProtectionCompliance(data);
            Map<String, Object> result = new HashMap<>();
            result.put("isCompliant", isCompliant);
            result.put("message", isCompliant ? "数据符合保护要求" : "数据包含个人敏感信息，不符合保护要求");
            return ResultUtils.success(result);
        } catch (Exception e) {
            log.error("检查数据保护合规性失败", e);
            return ResultUtils.error("检查失败: " + e.getMessage());
        }
    }
}